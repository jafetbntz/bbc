package dev.bntz.newsfeed.service;

import dev.bntz.newsfeed.model.Post;
import dev.bntz.newsfeed.repository.PostRepository;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.reactive.RedisStringReactiveCommands;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PostServiceImp implements PostService {
    private final PostRepository postRepository;
    private final ReactiveRedisOperations<String, Post> postOps;

    @Override
    public Mono<Post> create(Post post) {
        post.setId(UUID.randomUUID().toString());
        post.setCreateDate(LocalDateTime.now());

        if (post.getSlug() != null) {
            String streamKey = String.format("live:%s", post.getSlug());

            ObjectRecord<String, Post> record = StreamRecords.newRecord()
                    .in(streamKey)
                    .ofObject(post);

            postOps
                    .opsForStream()
                    .add(record);
        }




        return postRepository.save(post);
    }

    @Override
    public Mono<Post> getPostById(String id) {
        return postRepository.findById(id);
    }

    @Override
    public Flux<Post> getAllPosts() {

        return postRepository.findAll(Sort.by("createDate").descending());
    }

    @Override
    public Flux<Post> getPostsBySlug(String slug) {
        return postRepository.findBySlug(slug);
    }

    @Override
    public Flux<Post> streamPostUpdates(String slug) {
        int seconds = 1;


        return Flux.interval(Duration.ofSeconds(seconds)).flatMap(tick -> {
            return postRepository.getUpdates(slug, LocalDateTime.now().minusSeconds(seconds));
        });
    }

    @Override
    public Flux<Object> streamPostUpdatesFromRedis(String slug) {
        int seconds = 1;
        String streamKey = String.format("live:%s", slug);
        return Flux.interval(Duration.ofSeconds(seconds)).flatMap(t -> {
            return postOps.opsForStream()
                    .read(Post.class, StreamOffset.latest(streamKey))
                    .flatMap(e -> {
                        System.out.println(e);
                        return Flux.just(e);
                    });
        });
    }
}

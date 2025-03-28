package dev.bntz.newsfeed.service;

import dev.bntz.newsfeed.model.Post;
import dev.bntz.newsfeed.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostServiceImp implements PostService {
    private final PostRepository postRepository;

    @Override
    public Mono<Post> create(Post post) {
        post.setId(UUID.randomUUID().toString());
        post.setCreateDate(LocalDateTime.now());
        return postRepository.save(post);
    }

    @Override
    public Mono<Post> getPostById(String id) {
        return postRepository.findById(id);
    }

    @Override
    public Flux<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public Flux<Post> getPostsBySlug(String slug) {
        return postRepository.findBySlug(slug);
    }

    @Override
    public Flux<Post> streamPostUpdates(String slug) {
        int seconds = 3;
        return Flux.interval(Duration.ofSeconds(seconds)).flatMap(tick -> {

            return postRepository.getUpdates(slug, LocalDateTime.now().minusSeconds(seconds));

        });
    }
}

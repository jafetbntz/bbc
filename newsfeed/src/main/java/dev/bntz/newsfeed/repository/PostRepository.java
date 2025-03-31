package dev.bntz.newsfeed.repository;

import dev.bntz.newsfeed.model.Post;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

@Repository
public interface PostRepository extends ReactiveMongoRepository<Post, String> {


    @Query(sort="{'createDate': -1}")
    Flux<Post> findBySlug(String slug);

    @Query(value="{ 'slug': ?0, 'createDate': { '$gt': ?1 } }", sort="{'createDate': -1}")
    Flux<Post> getUpdates(String slug, LocalDateTime createDate);
}

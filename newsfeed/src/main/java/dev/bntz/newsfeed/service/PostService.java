package dev.bntz.newsfeed.service;

import dev.bntz.newsfeed.model.Post;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PostService {

    Mono<Post> create(Post post);
    Mono<Post> getPostById(String id);
    Flux<Post> getAllPosts();
    Flux<Post> getPostsBySlug(String slug);
    Flux<Post> streamPostUpdates(String slug);
    Flux<Object> streamPostUpdatesFromRedis(String slug);

}

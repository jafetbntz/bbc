package dev.bntz.newsfeed.controller;

import dev.bntz.newsfeed.model.Post;
import dev.bntz.newsfeed.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Post> create(@RequestBody Post post) {
        return postService.create(post);
    }


    @GetMapping("/{id}")
    public Mono<ResponseEntity<Post >> getById(@PathVariable String id) {
        return postService.getPostById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Flux<Post> geAll() {
        return postService.getAllPosts();
    }

    @GetMapping(value="/live/{slug}")
    public Flux<Post> getBySlug(@PathVariable String slug) {
        return postService.getPostsBySlug(slug);
    }

    @GetMapping(value="/stream/{slug}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Object> stream(@PathVariable String slug) {
        return postService.streamPostUpdatesFromRedis(slug);
    }

}

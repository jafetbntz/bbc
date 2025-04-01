package dev.bntz.newsfeed;

import dev.bntz.newsfeed.model.Post;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class NewsFeedConfiguration {

    @Bean
    ReactiveRedisOperations<String, Post> redisOperations(ReactiveRedisConnectionFactory factory) {
        Jackson2JsonRedisSerializer<Post> serializer = new Jackson2JsonRedisSerializer<>(Post.class);

        RedisSerializationContext.RedisSerializationContextBuilder<String, Post> builder =
                RedisSerializationContext.newSerializationContext(new StringRedisSerializer());

        RedisSerializationContext<String, Post> context = builder.value(serializer).build();

        return new ReactiveRedisTemplate<>(factory, context);
    }
}

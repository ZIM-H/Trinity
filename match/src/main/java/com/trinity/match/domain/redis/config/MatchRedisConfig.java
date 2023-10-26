package com.trinity.match.domain.redis.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableRedisRepositories(redisTemplateRef = "matchRedisTemplate")
@RequiredArgsConstructor
public class MatchRedisConfig {
    private final RedisProperties redisProperties;

    @Bean
    public RedisConnectionFactory matchRedisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        RedisProperties.Server server = redisProperties.getMatchServer();
        redisStandaloneConfiguration.setHostName(server.getHost());
        redisStandaloneConfiguration.setPort(server.getPort());
        redisStandaloneConfiguration.setPassword(server.getPassword());

        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    public RedisTemplate<String, String> matchRedisTemplate() {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(matchRedisConnectionFactory());

        /*
            redisTemplate를 사용할 때 Spring-Redis 간 데이터 직렬화, 역직렬화 시 사용하는 방식이 JDK 직렬화 방식
            redis-cli를 통해 쉽게 알아보려고 설정 -> 안해도 동작에는 문제가 없음
         */
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());

        return redisTemplate;
    }
}

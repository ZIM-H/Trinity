package com.trinity.match.global.redis.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Value("${spring.redis.matchServer.host}")
    private String matchHost;
    @Value("${spring.redis.matchServer.port}")
    private int matchPort;
    @Value("${spring.redis.matchServer.password}")
    private String matchPassword;
    @Value("${spring.redis.gameServer.host}")
    private String gameHost;
    @Value("${spring.redis.gameServer.port}")
    private int gamePort;
    @Value("${spring.redis.gameServer.password}")
    private String gamePassword;

    @Bean(destroyMethod = "shutdown")
    public RedissonClient matchRedissonClient() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://" + matchHost + ":" + matchPort)
                .setPassword(matchPassword);
        return Redisson.create(config);
    }

    @Bean(destroyMethod = "shutdown")
    public RedissonClient gameRedissonClient() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://" + gameHost + ":" + gamePort)
                .setPassword(gamePassword);
        return Redisson.create(config);
    }
}

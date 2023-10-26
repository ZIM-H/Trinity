package com.trinity.match.domain.redis.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "spring.redis")
public class RedisProperties {
    private Server matchServer;
    private Server gameServer;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Server {
        private String host;
        private int port;
        private String password;
    }
}

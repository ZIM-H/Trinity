package com.trinity.match.global.webClient;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "game-server.url")
public class WebClientProperties {
    private String gameServerUrl;
}

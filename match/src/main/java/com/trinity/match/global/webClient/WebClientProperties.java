package com.trinity.match.global.webClient;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class WebClientProperties {
    @Value("${game-server.gameServerUrl}")
    private String gameServerUrl;
}

package com.trinity.trinity.global.webClient.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class WebClientProperties {
    @Value("${match-server.url}")
    private String matchServerUrl;
}

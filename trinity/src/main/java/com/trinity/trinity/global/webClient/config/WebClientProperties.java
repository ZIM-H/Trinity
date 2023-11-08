package com.trinity.trinity.global.webClient.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class WebClientProperties {
    private String gameServerUrl = "https://k9b308a.p.ssafy.io";
}

package com.trinity.trinity.webClient;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class WebClientProperties {
    private String gameServerUrl = "https://k9b308a.p.ssafy.io";
}

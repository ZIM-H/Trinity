package com.trinity.match.global.webClient;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class WebClientProperties {
    private String gameServerUrl = "https://k9b308.p.ssafy.io/api/game";
}

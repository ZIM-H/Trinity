package com.trinity.trinity.global.webClient.service;

import com.trinity.trinity.global.webClient.config.WebClientConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebClientService {

    private final WebClientConfig webClientConfig;

    public void get(String userId) {
        webClientConfig.webClient()
                .get()
                .uri("/api/match/join/{userId}", userId)
                .retrieve()
                .bodyToMono(String.class) // 반환되는 응답의 타입. 필요에 따라 변경
                .subscribe(
                        response -> log.info(response),
                        error -> log.error(error.getMessage())
                ); // 비동기 처리를 위해 subscribe() 호출;
    }
}

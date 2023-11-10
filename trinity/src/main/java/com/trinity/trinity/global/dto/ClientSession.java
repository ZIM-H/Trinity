package com.trinity.trinity.global.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ClientSession {
    private String userId;
    private String clientId;

    @Builder
    public ClientSession(String userId, String clientId) {
        this.userId = userId;
        this.clientId = clientId;
    }
}

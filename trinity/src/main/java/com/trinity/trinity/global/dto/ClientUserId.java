package com.trinity.trinity.global.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ClientUserId {
    private String clientId;
    private String userId;

    @Builder
    public ClientUserId(String clientId, String userId) {
        this.clientId = clientId;
        this.userId = userId;
    }
}

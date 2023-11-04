package com.trinity.trinity.client;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClientSession {

    private String userId;
    private String clientId;
    private String clientAddress;

    @Builder
    public ClientSession(String userId, String clientId, String clientAddress) {
        this.userId = userId;
        this.clientId = clientId;
        this.clientAddress = clientAddress;
    }
}

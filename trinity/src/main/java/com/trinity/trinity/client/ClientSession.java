package com.trinity.trinity.client;

import lombok.Getter;

@Getter
public class ClientSession {

    private String userId;
    private String clientId;
    private String clientAddress;
    public ClientSession(String userId, String clientId, String clientAddress) {
        this.userId = userId;
        this.clientId = clientId;
        this.clientAddress = clientAddress;
    }
}

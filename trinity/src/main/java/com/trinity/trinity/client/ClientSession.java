package com.trinity.trinity.client;

import lombok.Getter;

@Getter
public class ClientSession {

    private String clientId;
    private String userId;
    private String clientAddress;
    public ClientSession(String clientId, String userId, String clientAddress) {
        this.clientId = clientId;
        this.userId = userId;
        this.clientAddress = clientAddress;
    }
}

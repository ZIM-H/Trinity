package com.trinity.trinity.client;

public class ClientSession {

    private String clientId;
    public ClientSession(String clientId) {
        this.clientId = clientId;
    }

    public String getClientId() {
        return clientId;
    }
}

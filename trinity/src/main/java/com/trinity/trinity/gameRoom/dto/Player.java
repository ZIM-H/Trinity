package com.trinity.trinity.gameRoom.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Player {
    private String userId;
    private String gameRoomId;
    private int playerStatus;
    private int taurineAmount;

    @Builder
    public Player(String userId, String gameRoomId, int playerStatus, int taurineAmount) {
        this.userId = userId;
        this.gameRoomId = gameRoomId;
        this.playerStatus = playerStatus;
        this.taurineAmount = taurineAmount;
    }
}

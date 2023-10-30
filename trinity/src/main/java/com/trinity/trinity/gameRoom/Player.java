package com.trinity.trinity.gameRoom;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Player {
    private int roomNo;
    private boolean playerStatus;
    private int taurineAmount;

    @Builder
    public Player(int roomNo, boolean playerStatus, int taurineAmount) {
        this.roomNo = roomNo;
        this.playerStatus = playerStatus;
        this.taurineAmount = taurineAmount;
    }
}

package com.trinity.trinity.gameRoom.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SecondRoom {
    private int fertilizerAmount;
    private String player;
    private String message;
    private int carbonCaptureStatus;
    private int farmStatus;

    @Builder
    public SecondRoom(int fertilizerAmount, String player, String message, int carbonCaptureStatus, int farmStatus) {
        this.fertilizerAmount = fertilizerAmount;
        this.player = player;
        this.message = message;
        this.carbonCaptureStatus = carbonCaptureStatus;
        this.farmStatus = farmStatus;
    }
}

package com.trinity.trinity.gameRoom.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SecondRoom {
    private int fertilizerAmount;
    private String player;
    private String message;
    private int carbonCaptureStatus;
    private int carbonCaptureTryCount;
    private boolean carbonCaptureTry;
    private boolean farmStatus;
    private boolean farmTry;
    private boolean taurineFilterStatus;
    private boolean taurineFilterTry;
    private boolean fertilizerTry;

    @Builder
    public SecondRoom(int fertilizerAmount, String player, String message, int carbonCaptureStatus, int carbonCaptureTryCount, boolean carbonCaptureTry, boolean farmStatus, boolean farmTry, boolean taurineFilterStatus, boolean taurineFilterTry, boolean fertilizerTry) {
        this.fertilizerAmount = fertilizerAmount;
        this.player = player;
        this.message = message;
        this.carbonCaptureStatus = carbonCaptureStatus;
        this.carbonCaptureTryCount = carbonCaptureTryCount;
        this.carbonCaptureTry = carbonCaptureTry;
        this.farmStatus = farmStatus;
        this.farmTry = farmTry;
        this.taurineFilterStatus = taurineFilterStatus;
        this.taurineFilterTry = taurineFilterTry;
        this.fertilizerTry = fertilizerTry;
    }
}

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
    private int farmStatus;
    private int inputFertilizer;

    @Builder
    public SecondRoom(int fertilizerAmount, String player, String message, int carbonCaptureStatus, int farmStatus, int inputFertilizer) {
        this.fertilizerAmount = fertilizerAmount;
        this.player = player;
        this.message = message;
        this.carbonCaptureStatus = carbonCaptureStatus;
        this.farmStatus = farmStatus;
        this.inputFertilizer = inputFertilizer;
    }
}

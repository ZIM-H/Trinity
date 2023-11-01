package com.trinity.trinity.gameRoom.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FirstRoom {
    private int fertilizerAmount;
    private String player;
    private String message;
    private int fertilizerUpgradeStatus;
    private int purifierStatus;
    private int inputFertilizer;

    @Builder
    public FirstRoom(int fertilizerAmount, String player, String message, int fertilizerUpgradeStatus, int purifierStatus, int inputFertilizer) {
        this.fertilizerAmount = fertilizerAmount;
        this.player = player;
        this.message = message;
        this.fertilizerUpgradeStatus = fertilizerUpgradeStatus;
        this.purifierStatus = purifierStatus;
        this.inputFertilizer = inputFertilizer;
    }
}

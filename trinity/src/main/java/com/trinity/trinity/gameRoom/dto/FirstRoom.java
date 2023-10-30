package com.trinity.trinity.gameRoom.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FirstRoom {
    private int fertilizerAmount;
    private String player;
    private String message;
    private int fertilizerUpgradeStatus;
    private int purifierStatus;

    @Builder
    public FirstRoom(int fertilizerAmount, String player, String message, int fertilizerUpgradeStatus, int purifierStatus) {
        this.fertilizerAmount = fertilizerAmount;
        this.player = player;
        this.message = message;
        this.fertilizerUpgradeStatus = fertilizerUpgradeStatus;
        this.purifierStatus = purifierStatus;
    }
}

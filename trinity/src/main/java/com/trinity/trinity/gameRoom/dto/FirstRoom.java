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
    private boolean fertilizerUpgradeTry;
    private int purifierStatus;
    private boolean purifierTry;
    private int purifierTryCount;
    private boolean fertilizerTry;

    @Builder

    public FirstRoom(int fertilizerAmount, String player, String message, int fertilizerUpgradeStatus, boolean fertilizerUpgradeTry, int purifierStatus, boolean purifierTry, int purifierTryCount, boolean fertilizerTry) {
        this.fertilizerAmount = fertilizerAmount;
        this.player = player;
        this.message = message;
        this.fertilizerUpgradeStatus = fertilizerUpgradeStatus;
        this.fertilizerUpgradeTry = fertilizerUpgradeTry;
        this.purifierStatus = purifierStatus;
        this.purifierTry = purifierTry;
        this.purifierTryCount = purifierTryCount;
        this.fertilizerTry = fertilizerTry;
    }
}

package com.trinity.trinity.gameRoom.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
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

    private boolean inputFertilizerTry;
    private boolean makeFertilizerTry;

    @Builder
    public FirstRoom(int fertilizerAmount, String player, String message, int fertilizerUpgradeStatus, boolean fertilizerUpgradeTry, int purifierStatus, boolean purifierTry, int purifierTryCount, boolean inputFertilizerTry, boolean makeFertilizerTry) {
     
        this.fertilizerAmount = fertilizerAmount;
        this.player = player;
        this.message = message;
        this.fertilizerUpgradeStatus = fertilizerUpgradeStatus;
        this.fertilizerUpgradeTry = fertilizerUpgradeTry;
        this.purifierStatus = purifierStatus;
        this.purifierTry = purifierTry;
        this.purifierTryCount = purifierTryCount;

        this.inputFertilizerTry = inputFertilizerTry;
        this.makeFertilizerTry = makeFertilizerTry;

    }
}

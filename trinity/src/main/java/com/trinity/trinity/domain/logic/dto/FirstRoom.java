package com.trinity.trinity.domain.logic.dto;

import com.trinity.trinity.domain.control.dto.request.FirstRoomPlayerRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FirstRoom {
    private int fertilizerAmount;
    private String userId;
    private String message;
    private int fertilizerUpgradeStatus;
    private boolean fertilizerUpgradeTry;
    private int purifierStatus;
    private boolean purifierTry;
    private boolean inputFertilizerTry;
    private boolean makeFertilizerTry;

    @Builder
    public FirstRoom(int fertilizerAmount, String userId, String message, int fertilizerUpgradeStatus, boolean fertilizerUpgradeTry, int purifierStatus, boolean purifierTry, boolean inputFertilizerTry, boolean makeFertilizerTry) {
        this.fertilizerAmount = fertilizerAmount;
        this.userId = userId;
        this.message = message;
        this.fertilizerUpgradeStatus = fertilizerUpgradeStatus;
        this.fertilizerUpgradeTry = fertilizerUpgradeTry;
        this.purifierStatus = purifierStatus;
        this.purifierTry = purifierTry;
        this.inputFertilizerTry = inputFertilizerTry;
        this.makeFertilizerTry = makeFertilizerTry;
    }

    public static FirstRoom toDto(FirstRoomPlayerRequestDto dto) {
        return FirstRoom.builder()
                .userId(dto.getUserId())
                .message(dto.getMessage())
                .inputFertilizerTry(dto.isInputFertilizerTry())
                .makeFertilizerTry(dto.isMakeFertilizerTry())
                .fertilizerUpgradeTry(dto.isFertilizerUpgradeTry())
                .purifierTry(dto.isPurifierTry())
                .build();
    }

    public void modifyDto(FirstRoom oldRoom) {
        this.fertilizerAmount = oldRoom.getFertilizerAmount();
        this.fertilizerUpgradeStatus = oldRoom.getFertilizerUpgradeStatus();
        this.purifierStatus = oldRoom.getPurifierStatus();
    }

    public void modifyPurifierStatus(int purifierStatus) {
        this.purifierStatus = purifierStatus;
    }

    public void modifyPurifierTry(boolean purifierTry) {
        this.purifierTry = purifierTry;
    }

    public void modifyFertilizerUpgradeTry(boolean fertilizerUpgradeTry) {
        this.fertilizerUpgradeTry = fertilizerUpgradeTry;
    }

    public void modifyFertilizerUpgradeStatus(int fertilizerUpgradeStatus) {
        this.fertilizerUpgradeStatus = fertilizerUpgradeStatus;
    }

    public void modifyUserId(String userId) {
        this.userId = userId;
    }

    public void modifyMakeFertilizerTry(boolean makeFertilizerTry) {
        this.makeFertilizerTry = makeFertilizerTry;
    }

    public void modifyFertilizerAmount(int fertilizerAmount) {
        this.fertilizerAmount = fertilizerAmount;
    }

    public void modifyInputFertilizerTry(boolean inputFertilizerTry) {
        this.inputFertilizerTry = inputFertilizerTry;
    }
}
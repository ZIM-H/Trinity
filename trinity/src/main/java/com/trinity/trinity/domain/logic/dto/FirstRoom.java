package com.trinity.trinity.domain.logic.dto;

import com.trinity.trinity.domain.control.dto.request.FirstRoomPlayerRequestDto;
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
    private boolean inputFertilizerTry;
    private boolean makeFertilizerTry;

    @Builder
    public FirstRoom(int fertilizerAmount, String player, String message, int fertilizerUpgradeStatus, boolean fertilizerUpgradeTry, int purifierStatus, boolean purifierTry, boolean inputFertilizerTry, boolean makeFertilizerTry) {
        this.fertilizerAmount = fertilizerAmount;
        this.player = player;
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
                .player(dto.getUserId())
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
}
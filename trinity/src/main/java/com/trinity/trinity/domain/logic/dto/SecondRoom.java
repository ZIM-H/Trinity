package com.trinity.trinity.domain.logic.dto;

import com.trinity.trinity.domain.control.dto.request.SecondRoomPlayerRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SecondRoom {
    private int fertilizerAmount;
    private String userId;
    private String message;
    private int carbonCaptureStatus;
    private int carbonCaptureTryCount;
    private boolean carbonCaptureTry;
    private boolean farmStatus;
    private boolean farmTry;
    private boolean taurineFilterStatus;
    private boolean taurineFilterTry;
    private boolean inputFertilizerTry;
    private boolean makeFertilizerTry;

    @Builder
    public SecondRoom(int fertilizerAmount, String userId, String message, int carbonCaptureStatus, int carbonCaptureTryCount, boolean carbonCaptureTry, boolean farmStatus, boolean farmTry, boolean taurineFilterStatus, boolean taurineFilterTry, boolean inputFertilizerTry, boolean makeFertilizerTry) {
        this.fertilizerAmount = fertilizerAmount;
        this.userId = userId;
        this.message = message;
        this.carbonCaptureStatus = carbonCaptureStatus;
        this.carbonCaptureTryCount = carbonCaptureTryCount;
        this.carbonCaptureTry = carbonCaptureTry;
        this.farmStatus = farmStatus;
        this.farmTry = farmTry;
        this.taurineFilterStatus = taurineFilterStatus;
        this.taurineFilterTry = taurineFilterTry;
        this.inputFertilizerTry = inputFertilizerTry;
        this.makeFertilizerTry = makeFertilizerTry;
    }

    public static SecondRoom toDto(SecondRoomPlayerRequestDto dto) {
        return SecondRoom.builder()
                .userId(dto.getUserId())
                .message(dto.getMessage())
                .inputFertilizerTry(dto.isInputFertilizerTry())
                .makeFertilizerTry(dto.isMakeFertilizerTry())
                .carbonCaptureTry(dto.isCarbonCaptureTry())
                .farmTry(dto.isFarmTry())
                .taurineFilterTry(dto.isTaurineFilterTry())
                .build();
    }

    public void modifyDto(SecondRoom oldRoom) {
        this.fertilizerAmount = oldRoom.getFertilizerAmount();
        this.carbonCaptureStatus = oldRoom.getCarbonCaptureStatus();
        this.carbonCaptureTryCount = oldRoom.getCarbonCaptureTryCount();
        this.taurineFilterStatus = oldRoom.isTaurineFilterStatus();
        this.farmStatus = oldRoom.isFarmStatus();
    }

    public void modifyFarmStatus(boolean farmStatus) {
        this.farmStatus = farmStatus;
    }

    public void modifyFarmTry(boolean farmTry) {
        this.farmTry = farmTry;
    }

    public void modifyTaurineFilterStatus(boolean taurineFilterStatus) {
        this.taurineFilterStatus = taurineFilterStatus;
    }
    public void modifyTaurineFilterTry(boolean taurineFilterTry) {
        this.taurineFilterTry = taurineFilterTry;
    }

    public void modifyCarbonCaptureStatus(int carbonCaptureStatus) {
        this.carbonCaptureStatus = carbonCaptureStatus;
    }

    public void modifyCarbonCaptureTry(boolean carbonCaptureTry) {
        this.carbonCaptureTry = carbonCaptureTry;
    }

    public void modifyCarbonCaptureTryCount(int carbonCaptureTryCount) {
        this.carbonCaptureTryCount = carbonCaptureTryCount;
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

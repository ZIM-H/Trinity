package com.trinity.trinity.domain.control.dto.response;

import com.trinity.trinity.domain.logic.dto.GameRoom;
import lombok.Builder;

public class SecondRoomResponseDto {
    private String type;
    private int fertilizerAmount;
    private int eventCode;
    private int foodAmount;
    private boolean fertilizerUpgrade;
    private boolean barrierUpgrade;
    private boolean conflictAsteroid;
    private String gameRoomId;
    private SecondResponseDto secondResponseDto;

    @Builder
    public SecondRoomResponseDto(String type, int fertilizerAmount, int eventCode, int foodAmount, boolean fertilizerUpgrade, boolean barrierUpgrade, boolean conflictAsteroid, String gameRoomId) {
        this.type = type;
        this.fertilizerAmount = fertilizerAmount;
        this.eventCode = eventCode;
        this.foodAmount = foodAmount;
        this.fertilizerUpgrade = fertilizerUpgrade;
        this.barrierUpgrade = barrierUpgrade;
        this.conflictAsteroid = conflictAsteroid;
        this.gameRoomId = gameRoomId;
    }

    public void modifySecondRoomResponseDto(CommonDataDto commonDataDto, GameRoom gameRoom) {
        this.fertilizerAmount = gameRoom.getFertilizerAmount();
        this.eventCode = gameRoom.getEvent();
        this.foodAmount = gameRoom.getFoodAmount();
        this.fertilizerUpgrade = commonDataDto.isFertilizerUpgrade();
        this.barrierUpgrade = commonDataDto.isBarrierUpgrade();
        this.conflictAsteroid = commonDataDto.isConflictAsteroid();
        this.gameRoomId = gameRoom.getGameRoomId();
        this.secondResponseDto = SecondResponseDto.builder()
                .message(gameRoom.getSecondRoom().getMessage())
                .fertilizerAmount(gameRoom.getSecondRoom().getFertilizerAmount())
                .carbonCaptureTryCount(gameRoom.getSecondRoom().getCarbonCaptureTryCount())
                .carbonCaptureStatus(gameRoom.getSecondRoom().getCarbonCaptureStatus())
                .farmStatus(gameRoom.getSecondRoom().isFarmStatus())
                .build();
    }

    @Builder
    private static class SecondResponseDto {
        String message;
        int fertilizerAmount;
        int carbonCaptureTryCount;
        int carbonCaptureStatus;
        boolean farmStatus;
    }
}

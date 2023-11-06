package com.trinity.trinity.DTO.response;

import com.trinity.trinity.gameRoom.dto.GameRoom;
import lombok.Builder;

public class SecondRoomResponseDto {
    private String type;
    private int fertilizerAmount;
    private int eventCode;
    private boolean fertilizerUpgrade;
    private boolean barrierUpgrade;
    private boolean conflictAsteroid;
    private String gameRoomId;
    private SecondResponseDto secondResponseDto;

    @Builder
    public SecondRoomResponseDto(String type, int fertilizerAmount, int eventCode, boolean fertilizerUpgrade, boolean barrierUpgrade, boolean conflictAsteroid, String gameRoomId) {
        this.type = type;
        this.fertilizerAmount = fertilizerAmount;
        this.eventCode = eventCode;
        this.fertilizerUpgrade = fertilizerUpgrade;
        this.barrierUpgrade = barrierUpgrade;
        this.conflictAsteroid = conflictAsteroid;
        this.gameRoomId = gameRoomId;
    }

    public void modifySecondRoomResponseDto(CommonDataDto commonDataDto, GameRoom gameRoom) {
        this.fertilizerAmount = gameRoom.getFertilizerAmount();
        this.eventCode = gameRoom.getEvent();
        this.fertilizerUpgrade = commonDataDto.isFertilizerUpgrade();
        this.barrierUpgrade = commonDataDto.isBarrierUpgrade();
        this.conflictAsteroid = commonDataDto.isConflictAsteroid();
        this.gameRoomId = gameRoom.getGameRoomId();
        this.secondResponseDto = SecondResponseDto.builder()
                .message(gameRoom.getRound().getSecondRoom().getMessage())
                .fertilizerAmount(gameRoom.getRound().getSecondRoom().getFertilizerAmount())
                .carbonCaptureTryCount(gameRoom.getRound().getSecondRoom().getCarbonCaptureTryCount())
                .farmStatus(gameRoom.getRound().getSecondRoom().isFarmStatus())
                .build();
    }

    @Builder
    private class SecondResponseDto {
        String message;
        int fertilizerAmount;
        int carbonCaptureTryCount;
        boolean farmStatus;
    }
}

package com.trinity.trinity.DTO.response;

import com.trinity.trinity.gameRoom.dto.GameRoom;
import lombok.Builder;

public class ThirdRoomResponseDto {

    private String type;
    private int fertilizerAmount;
    private int eventCode;
    private int foodAmount;
    private boolean fertilizerUpgrade;
    private boolean barrierUpgrade;
    private boolean conflictAsteroid;
    private String gameRoomId;
    private ThirdResponseDto thirdResponseDto;

    @Builder
    public ThirdRoomResponseDto(String type, int fertilizerAmount, int eventCode, int foodAmount, boolean fertilizerUpgrade, boolean barrierUpgrade, boolean conflictAsteroid, String gameRoomId, ThirdResponseDto thirdResponseDto) {
        this.type = type;
        this.fertilizerAmount = fertilizerAmount;
        this.eventCode = eventCode;
        this.foodAmount = foodAmount;
        this.fertilizerUpgrade = fertilizerUpgrade;
        this.barrierUpgrade = barrierUpgrade;
        this.conflictAsteroid = conflictAsteroid;
        this.gameRoomId = gameRoomId;
        this.thirdResponseDto = thirdResponseDto;
    }

    public void modifyThirdRoomResponseDto(CommonDataDto commonDataDto, GameRoom gameRoom) {
        this.fertilizerAmount = gameRoom.getFertilizerAmount();
        this.eventCode = gameRoom.getEvent();
        this.foodAmount = gameRoom.getFoodAmount();
        this.fertilizerUpgrade = commonDataDto.isFertilizerUpgrade();
        this.barrierUpgrade = commonDataDto.isBarrierUpgrade();
        this.conflictAsteroid = commonDataDto.isConflictAsteroid();
        this.gameRoomId = gameRoom.getGameRoomId();
        this.thirdResponseDto = ThirdResponseDto.builder()
                .message(gameRoom.getRound().getThirdRoom().getMessage())
                .fertilizerAmount(gameRoom.getRound().getThirdRoom().getFertilizerAmount())
                .build();
    }

    @Builder
    private static class ThirdResponseDto{
        String message;
        int fertilizerAmount;
    }
}

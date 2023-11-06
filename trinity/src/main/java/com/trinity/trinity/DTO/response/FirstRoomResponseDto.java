package com.trinity.trinity.DTO.response;

import com.trinity.trinity.gameRoom.dto.GameRoom;
import lombok.Builder;

public class FirstRoomResponseDto {

    private String type;
    private int fertilizerAmount;
    private int eventCode;
    private boolean fertilizerUpgrade;
    private boolean barrierUpgrade;
    private boolean conflictAsteroid;
    private String gameRoomId;
    private FirstResponseDto firstResponseDto;

    @Builder
    public FirstRoomResponseDto(String type, int fertilizerAmount, int event, boolean fertilizerUpgrade, boolean barrierUpgrade, boolean conflictAsteroid, String gameRoomId, FirstResponseDto firstResponseDto) {
        this.type = type;
        this.fertilizerAmount = fertilizerAmount;
        this.eventCode = event;
        this.fertilizerUpgrade = fertilizerUpgrade;
        this.barrierUpgrade = barrierUpgrade;
        this.conflictAsteroid = conflictAsteroid;
        this.gameRoomId = gameRoomId;
        this.firstResponseDto = firstResponseDto;
    }

    public void modifyFirstRoomResponseDto(CommonDataDto commonDataDto, GameRoom gameRoom) {
        this.fertilizerAmount = gameRoom.getFertilizerAmount();
        this.eventCode = gameRoom.getEvent();
        this.fertilizerUpgrade = commonDataDto.isFertilizerUpgrade();
        this.barrierUpgrade = commonDataDto.isBarrierUpgrade();
        this.conflictAsteroid = commonDataDto.isConflictAsteroid();
        this.gameRoomId = gameRoom.getGameRoomId();

        boolean purifierStatus = true;
        if(gameRoom.getRound().getFirstRoom().getPurifierStatus() != 0) {
            purifierStatus = false;
        }

        this.firstResponseDto = FirstResponseDto.builder()
                .fertilizerAmount(gameRoom.getRound().getFirstRoom().getFertilizerAmount())
                .message(gameRoom.getRound().getFirstRoom().getMessage())
                .purifierStatus(purifierStatus)
                .build();
    }

    @Builder
    private static class FirstResponseDto{
        String message;
        int fertilizerAmount;
        boolean purifierStatus;
    }
}

package com.trinity.trinity.gameRoom.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GameRoom {
    private String gameRoomId;
    private int foodAmount;
    private boolean foodOverCheck;
    private int farmFertilizerAmount;
    private int roundNo;
    private Round round;

    @Builder
    public GameRoom(String gameRoomId, int foodAmount, boolean foodOverCheck, int farmFertilizerAmount, int roundNo, Round round) {
        this.gameRoomId = gameRoomId;
        this.foodAmount = foodAmount;
        this.foodOverCheck = foodOverCheck;
        this.farmFertilizerAmount = farmFertilizerAmount;
        this.roundNo = roundNo;
        this.round = round;
    }
}

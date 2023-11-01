package com.trinity.trinity.gameRoom.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GameRoom {
    private String gameRoomId;
    private int foodAmount;
    private int fertilizerAmount;
    private int roundNo;
    private Round round;

    @Builder
    public GameRoom(String gameRoomId, int foodAmount, int fertilizerAmount, int roundNo, Round round) {
        this.gameRoomId = gameRoomId;
        this.foodAmount = foodAmount;
        this.fertilizerAmount = fertilizerAmount;
        this.roundNo = roundNo;
        this.round = round;
    }
}

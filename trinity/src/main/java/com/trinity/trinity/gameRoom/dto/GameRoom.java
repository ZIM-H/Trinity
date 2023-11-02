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
    private boolean playerStatus;
    private boolean birthday;

    @Builder
    public GameRoom(String gameRoomId, int foodAmount, int fertilizerAmount, int roundNo, Round round, boolean playerStatus, boolean birthday) {
        this.gameRoomId = gameRoomId;
        this.foodAmount = foodAmount;
        this.fertilizerAmount = fertilizerAmount;
        this.roundNo = roundNo;
        this.round = round;
        this.playerStatus = playerStatus;
        this.birthday = birthday;
    }



    public void modifyGameRoomId(String gameRoomId) {
        this.gameRoomId = gameRoomId;
    }

    public void modifyRound(Round round) {
        this.round = round;
    }
}

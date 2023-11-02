package com.trinity.trinity.gameRoom.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GameRoom {
    private String gameRoomId;
    private int foodAmount;
    private int fertilizerAmount;
    private int roundNo;
    private Round round;
    private boolean playerStatus;
    private boolean birthday;
    private int event;

    @Builder
    public GameRoom(String gameRoomId, int foodAmount, int fertilizerAmount, int roundNo, Round round, boolean playerStatus, boolean birthday, int event) {
        this.gameRoomId = gameRoomId;
        this.foodAmount = foodAmount;
        this.fertilizerAmount = fertilizerAmount;
        this.roundNo = roundNo;
        this.round = round;
        this.playerStatus = playerStatus;
        this.birthday = birthday;
        this.event = event;
    }
}

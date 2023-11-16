package com.trinity.trinity.domain.logic.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
public class GameRoomCheck {
    private                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     boolean firstRoom;
    private volatile boolean secondRoom;
    private volatile boolean thirdRoom;

    @Builder
    public GameRoomCheck() {
        this.firstRoom = false;
        this.secondRoom = false;
        this.thirdRoom = false;
    }

    public boolean checkRoom(String roomNum) {
        if (roomNum.equals("first")) {
            firstRoom = true;
        } else if (roomNum.equals("second")) {
            secondRoom = true;
        } else {
            thirdRoom = true;
        }

        return firstRoom && secondRoom && thirdRoom;
    }
}

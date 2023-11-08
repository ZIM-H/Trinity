package com.trinity.trinity.domain.logic.dto;


import lombok.Builder;

public class GameRoomCheck {
    private boolean firstRoom;
    private boolean secondRoom;
    private boolean thirdRoom;

    @Builder
    public GameRoomCheck() {
        this.firstRoom = false;
        this.secondRoom = false;
        this.thirdRoom = false;
    }

    public boolean checkRoom(String roomNum) {
        if (roomNum.equals(firstRoom)) {
            firstRoom = true;
        } else if (roomNum.equals(secondRoom)) {
            secondRoom = true;
        } else {
            thirdRoom = true;
        }

        if (firstRoom && secondRoom && thirdRoom) return true;
        else return false;
    }
}

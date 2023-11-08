package com.trinity.trinity.domain.logic.dto;

public class GameRoomCheck {
    private boolean firstRoom;
    private boolean secondRoom;
    private boolean thirdRoom;

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

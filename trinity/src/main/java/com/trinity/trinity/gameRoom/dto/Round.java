package com.trinity.trinity.gameRoom.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Round {
    private FirstRoom firstRoom;
    private SecondRoom secondRoom;
    private ThirdRoom thirdRoom;

    @Builder
    public Round(FirstRoom firstRoom, SecondRoom secondRoom, ThirdRoom thirdRoom) {
        this.firstRoom = firstRoom;
        this.secondRoom = secondRoom;
        this.thirdRoom = thirdRoom;
    }

    public void modifyFirstRoom(FirstRoom firstRoom) {
        this.firstRoom = firstRoom;
    }

    public void modifySecondRoom(SecondRoom secondRoom) {
        this.secondRoom = secondRoom;
    }

    public void modifyThirdRoom(ThirdRoom thirdRoom) {
        this.thirdRoom = thirdRoom;
    }
}

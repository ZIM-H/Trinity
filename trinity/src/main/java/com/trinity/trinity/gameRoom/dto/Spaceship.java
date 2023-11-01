package com.trinity.trinity.gameRoom.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Spaceship {

    private FirstRoom firstRoom;
    private SecondRoom secondRoom;
    private ThirdRoom thirdRoom;

    @Builder
    public Spaceship(FirstRoom firstRoom, SecondRoom secondRoom, ThirdRoom thirdRoom) {
        this.firstRoom = firstRoom;
        this.secondRoom = secondRoom;
        this.thirdRoom = thirdRoom;
    }
}

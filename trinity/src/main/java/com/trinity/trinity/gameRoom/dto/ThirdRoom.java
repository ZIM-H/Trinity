package com.trinity.trinity.gameRoom.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ThirdRoom {
    private int fertilizerAmount;
    private String player;
    private String message;
    private int asteroidStatus;
    private int blackholeStatus;
    private int barrierStatus;
    private String developer;
    private int inputFertilizer;

    @Builder
    public ThirdRoom(int fertilizerAmount, String player, String message, int asteroidStatus, int blackholeStatus, int barrierStatus, String developer, int inputFertilizer) {
        this.fertilizerAmount = fertilizerAmount;
        this.player = player;
        this.message = message;
        this.asteroidStatus = asteroidStatus;
        this.blackholeStatus = blackholeStatus;
        this.barrierStatus = barrierStatus;
        this.developer = developer;
        this.inputFertilizer = inputFertilizer;
    }
}

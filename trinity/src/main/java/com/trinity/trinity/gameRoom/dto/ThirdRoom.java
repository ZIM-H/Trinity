package com.trinity.trinity.gameRoom.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ThirdRoom {
    private int fertilizerAmount;
    private String player;
    private String message;
    private int asteroidStatus;
    private int blackholeStatus;
    private int barrierStatus;
    private String developer;

    @Builder
    public ThirdRoom(int fertilizerAmount, String player, String message, int asteroidStatus, int blackholeStatus, int barrierStatus, String developer) {
        this.fertilizerAmount = fertilizerAmount;
        this.player = player;
        this.message = message;
        this.asteroidStatus = asteroidStatus;
        this.blackholeStatus = blackholeStatus;
        this.barrierStatus = barrierStatus;
        this.developer = developer;
    }
}

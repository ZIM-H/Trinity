package com.trinity.trinity.gameRoom.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ThirdRoom {
    private int fertilizerAmount;
    private String player;
    private String message;
    private boolean asteroidStatus;
    private int blackholeStatus;
    private int barrierStatus;
    private boolean barrierDevTry;
    private String developer;
    private boolean fertilizerTry;

    @Builder
    public ThirdRoom(int fertilizerAmount, String player, String message, boolean asteroidStatus, int blackholeStatus, int barrierStatus, boolean barrierDevTry, String developer, boolean fertilizerTry) {
        this.fertilizerAmount = fertilizerAmount;
        this.player = player;
        this.message = message;
        this.asteroidStatus = asteroidStatus;
        this.blackholeStatus = blackholeStatus;
        this.barrierStatus = barrierStatus;
        this.barrierDevTry = barrierDevTry;
        this.developer = developer;
        this.fertilizerTry = fertilizerTry;
    }
}

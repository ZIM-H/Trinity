package com.trinity.trinity.gameRoom;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Round {
    private Spaceship spaceship;
    private Player player1;
    private Player player2;
    private Player player3;

    @Builder
    public Round(Spaceship spaceship, Player player1, Player player2, Player player3) {
        this.spaceship = spaceship;
        this.player1 = player1;
        this.player2 = player2;
        this.player3 = player3;
    }
}

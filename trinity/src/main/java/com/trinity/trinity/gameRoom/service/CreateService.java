package com.trinity.trinity.gameRoom.service;

import com.trinity.trinity.gameRoom.dto.*;
import org.springframework.stereotype.Service;

@Service
public class CreateService {
    public Round createRound(Spaceship spaceship, Player player1, Player player2, Player player3) {
        return Round.builder()
                .spaceship(spaceship)
                .player1(player1)
                .player2(player2)
                .player3(player3)
                .build();
    }

    public Spaceship createSpaceship(FirstRoom firstRoom, SecondRoom secondRoom, ThirdRoom thirdRoom) {
        return Spaceship.builder()
                .firstRoom(firstRoom)
                .secondRoom(secondRoom)
                .thirdRoom(thirdRoom)
                .build();
    }

    public ThirdRoom createThirdRoom(Player player3) {
        return ThirdRoom.builder()
                .fertilizerAmount(3)
                .player(player3.getUserId())
                .message("")
                .asteroidStatus(0)
                .blackholeStatus(0)
                .barrierStatus(0)
                .developer("")
                .inputFertilizer(0)
                .build();
    }

    public SecondRoom createSecondRoom(Player player2) {
        return SecondRoom.builder()
                .fertilizerAmount(3)
                .player(player2.getUserId())
                .message("")
                .carbonCaptureStatus(0)
                .farmStatus(0)
                .inputFertilizer(0)
                .build();
    }

    public FirstRoom createFirstRoom(Player player1) {
        return FirstRoom.builder()
                .fertilizerAmount(3)
                .player(player1.getUserId())
                .message("")
                .fertilizerUpgradeStatus(0)
                .purifierStatus(0)
                .inputFertilizer(0)
                .build();
    }

    public Player createPlayer(String gameRoomId, String userId) {
        return Player.builder()
                .userId(userId)
                .gameRoomId(gameRoomId)
                .playerStatus(0)
                .taurineAmount(0)
                .build();
    }
}

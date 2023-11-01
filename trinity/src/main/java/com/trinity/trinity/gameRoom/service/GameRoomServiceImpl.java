package com.trinity.trinity.gameRoom.service;

import com.trinity.trinity.DTO.request.GameStartPlayerListRequestDto;
import com.trinity.trinity.gameRoom.dto.*;
import com.trinity.trinity.redisUtil.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GameRoomServiceImpl implements GameRoomService {

    private final RedisService redisService;
    private final RedisTemplate<String, Object> gameRoomRedisTemplate;

    @Override
    public String createGameRoom(List<GameStartPlayerListRequestDto> players) {
        String gameRoomId = UUID.randomUUID().toString();
        Player player1 = createPlayer(gameRoomId, players.get(0).getUserId());
        Player player2 = createPlayer(gameRoomId, players.get(1).getUserId());
        Player player3 = createPlayer(gameRoomId, players.get(2).getUserId());

        FirstRoom firstRoom = createFirstRoom(player1);
        SecondRoom secondRoom = createSecondRoom(player2);
        ThirdRoom thirdRoom = createThirdRoom(player3);

        Spaceship spaceship = createSpaceship(firstRoom, secondRoom, thirdRoom);

        Round round = createRound(spaceship, player1, player2, player3);

        GameRoom gameRoom = GameRoom.builder()
                .gameRoomId(gameRoomId)
                .foodAmount(3)
                .fertilizerAmount(0)
                .roundNo(1)
                .round(round)
                .build();

        List<GameRoom> list = new ArrayList<>();
        list.add(gameRoom);

        gameRoomRedisTemplate.opsForHash().put("gameRoom", gameRoomId, list);

        return gameRoomId;
    }

    private Round createRound(Spaceship spaceship, Player player1, Player player2, Player player3) {
        return Round.builder()
                .spaceship(spaceship)
                .player1(player1)
                .player2(player2)
                .player3(player3)
                .build();
    }

    private Spaceship createSpaceship(FirstRoom firstRoom, SecondRoom secondRoom, ThirdRoom thirdRoom) {
        return Spaceship.builder()
                .firstRoom(firstRoom)
                .secondRoom(secondRoom)
                .thirdRoom(thirdRoom)
                .build();
    }

    private ThirdRoom createThirdRoom(Player player3) {
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

    private SecondRoom createSecondRoom(Player player2) {
        return SecondRoom.builder()
                .fertilizerAmount(3)
                .player(player2.getUserId())
                .message("")
                .carbonCaptureStatus(0)
                .farmStatus(0)
                .inputFertilizer(0)
                .build();
    }

    private FirstRoom createFirstRoom(Player player1) {
        return FirstRoom.builder()
                .fertilizerAmount(3)
                .player(player1.getUserId())
                .message("")
                .fertilizerUpgradeStatus(0)
                .purifierStatus(0)
                .inputFertilizer(0)
                .build();
    }

    private Player createPlayer(String gameRoomId, String userId) {
        return Player.builder()
                .userId(userId)
                .gameRoomId(gameRoomId)
                .playerStatus(0)
                .taurineAmount(0)
                .build();
    }
}

package com.trinity.trinity.gameRoom.service;

import com.trinity.trinity.DTO.request.FirstRoomPlayerRequestDto;
import com.trinity.trinity.DTO.request.GameStartPlayerListRequestDto;
import com.trinity.trinity.gameRoom.dto.*;
import com.trinity.trinity.redisUtil.GameRoomRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GameRoomServiceImpl implements GameRoomService {

    private final GameRoomRedisService gameRoomRedisService;
    private final CreateService createService;

    @Override
    public String createGameRoom(List<GameStartPlayerListRequestDto> players) {
        String gameRoomId = UUID.randomUUID().toString();
        Player player1 = createService.createPlayer(gameRoomId, players.get(0).getUserId());
        Player player2 = createService.createPlayer(gameRoomId, players.get(1).getUserId());
        Player player3 = createService.createPlayer(gameRoomId, players.get(2).getUserId());

        FirstRoom firstRoom = createService.createFirstRoom(player1);
        SecondRoom secondRoom = createService.createSecondRoom(player2);
        ThirdRoom thirdRoom = createService.createThirdRoom(player3);

        Spaceship spaceship = createService.createSpaceship(firstRoom, secondRoom, thirdRoom);

        Round round = createService.createRound(spaceship, player1, player2, player3);

        GameRoom gameRoom = GameRoom.builder()
                .gameRoomId(gameRoomId)
                .foodAmount(3)
                .fertilizerAmount(0)
                .roundNo(1)
                .round(round)
                .build();

        gameRoomRedisService.createGameRoom(gameRoom);

        return gameRoomId;
    }

    @Override
    public void updateFirstRoom(FirstRoomPlayerRequestDto firstRoomPlayerRequestDto) {
        // 게임방 가져오고
        GameRoom gameRoom = gameRoomRedisService.getGameRoom(firstRoomPlayerRequestDto.getGameRoomId());
        // 게임방 1번방 정보 넣기
    }
}

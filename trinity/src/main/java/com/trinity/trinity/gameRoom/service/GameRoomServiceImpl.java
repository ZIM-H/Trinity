package com.trinity.trinity.gameRoom.service;

import com.trinity.trinity.DTO.request.FirstRoomPlayerRequestDto;
import com.trinity.trinity.DTO.request.GameStartPlayerListRequestDto;
import com.trinity.trinity.DTO.request.SecondRoomPlayerRequestDto;
import com.trinity.trinity.DTO.request.ThridRoomPlayerRequestDto;
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

        FirstRoom firstRoom = createService.createFirstRoom(players.get(0).getUserId());
        SecondRoom secondRoom = createService.createSecondRoom(players.get(1).getUserId());
        ThirdRoom thirdRoom = createService.createThirdRoom(players.get(1).getUserId());

        Round round = createService.createRound(firstRoom, secondRoom, thirdRoom);

        GameRoom gameRoom = GameRoom.builder()
                .gameRoomId(gameRoomId)
                .foodAmount(3)
                .fertilizerAmount(0)
                .roundNo(1)
                .round(round)
                .playerStatus(false)
                .birthday(false)
                .build();

        gameRoomRedisService.createGameRoom(gameRoom);

        return gameRoomId;
    }

    @Override
    public void updateFirstRoom(FirstRoomPlayerRequestDto firstRoomPlayerRequestDto) {
        // 게임방 가져오고
        GameRoom gameRoom = gameRoomRedisService.getGameRoom(firstRoomPlayerRequestDto.getGameRoomId());
        // 게임방 1번방 정보 넣기
        Round round = gameRoom.getRound();

        if (round == null) {
            gameRoom.modifyRound(new Round());
            round = gameRoom.getRound();
        }

        round.modifyFirstRoom(firstRoomPlayerRequestDto.getFirstRoom());

        gameRoomRedisService.saveGameRoom(gameRoom);
    }

    @Override
    public void updateSecondRoom(SecondRoomPlayerRequestDto secondRoomPlayerRequestDto) {
        GameRoom gameRoom = gameRoomRedisService.getGameRoom(secondRoomPlayerRequestDto.getGameRoomId());

        Round round = gameRoom.getRound();

        if (round == null) {
            gameRoom.modifyRound(new Round());
            round = gameRoom.getRound();
        }

        round.modifySecondRoom(secondRoomPlayerRequestDto.getSecondRoom());

        gameRoomRedisService.saveGameRoom(gameRoom);
    }

    @Override
    public void updateThridRoom(ThridRoomPlayerRequestDto thirdRoomPlayerRequestDto) {
        GameRoom gameRoom = gameRoomRedisService.getGameRoom(thirdRoomPlayerRequestDto.getGameRoomId());

        Round round = gameRoom.getRound();

        if (round == null) {
            gameRoom.modifyRound(new Round());
            round = gameRoom.getRound();
        }

        round.modifyThirdRoom(thirdRoomPlayerRequestDto.getThirdRoom());

        gameRoomRedisService.saveGameRoom(gameRoom);
    }
}

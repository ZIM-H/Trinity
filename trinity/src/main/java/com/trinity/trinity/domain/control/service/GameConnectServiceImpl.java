package com.trinity.trinity.domain.control.service;

import com.google.gson.Gson;
import com.trinity.trinity.domain.control.dto.PlayerDto;
import com.trinity.trinity.domain.control.dto.response.*;
import com.trinity.trinity.domain.logic.dto.GameRoom;
import com.trinity.trinity.domain.logic.service.GameRoomService;
import com.trinity.trinity.domain.control.enums.UserStatus;
import com.trinity.trinity.global.redis.service.RedisService;
import com.trinity.trinity.global.webClient.service.WebClientService;
import com.trinity.trinity.global.webSocket.WebSocketFrameHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GameConnectServiceImpl implements GameConnectService {

    private final RedisService redisService;
    private final WebClientService webClientService;
    private final GameRoomService gameRoomService;
    private final WebSocketFrameHandler webSocketFrameHandler;

    @Override
    public PlayerDto connectToGameServer() {
        String userId = UUID.randomUUID().toString();
        redisService.saveData(userId, String.valueOf(UserStatus.LOBBY));

        return PlayerDto.builder()
                .userId(userId)
                .build();
    }

    @Override
    public void matchMaking(String userId) {
        webClientService.get(userId);
    }

    @Override
    public void createGameRoom(List<PlayerDto> players) {

        GameRoom gameRoom = gameRoomService.createGameRoom(players);
        redisService.saveGameRoomUserStatus(gameRoom.getGameRoomId());

        Gson gson = new Gson();

        CommonDataDto commonDataDto = CommonDataDto.builder()
                .conflictAsteroid(false)
                .barrierUpgrade(false)
                .fertilizerUpgrade(false)
                .build();

        FirstRoomResponseDto firstRoomResponseDto = FirstRoomResponseDto.builder()
                .type("firstDay")
                .build();
        firstRoomResponseDto.modifyFirstRoomResponseDto(commonDataDto, gameRoom);

        SecondRoomResponseDto secondRoomResponseDto = SecondRoomResponseDto.builder()
                .type("firstDay")
                .build();
        secondRoomResponseDto.modifySecondRoomResponseDto(commonDataDto, gameRoom);

        ThirdRoomResponseDto thirdRoomResponseDto = ThirdRoomResponseDto.builder()
                .type("firstDay")
                .build();
        thirdRoomResponseDto.modifyThirdRoomResponseDto(commonDataDto, gameRoom);

        String firstRoom = gson.toJson(firstRoomResponseDto);
        String secondRoom = gson.toJson(secondRoomResponseDto);
        String thirdRoom = gson.toJson(thirdRoomResponseDto);

        String firstUserClientId = redisService.getClientId(gameRoom.getFirstRoom().getPlayer());
        String secondUserClientId = redisService.getClientId(gameRoom.getSecondRoom().getPlayer());
        String thirdUserClientId = redisService.getClientId(gameRoom.getThirdRoom().getPlayer());

        webSocketFrameHandler.sendDataToClient(firstUserClientId, firstRoom);
        webSocketFrameHandler.sendDataToClient(secondUserClientId, secondRoom);
        webSocketFrameHandler.sendDataToClient(thirdUserClientId, thirdRoom);

        redisService.saveData(gameRoom.getFirstRoom().getPlayer(), String.valueOf(UserStatus.PLAYING));
        redisService.saveData(gameRoom.getSecondRoom().getPlayer(), String.valueOf(UserStatus.PLAYING));
        redisService.saveData(gameRoom.getThirdRoom().getPlayer(), String.valueOf(UserStatus.PLAYING));

    }
}

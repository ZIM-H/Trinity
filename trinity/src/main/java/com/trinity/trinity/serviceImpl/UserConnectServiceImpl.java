package com.trinity.trinity.serviceImpl;

import com.google.gson.Gson;
import com.trinity.trinity.DTO.request.GameServerPlayerListRequestDto;
import com.trinity.trinity.DTO.response.*;
import com.trinity.trinity.gameRoom.dto.GameRoom;
import com.trinity.trinity.gameRoom.service.GameRoomService;
import com.trinity.trinity.service.UserConnectService;
import com.trinity.trinity.enums.UserStatus;
import com.trinity.trinity.redisUtil.RedisService;
import com.trinity.trinity.webClient.ChannelManager;
import com.trinity.trinity.webClient.WebClientService;
import com.trinity.trinity.webSocket.WebSocketFrameHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserConnectServiceImpl implements UserConnectService {

    private final RedisService redisService;
    private final WebClientService webClientService;
    private final GameRoomService gameRoomService;
    private final WebSocketFrameHandler webSocketFrameHandler;
    private final ChannelManager channelManager;
    @Override
    public UserConnectResponse connectToGameServer() {
        String userId = UUID.randomUUID().toString();
        redisService.saveData(userId, String.valueOf(UserStatus.LOBBY));

        return UserConnectResponse.builder()
                .userId(userId)
                .userStatus(UserStatus.LOBBY.getStatus())
                .build();
    }

    @Override
    public UserMatchResponse matchMaking(String userId) {
        redisService.saveData(userId, String.valueOf(UserStatus.WAITING));
        webClientService.get(userId);
        return null;
    }

    @Override
    public void createGameRoom(List<GameServerPlayerListRequestDto> players) {

        GameRoom gameRoom = gameRoomService.createGameRoom(players);

        Gson gson = new Gson();

        CommonDataDto commonDataDto = CommonDataDto.builder()
                .conflictAsteroid(false)
                .barrierUpgrade(false)
                .fertilizerUpgrade(false)
                .build();

        FirstRoomResponseDto firstRoomResponseDto = FirstRoomResponseDto.builder().build();
        firstRoomResponseDto.modifyFirstRoomResponseDto(commonDataDto, gameRoom);

        SecondRoomResponseDto secondRoomResponseDto = SecondRoomResponseDto.builder().build();
        secondRoomResponseDto.modifySecondRoomResponseDto(commonDataDto, gameRoom);

        ThirdRoomResponseDto thirdRoomResponseDto = ThirdRoomResponseDto.builder().build();
        thirdRoomResponseDto.modifyThirdRoomResponseDto(commonDataDto, gameRoom);

        String firstRoom = gson.toJson(firstRoomResponseDto);
        String secondRoom = gson.toJson(secondRoomResponseDto);
        String thirdRoom = gson.toJson(thirdRoomResponseDto);

        String firstUserClientId = redisService.getClientId(gameRoom.getRound().getFirstRoom().getPlayer());
        String secondUserClientId = redisService.getClientId(gameRoom.getRound().getSecondRoom().getPlayer());
        String thirdUserClientId = redisService.getClientId(gameRoom.getRound().getThirdRoom().getPlayer());

        webSocketFrameHandler.sendDataToClient(firstUserClientId, firstRoom);
        webSocketFrameHandler.sendDataToClient(secondUserClientId, secondRoom);
        webSocketFrameHandler.sendDataToClient(thirdUserClientId, thirdRoom);

        redisService.saveData(gameRoom.getRound().getFirstRoom().getPlayer(), String.valueOf(UserStatus.PLAYING));
        redisService.saveData(gameRoom.getRound().getSecondRoom().getPlayer(), String.valueOf(UserStatus.PLAYING));
        redisService.saveData(gameRoom.getRound().getThirdRoom().getPlayer(), String.valueOf(UserStatus.PLAYING));

    }
}

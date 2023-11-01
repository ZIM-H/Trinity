package com.trinity.trinity.serviceImpl;

import com.trinity.trinity.DTO.request.GameStartPlayerListRequestDto;
import com.trinity.trinity.DTO.response.UserConnectResponse;
import com.trinity.trinity.DTO.response.UserMatchResponse;
import com.trinity.trinity.service.UserConnectService;
import com.trinity.trinity.enums.UserStatus;
import com.trinity.trinity.redisUtil.RedisService;
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
    private  final WebSocketFrameHandler webSocketFrameHandler;
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
        webClientService.get(userId);
        return null;
    }

    @Override
    public void createGameRoom(List<GameStartPlayerListRequestDto> players) {
        for(GameStartPlayerListRequestDto p : players) {
            webSocketFrameHandler.sendDataToClient(p.getUserId(), ("*hi*" + p.getUserId()));
        }
    }
}

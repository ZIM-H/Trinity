package com.trinity.trinity.service;

import com.trinity.trinity.DTO.request.GameServerPlayerListRequestDto;
import com.trinity.trinity.DTO.response.UserConnectResponse;
import com.trinity.trinity.DTO.response.UserMatchResponse;

import java.util.List;

public interface UserConnectService {
    UserConnectResponse connectToGameServer();

    UserMatchResponse matchMaking(String userId);

    void createGameRoom(List<GameServerPlayerListRequestDto> players);
}

package com.trinity.trinity.service;

import com.trinity.trinity.DTO.request.GameStartPlayerListRequestDto;
import com.trinity.trinity.DTO.response.UserConnectResponse;
import com.trinity.trinity.DTO.response.UserMatchResponse;

import java.util.List;

public interface UserConnectService {
    UserConnectResponse connectToGameServer();

    UserMatchResponse matchMaking(String userId);

    void createGameRoom(List<GameStartPlayerListRequestDto> players);
}

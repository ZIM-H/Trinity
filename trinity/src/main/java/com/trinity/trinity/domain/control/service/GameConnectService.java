package com.trinity.trinity.domain.control.service;

import com.trinity.trinity.domain.control.dto.PlayerDto;

import java.util.List;

public interface GameConnectService {
    PlayerDto connectToGameServer();

    void matchMaking(String userId);

    void createGameRoom(List<PlayerDto> players);
}

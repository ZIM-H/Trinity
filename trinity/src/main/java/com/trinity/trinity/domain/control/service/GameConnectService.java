package com.trinity.trinity.domain.control.service;

import com.trinity.trinity.domain.control.dto.PlayerDto;

import java.util.List;

public interface GameConnectService {
    PlayerDto connectToGameServer();

    void matchMaking(String userId);

    boolean checkUserStatus(List<PlayerDto> players);

//    void checkEnteringQ(String userId, String response)
;
    void createGameRoom(List<PlayerDto> players);
}

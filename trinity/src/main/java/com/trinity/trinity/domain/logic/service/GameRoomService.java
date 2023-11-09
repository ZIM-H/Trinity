package com.trinity.trinity.domain.logic.service;

import com.trinity.trinity.domain.control.dto.PlayerDto;
import com.trinity.trinity.domain.logic.dto.GameRoom;

import java.util.List;

public interface GameRoomService {
    GameRoom createGameRoom(List<PlayerDto> players);

    boolean gameLogic(GameRoom gameRoom);

    void morningGameLogic(GameRoom gameRoom);

    boolean checkEndGame(GameRoom gameRoom);

    void endGame(String gameRoomId);

}

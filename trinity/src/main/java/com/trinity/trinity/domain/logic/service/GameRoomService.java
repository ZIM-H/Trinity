package com.trinity.trinity.domain.logic.service;

import com.trinity.trinity.domain.logic.dto.GameRoom;

public interface GameRoomService {

    boolean gameLogic(GameRoom gameRoom);

    void morningGameLogic(GameRoom gameRoom);

    boolean checkEndGame(GameRoom gameRoom);

    void endGame(String gameRoomId);

}

package com.trinity.trinity.domain.logic.service;

import com.trinity.trinity.domain.control.dto.PlayerDto;
import com.trinity.trinity.domain.control.dto.request.FirstRoomPlayerRequestDto;
import com.trinity.trinity.domain.control.dto.request.SecondRoomPlayerRequestDto;
import com.trinity.trinity.domain.control.dto.request.ThirdRoomPlayerRequestDto;
import com.trinity.trinity.domain.logic.dto.GameRoom;

import java.util.List;

public interface GameRoomService {
    GameRoom createGameRoom(List<PlayerDto> players);

//    void updateFirstRoom(FirstRoomPlayerRequestDto firstRoomPlayerRequestDto);
//
//    void updateSecondRoom(SecondRoomPlayerRequestDto secondRoomPlayerRequestDto);
//
//    void updateThridRoom(ThirdRoomPlayerRequestDto thirdRoomPlayerRequestDto);

    boolean gameLogic(String gameRoomId);

    void morningGameLogic(String gameRoomId);

    boolean checkEndGame(String gameRoomId);

    void endGame(String gameRoomId);

}

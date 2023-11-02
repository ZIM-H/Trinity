package com.trinity.trinity.gameRoom.service;

import com.trinity.trinity.DTO.request.FirstRoomPlayerRequestDto;
import com.trinity.trinity.DTO.request.GameStartPlayerListRequestDto;
import com.trinity.trinity.DTO.request.SecondRoomPlayerRequestDto;
import com.trinity.trinity.DTO.request.ThridRoomPlayerRequestDto;

import java.util.List;

public interface GameRoomService {
    String createGameRoom(List<GameStartPlayerListRequestDto> players);

    void updateFirstRoom(FirstRoomPlayerRequestDto firstRoomPlayerRequestDto);

    void updateSecondRoom(SecondRoomPlayerRequestDto secondRoomPlayerRequestDto);

    void updateThridRoom(ThridRoomPlayerRequestDto thirdRoomPlayerRequestDto);

}

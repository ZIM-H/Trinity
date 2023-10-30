package com.trinity.trinity.gameRoom.service;

import com.trinity.trinity.DTO.request.GameStartPlayerListRequestDto;
import com.trinity.trinity.gameRoom.dto.GameRoom;
import com.trinity.trinity.gameRoom.dto.Player;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GameRoomService {
    GameRoom createGameRoom(List<GameStartPlayerListRequestDto> players);

    Player createPlayer(String gameRoomId, String userId);
}

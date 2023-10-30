package com.trinity.trinity.gameRoom.service;

import com.trinity.trinity.DTO.request.GameStartPlayerListRequestDto;
import com.trinity.trinity.gameRoom.dto.GameRoom;
import com.trinity.trinity.gameRoom.dto.Player;

import java.util.List;
import java.util.UUID;

public class GameRoomServiceImpl implements GameRoomService {

    @Override
    public GameRoom createGameRoom(List<GameStartPlayerListRequestDto> players) {
        String gameRoomId = UUID.randomUUID().toString();
        Player player1 = createPlayer(gameRoomId, players.get(0).getUserId());
        Player player2 = createPlayer(gameRoomId, players.get(1).getUserId());
        Player player3 = createPlayer(gameRoomId, players.get(2).getUserId());

        return GameRoom.builder()
                .build();
    }

    @Override
    public Player createPlayer(String gameRoomId, String userId) {
        return Player.builder()
                .userId(userId)
                .gameRoomId(gameRoomId)
                .playerStatus(0)
                .taurineAmount(0)
                .build();
    }
}

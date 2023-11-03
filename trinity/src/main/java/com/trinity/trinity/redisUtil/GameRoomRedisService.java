package com.trinity.trinity.redisUtil;

import com.trinity.trinity.gameRoom.dto.GameRoom;
import com.trinity.trinity.gameRoom.dto.Round;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GameRoomRedisService {
    private final RedisTemplate<String, Object> gameRoomRedisTemplate;

    public void createGameRoom(GameRoom gameRoom) {
        List<GameRoom> list = new ArrayList<>();
        list.add(gameRoom);

        gameRoomRedisTemplate.opsForHash().put("gameRoom", gameRoom.getGameRoomId(), list);
    }

    public GameRoom getGameRoom(String gameRoomId) {
        // temp game room에 없으면 만들고
        GameRoom findRoom = (GameRoom) gameRoomRedisTemplate.opsForHash().get("temp", gameRoomId);

        if (findRoom == null) {
            List<GameRoom> before = (List<GameRoom>) gameRoomRedisTemplate.opsForHash().get("gameRoom", gameRoomId);

            GameRoom last = before.get(before.size() - 1);
            GameRoom gameRoom = GameRoom.builder()
                    .fertilizerAmount(last.getFertilizerAmount())
                    .foodAmount(last.getFoodAmount())
                    .roundNo(last.getRoundNo() + 1)
                    .build();


            gameRoom.setGameRoomId(gameRoomId);

            gameRoomRedisTemplate.opsForHash().put("temp", gameRoomId, gameRoom);
            return gameRoom;
        }

        return findRoom;
    }


    public void saveGameRoomToTemp(GameRoom gameRoom) {
        gameRoomRedisTemplate.opsForHash().put("temp", gameRoom.getGameRoomId(), gameRoom);
    }

    public void saveGameRoom(GameRoom gameRoom) {
        List<GameRoom> list = (List<GameRoom>) gameRoomRedisTemplate.opsForHash().get("gameRoom", gameRoom.getGameRoomId());
        list.add(gameRoom);
        gameRoomRedisTemplate.opsForHash().put("gameRoom", gameRoom.getGameRoomId(), list);

    }
}

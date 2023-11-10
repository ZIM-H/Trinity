package com.trinity.trinity.global.redis.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.trinity.trinity.domain.control.dto.request.FirstRoomPlayerRequestDto;
import com.trinity.trinity.domain.control.dto.request.SecondRoomPlayerRequestDto;
import com.trinity.trinity.domain.control.dto.request.ThirdRoomPlayerRequestDto;
import com.trinity.trinity.domain.logic.dto.FirstRoom;
import com.trinity.trinity.domain.logic.dto.GameRoom;
import com.trinity.trinity.domain.logic.dto.SecondRoom;
import com.trinity.trinity.domain.logic.dto.ThirdRoom;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GameRoomRedisService {
    private final RedisTemplate<String, Object> gameRoomRedisTemplate;

    private HashOperations<String, String, Object> hashOperations;

    @PostConstruct
    private void init() {
        hashOperations = gameRoomRedisTemplate.opsForHash();
    }

    @Synchronized
    public void createGameRoom(GameRoom gameRoom) {
        List<GameRoom> list = new ArrayList<>();
        list.add(gameRoom);

        hashOperations.put("gameRoom", gameRoom.getGameRoomId(), list);
    }

    @Synchronized
    public GameRoom getGameRoom(String gameRoomId) {
        // temp game room에 없으면 만들고
        GameRoom findRoom = (GameRoom) hashOperations.get("temp", gameRoomId);

        if (findRoom == null) {
            List<GameRoom> before = (List<GameRoom>) hashOperations.get("gameRoom", gameRoomId);

            GameRoom last = before.get(before.size() - 1);

            GameRoom gameRoom = GameRoom.builder()
                    .foodAmount(last.getFoodAmount())
                    .fertilizerAmount(last.getFertilizerAmount())
                    .playerStatus(last.isPlayerStatus())
                    .birthday(last.isBirthday())
                    .event(last.getEvent())
                    .carbonCaptureNotice(last.isCarbonCaptureNotice())
                    .blackholeStatus(last.getBlackholeStatus())
                    .events(last.getEvents())
                    .firstRoom(last.getFirstRoom())
                    .secondRoom(last.getSecondRoom())
                    .thirdRoom(last.getThirdRoom())
                    .build();

            gameRoom.setGameRoomId(gameRoomId);

            hashOperations.put("temp", gameRoomId, gameRoom);
            return gameRoom;
        }
        return findRoom;
    }

    @Synchronized
    public void saveGameRoomToTemp(GameRoom gameRoom) {
        hashOperations.put("temp", gameRoom.getGameRoomId(), gameRoom);
    }

    @Synchronized
    public void saveGameRoom(GameRoom gameRoom) {
        List<GameRoom> list = (List<GameRoom>) gameRoomRedisTemplate.opsForHash().get("gameRoom", gameRoom.getGameRoomId());
        list.add(gameRoom);
        hashOperations.put("gameRoom", gameRoom.getGameRoomId(), list);

    }

    @Synchronized
    public void deleteGameRoom(String gameRoomId) {
        hashOperations.delete("gameRoom", gameRoomId);
    }

    @Synchronized
    public void updateRoom(Gson gson, JsonObject jsonObject, String roomNum, String gameRoomId) {
        GameRoom gameRoom = getGameRoom(gameRoomId);

        // 방번호 확인하는 로직
        if (roomNum.equals("first")) {
            FirstRoomPlayerRequestDto dto = gson.fromJson(jsonObject.get("FirstRoomPlayerRequestDto").getAsJsonObject(), FirstRoomPlayerRequestDto.class);
            FirstRoom firstRoom = FirstRoom.toDto(dto);
            FirstRoom oldRoom = gameRoom.getFirstRoom();
            firstRoom.modifyDto(oldRoom);
            gameRoom.modifyFirstRoom(firstRoom);
        } else if (roomNum.equals("second")) {
            SecondRoomPlayerRequestDto dto = gson.fromJson(jsonObject.get("SecondRoomPlayerRequestDto").getAsJsonObject(), SecondRoomPlayerRequestDto.class);
            SecondRoom secondRoom = SecondRoom.toDto(dto);
            SecondRoom oldRoom = gameRoom.getSecondRoom();
            secondRoom.modifyDto(oldRoom);
            gameRoom.modifySecondRoom(secondRoom);
        } else {
            ThirdRoomPlayerRequestDto dto = gson.fromJson(jsonObject.get("ThirdRoomPlayerRequestDto").getAsJsonObject(), ThirdRoomPlayerRequestDto.class);
            ThirdRoom thirdRoom = ThirdRoom.toDto(dto);
            ThirdRoom oldRoom = gameRoom.getThirdRoom();
            thirdRoom.modifyDto(oldRoom);
            gameRoom.modifyThirdRoom(thirdRoom);
        }
        saveGameRoomToTemp(gameRoom);
    }
}

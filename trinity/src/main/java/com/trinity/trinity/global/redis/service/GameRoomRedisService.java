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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GameRoomRedisService {
    private final RedisTemplate<String, Object> gameRoomRedisTemplate;

    @Synchronized
    public void createGameRoom(GameRoom gameRoom) {
        List<GameRoom> list = new ArrayList<>();
        list.add(gameRoom);

        gameRoomRedisTemplate.opsForHash().put("gameRoom", gameRoom.getGameRoomId(), list);
    }

    @Synchronized
    public GameRoom getGameRoom(String gameRoomId) {
        // temp game room에 없으면 만들고
        GameRoom findRoom = (GameRoom) gameRoomRedisTemplate.opsForHash().get("temp", gameRoomId);

        if (findRoom == null) {

            System.out.println("temp에 없을 때!!!");

            List<GameRoom> before = (List<GameRoom>) gameRoomRedisTemplate.opsForHash().get("gameRoom", gameRoomId);

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

            gameRoomRedisTemplate.opsForHash().put("temp", gameRoomId, gameRoom);
            return gameRoom;
        }

        System.out.println("있다 있다 있ㄲ다");

        return findRoom;
    }

    @Synchronized
    public void saveGameRoomToTemp(GameRoom gameRoom) {
        gameRoomRedisTemplate.opsForHash().put("temp", gameRoom.getGameRoomId(), gameRoom);
    }

    @Synchronized
    public void saveGameRoom(GameRoom gameRoom) {
        List<GameRoom> list = (List<GameRoom>) gameRoomRedisTemplate.opsForHash().get("gameRoom", gameRoom.getGameRoomId());
        list.add(gameRoom);
        gameRoomRedisTemplate.opsForHash().put("gameRoom", gameRoom.getGameRoomId(), list);

    }

    @Synchronized
    public void deleteGameRoom(String gameRoomId) {
        gameRoomRedisTemplate.opsForHash().delete("gameRoom", gameRoomId);
    }

    public void updateRoom(Gson gson, JsonObject jsonObject, String roomNum) throws InterruptedException {
        // 방번호 확인하는 로직
        if (roomNum.equals("first")) {
            FirstRoomPlayerRequestDto dto = gson.fromJson(jsonObject.get("FirstRoomPlayerRequestDto").getAsJsonObject(), FirstRoomPlayerRequestDto.class);
            updateFirstRoom(dto);
        } else if (roomNum.equals("second")) {
            Thread.sleep(1750);
            SecondRoomPlayerRequestDto dto = gson.fromJson(jsonObject.get("SecondRoomPlayerRequestDto").getAsJsonObject(), SecondRoomPlayerRequestDto.class);
            updateSecondRoom(dto);
        } else {
            Thread.sleep(3500);
            ThirdRoomPlayerRequestDto dto = gson.fromJson(jsonObject.get("ThirdRoomPlayerRequestDto").getAsJsonObject(), ThirdRoomPlayerRequestDto.class);
            updateThridRoom(dto);
        }
    }

    public void updateFirstRoom(FirstRoomPlayerRequestDto firstRoomPlayerRequestDto) {
        // 게임방 가져오고
        GameRoom gameRoom = getGameRoom(firstRoomPlayerRequestDto.getGameRoomId());
        // 게임방 1번방 정보 넣기
        FirstRoom firstRoom = FirstRoom.toDto(firstRoomPlayerRequestDto);
        FirstRoom oldRoom = gameRoom.getFirstRoom();

        firstRoom.modifyDto(oldRoom);

        System.out.println("firstRoom.getPlayer() : " + firstRoom.getPlayer());

        gameRoom.modifyFirstRoom(firstRoom);

        System.out.println(gameRoom.getFirstRoom().getPlayer());

        saveGameRoomToTemp(gameRoom);
    }

    public void updateSecondRoom(SecondRoomPlayerRequestDto secondRoomPlayerRequestDto) {
        GameRoom gameRoom = getGameRoom(secondRoomPlayerRequestDto.getGameRoomId());

        SecondRoom secondRoom = SecondRoom.toDto(secondRoomPlayerRequestDto);
        SecondRoom oldRoom = gameRoom.getSecondRoom();

        secondRoom.modifyDto(oldRoom);
        gameRoom.modifySecondRoom(secondRoom);

        saveGameRoomToTemp(gameRoom);
    }

    public void updateThridRoom(ThirdRoomPlayerRequestDto thirdRoomPlayerRequestDto) {
        GameRoom gameRoom = getGameRoom(thirdRoomPlayerRequestDto.getGameRoomId());

        ThirdRoom thirdRoom = ThirdRoom.toDto(thirdRoomPlayerRequestDto);
        ThirdRoom oldRoom = gameRoom.getThirdRoom();

        thirdRoom.modifyDto(oldRoom);
        gameRoom.modifyThirdRoom(thirdRoom);

        saveGameRoomToTemp(gameRoom);
    }
}

package com.trinity.trinity.global.redis.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.trinity.trinity.global.dto.ClientSession;
import com.trinity.trinity.domain.logic.dto.GameRoomCheck;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, String> redisLoginTemplate;
    private final RedisTemplate<String, Object> redisTemplate;

    public void saveData(String key, String value) {
        redisLoginTemplate.opsForHash().put("connectingMember", key, value);
    }

    public void saveClient(ClientSession clientSession) throws JsonProcessingException {
        redisTemplate.opsForHash().put("ClientSession", clientSession.getUserId(), clientSession);
    }

    public ClientSession getClientSession(String key) {
        ClientSession clientSession = (ClientSession) redisTemplate.opsForHash().get("ClientSession", key);
        return clientSession;
    }

    public void removeClientSession(String userId) {
        redisTemplate.opsForHash().delete("ClientSession", userId);
    }

    public String getClientId(String userId) {
        System.out.println(userId);
        ClientSession channelInfo = (ClientSession) redisTemplate.opsForHash()
                .get("ClientSession", userId);
        String clientId = channelInfo.getClientId();
        return clientId;
    }

    public void saveGameRoomUserStatus(String gameRoomId) {
        GameRoomCheck gameRoomCheck = GameRoomCheck.builder().build();
        redisTemplate.opsForHash().put("gameRoomCheck", gameRoomId, gameRoomCheck);
    }

    public boolean checkGameRoomAllClear(String gameRoomId, String roomNum) {
        System.out.println("checkGameRoomAllClear의 안쪽입니다!!!!!!!!!!");

        GameRoomCheck checkList = (GameRoomCheck) redisTemplate.opsForHash().get("gameRoomCheck", gameRoomId);
        boolean complete = checkList.checkRoom(roomNum);
        if (complete) {
            System.out.println("3명다 데이터가 들어왔습니다!!!!!!!!!!!!!!!!");
            checkList = new GameRoomCheck();
            redisTemplate.opsForHash().put("gameRoomCheck", gameRoomId, checkList);
            return true;
        } else {
            System.out.println("아직 안들어왔습니다 @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            redisTemplate.opsForHash().put("gameRoomCheck", gameRoomId, checkList);
            return false;
        }
    }
}
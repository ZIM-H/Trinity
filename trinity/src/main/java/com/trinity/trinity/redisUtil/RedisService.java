package com.trinity.trinity.redisUtil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.trinity.trinity.client.ClientSession;
import com.trinity.trinity.gameRoom.dto.GameRoomCheck;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public String getClientId(String userId) {
        System.out.println(userId);
        ClientSession channelInfo = (ClientSession) redisTemplate.opsForHash()
                .get("ClientSession", userId);
        String clientId = channelInfo.getClientId();
        return clientId;
    }

    public boolean checkGameRoomAllClear(String gameRoomId, String roomNum) {
        GameRoomCheck checkList = (GameRoomCheck) redisTemplate.opsForHash().get("gameRoomCheck", gameRoomId);
        boolean complete = checkList.checkRoom(roomNum);
        if(complete){
            checkList = new GameRoomCheck();
            redisTemplate.opsForHash().put("connectingMember", gameRoomId, checkList);
            return true;
        } else {
            redisTemplate.opsForHash().put("connectingMember", gameRoomId, checkList);
            return false;
        }
    }
}
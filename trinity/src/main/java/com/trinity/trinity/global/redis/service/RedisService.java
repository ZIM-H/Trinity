package com.trinity.trinity.global.redis.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.trinity.trinity.domain.control.enums.UserStatus;
import com.trinity.trinity.global.dto.ClientSession;
import com.trinity.trinity.domain.logic.dto.GameRoomCheck;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, String> redisLoginTemplate;
    private final RedisTemplate<String, Object> redisTemplate;

    @Synchronized
    public void saveData(String key, String value) {
        redisLoginTemplate.opsForHash().put("connectingMember", key, value);
    }

    @Synchronized
    public void backToLooby(String[] userIds) {
        for (String userId : userIds)
            redisLoginTemplate.opsForHash().put("connectingMember", userId, String.valueOf(UserStatus.LOBBY));
    }

    @Synchronized
    public void saveClient(ClientSession clientSession) {
        redisTemplate.opsForHash().put("ClientSession", clientSession.getUserId(), clientSession);
    }


    public ClientSession getClientSession(String key) {
        ClientSession clientSession = (ClientSession) redisTemplate.opsForHash().get("ClientSession", key);
        return clientSession;
    }

    @Synchronized
    public void removeClientListSession(String[] userIds) {
        for (String userId : userIds) redisTemplate.opsForHash().delete("ClientSession", userId);
    }


    public String getClientId(String userId) {
        ClientSession channelInfo = (ClientSession) redisTemplate.opsForHash()
                .get("ClientSession", userId);
        String clientId = channelInfo.getClientId();

        return clientId;
    }

    @Synchronized
    public String[] getClientIdList(String[] userIds) {
        String[] result = new String[3];
        for (int i = 0; i < 3; i++) {
            ClientSession channelInfo = (ClientSession) redisTemplate.opsForHash()
                    .get("ClientSession", userIds[i]);
            result[i] = channelInfo.getClientId();
        }

        return result;
    }

    @Synchronized
    public void saveGameRoomUserStatus(String gameRoomId) {
        GameRoomCheck gameRoomCheck = GameRoomCheck.builder().build();
        redisTemplate.opsForHash().put("gameRoomCheck", gameRoomId, gameRoomCheck);
    }

    @Synchronized
    public boolean checkGameRoomAllClear(String gameRoomId, String roomNum) {
        GameRoomCheck checkList = (GameRoomCheck) redisTemplate.opsForHash().get("gameRoomCheck", gameRoomId);
        boolean complete = checkList.checkRoom(roomNum);
        if (complete) {
            checkList = new GameRoomCheck();
            redisTemplate.opsForHash().put("gameRoomCheck", gameRoomId, checkList);
            return true;
        } else {
            redisTemplate.opsForHash().put("gameRoomCheck", gameRoomId, checkList);
            return false;
        }
    }
}
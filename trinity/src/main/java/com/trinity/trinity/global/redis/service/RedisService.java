package com.trinity.trinity.global.redis.service;

import com.trinity.trinity.domain.control.enums.UserStatus;
import com.trinity.trinity.global.dto.ClientSession;
import com.trinity.trinity.domain.logic.dto.GameRoomCheck;
import com.trinity.trinity.global.dto.ClientUserId;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@Component
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, String> redisLoginTemplate;
    private final RedisTemplate<String, Object> redisTemplate;

    private HashOperations<String, String, String> loginHashOperations;
    private HashOperations<String, String, Object> hashOperations;

    @PostConstruct
    private void initLoginHash() {
        loginHashOperations = redisLoginTemplate.opsForHash();
    }
    @PostConstruct
    private void init() {
        hashOperations = redisTemplate.opsForHash();
    }

    @Synchronized
    public void saveData(String key, String value) {
        loginHashOperations.put("connectingMember", key, value);
    }

    @Synchronized
    public  String getData(String key) {
        return loginHashOperations.get("connectingMember", key);
    }

    @Synchronized
    public void backToLooby(String[] userIds) {
        for (String userId : userIds)
            loginHashOperations.put("connectingMember", userId, String.valueOf(UserStatus.LOBBY));
    }

    @Synchronized
    public void removeMatching(String userId) {
        loginHashOperations.put("connectingMember", userId, String.valueOf(UserStatus.LOBBY));
    }

    @Synchronized
    public void saveClient(ClientSession clientSession) {
        hashOperations.put("ClientSession", clientSession.getUserId(), clientSession);
    }


    public ClientSession getClientSession(String key) {
        ClientSession clientSession = (ClientSession) hashOperations.get("ClientSession", key);
        return clientSession;
    }

    @Synchronized
    public void removeClientListSession(String[] userIds) {
        for (String userId : userIds) hashOperations.delete("ClientSession", userId);
    }


    public String getClientId(String userId) {
        ClientSession channelInfo = (ClientSession) hashOperations.get("ClientSession", userId);
        String clientId = channelInfo.getClientId();

        return clientId;
    }

    @Synchronized
    public String[] getClientIdList(String[] userIds) {
        String[] result = new String[3];
        for (int i = 0; i < 3; i++) {
            ClientSession channelInfo = (ClientSession) hashOperations.get("ClientSession", userIds[i]);
            result[i] = channelInfo.getClientId();
        }

        return result;
    }

    @Synchronized
    public void saveGameRoomUserStatus(String gameRoomId) {
        GameRoomCheck gameRoomCheck = GameRoomCheck.builder().build();
        hashOperations.put("gameRoomCheck", gameRoomId, gameRoomCheck);
    }

    @Synchronized
    public boolean checkGameRoomAllClear(String gameRoomId, String roomNum) {
        GameRoomCheck checkList = (GameRoomCheck) hashOperations.get("gameRoomCheck", gameRoomId);
        boolean complete = checkList.checkRoom(roomNum);
        if (complete) {
            checkList = new GameRoomCheck();
            hashOperations.put("gameRoomCheck", gameRoomId, checkList);
            return true;
        } else {
            hashOperations.put("gameRoomCheck", gameRoomId, checkList);
            return false;
        }
    }

    //clientId - userId 부분

    @Synchronized
    public void saveUserId(ClientUserId client) {
        hashOperations.put("ClientUserId", client.getClientId(), client.getUserId());
    }

    public String getUserId(String clientId) {
        return (String) hashOperations.get("ClientUserId", clientId);
    }

    public void removeUserId(String clientId) {
        hashOperations.delete("ClientUserId", clientId);
    }
}
package com.trinity.trinity.redisUtil;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.trinity.trinity.client.ClientSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    public void saveData(String key, String value) {
        redisTemplate.opsForHash().put("connectingMember", key, value);
    }

    public String retrieveData(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }

    public void deleteData(String key) {
        redisTemplate.delete(key);
    }


    public void saveClient(ClientSession clientSession){
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("clientId", clientSession.getClientId());
        userInfo.put("clientAddress", clientSession.getClientAddress());
        redisTemplate.opsForHash().put("ClientSession", clientSession.getUserId(), userInfo);
    }
    public ClientSession getClientSession(String key) {
        return (ClientSession) redisTemplate.opsForHash().get("ClientSession", key);
    }

    public String getClientAddress(String userId) {

        String channelInfo = redisTemplate.<String, String>opsForHash()
                .get("ClientSession", userId);
        JsonObject jsonObject = new JsonParser().parse(channelInfo).getAsJsonObject();
        String channelAddress = jsonObject.get("channelAddress").getAsString();

        return channelAddress;
    }
}

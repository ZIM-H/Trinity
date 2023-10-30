package com.trinity.trinity.redisUtil;

import com.trinity.trinity.client.ClientSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {
    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public RedisService(RedisTemplate<String, String> redisTemplate) {

        this.redisTemplate = redisTemplate;
    }

    public void saveData(String key, String value) {
        redisTemplate.opsForHash().put("connectingMember", key, value);
    }

    public String retrieveData(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }

    public void deleteData(String key) {
        redisTemplate.delete(key);
    }


    public void saveClient(String key, String clientSession){
        if (clientSession != null && redisTemplate != null) {
            System.out.println(key);
            System.out.println(clientSession);
            System.out.println(redisTemplate);
            redisTemplate.opsForHash().put("ClientSession", key, clientSession);
        } else if(redisTemplate == null){
            // 예외 처리 또는 오류 메시지를 로깅합니다.
            System.err.println("redisTemplate is null");
        } else {
            System.err.println("String is null");
        }
    }
    public ClientSession getClientSession(String key) {
        return (ClientSession) redisTemplate.opsForHash().get("ClientSession", key);
    }
}

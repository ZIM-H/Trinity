package com.trinity.trinity.redisUtil;

import com.trinity.trinity.client.ClientSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

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


    public void saveClient(String key, String clientSession){
        if (clientSession != null && redisTemplate != null) {
            redisTemplate.opsForHash().put("ClientSession", key, "정의민");
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

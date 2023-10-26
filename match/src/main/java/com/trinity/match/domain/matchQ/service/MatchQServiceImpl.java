package com.trinity.match.domain.matchQ.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchQServiceImpl implements MatchQService {

    @Qualifier("matchRedisTemplate")
    private final RedisTemplate<String, String> matchRedisTemplate;
    @Qualifier("gameRedisTemplate")
    private final RedisTemplate<String, String> gameRedisTemplate;

    @Override
    public void joinQueue(String userId) {
        double time = System.currentTimeMillis();
        matchRedisTemplate.opsForZSet().add("gameQueue", userId, time);
    }

    @Scheduled(fixedRate = 5000)
    public void checkQueueSize() {
        /*
            1. 대기 큐의 크기가 3 이상인지 확인
            2. webSocket을 보낼 사람들을 담을 리스트 생성
            3. 대기 큐에서 꺼내 아직 접속 중인지 확인
                1. 접속 중이면 리스트에 담기
                2. 접속 중이 아니면 ip 정보 삭제
            4. 대기 큐에서 삭제
            5. 리스트에 담긴 사람들에게 webSocket 발송
         */
        Long size = matchRedisTemplate.opsForZSet().size("gameQueue");

        if (size >= 3) {
            List<String> waitingList = new ArrayList<>();

//            int no = 0;
//            while (no != 3) {
//                String findUserId = redisTemplate.opsForZSet().range("gameQueue", 0, 0).iterator().next();
//                if ()
//                no++;
//            }

            for (int i = 0; i < 3; i++) {
                String findUserId = matchRedisTemplate.opsForZSet().range("gameQueue", 0, 0).iterator().next();
                waitingList.add(findUserId);
                matchRedisTemplate.opsForZSet().remove("gameQueue", findUserId);
            }


        }
    }
}

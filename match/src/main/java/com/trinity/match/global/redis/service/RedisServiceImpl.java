package com.trinity.match.global.redis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {
    @Qualifier("matchRedisTemplate")
    private final RedisTemplate<String, String> matchRedisTemplate;
    @Qualifier("gameRedisTemplate")
    private final RedisTemplate<String, String> gameRedisTemplate;

    private static final String MATCH_QUEUE = "matchQueue";

    @Override
    public boolean addUser(String userId) {
        double time = System.currentTimeMillis();
        return matchRedisTemplate.opsForZSet().add(MATCH_QUEUE, userId, time);
    }

    @Override
    public void recoverList(List<Pair<String, Double>> waitingList) {
        for (Pair<String, Double> userAndScore : waitingList) {
            matchRedisTemplate.opsForZSet().add("matchQueue", userAndScore.getFirst(), userAndScore.getSecond());
        }
    }

    @Override
    public void deleteData(String key) {
        matchRedisTemplate.opsForZSet().remove("matchQueue", key);
    }

    @Override
    public long getSize() {
        return matchRedisTemplate.opsForZSet().size("matchQueue");
    }

    @Override
    public Object validate(String findUserId) {
        return gameRedisTemplate.opsForHash().get("connectingMember", findUserId);
    }

    @Override
    public Set<ZSetOperations.TypedTuple<String>> getSet() {
        return matchRedisTemplate.opsForZSet().rangeWithScores("matchQueue", 0, 0);
    }
}

package com.trinity.match.global.redis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {
    @Qualifier("matchRedisTemplate")
    private final RedisTemplate<String, String> matchRedisTemplate;
    @Qualifier("gameRedisTemplate")
    private final RedisTemplate<String, String> gameRedisTemplate;

    private ZSetOperations<String, String> matchOperations;
    private HashOperations<String, String, String> gameOperations;

    @PostConstruct
    private void init() {
        matchOperations = matchRedisTemplate.opsForZSet();
        gameOperations = gameRedisTemplate.opsForHash();
    }

    private static final String MATCH_QUEUE = "matchQueue";

    @Override
    public boolean addUser(String userId) {
        double time = System.currentTimeMillis();
        return matchOperations.add(MATCH_QUEUE, userId, time);
    }

    @Override
    public void recoverList(List<Pair<String, Double>> waitingList) {
        for (Pair<String, Double> userAndScore : waitingList) {
            matchOperations.add("matchQueue", userAndScore.getFirst(), userAndScore.getSecond());
        }
    }

    @Override
    public void deleteData(String key) {
        matchOperations.remove("matchQueue", key);
    }

    @Override
    public long getSize() {
        return matchOperations.size("matchQueue");
    }

    @Override
    public Object validate(String findUserId) {
        return gameOperations.get("connectingMember", findUserId);
    }

    @Override
    public Set<ZSetOperations.TypedTuple<String>> getSet() {
        return matchOperations.rangeWithScores("matchQueue", 0, 0);
    }
}

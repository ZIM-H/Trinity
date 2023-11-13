package com.trinity.match.domain.matchQ.service;

import com.trinity.match.domain.matchQ.dto.request.GameServerPlayerListRequestDto;
import com.trinity.match.global.webClient.WebClientService;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.util.Pair;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchQServiceImpl implements MatchQService {

    @Qualifier("matchRedisTemplate")
    private final RedisTemplate<String, String> matchRedisTemplate;
    @Qualifier("gameRedisTemplate")
    private final RedisTemplate<String, String> gameRedisTemplate;
    private final RedissonClient matchRedissonClient;
    private final WebClientService webClientService;

    private static final String MATCH_QUEUE = "matchQueue";
    private static final String LOCK_NAME = "matchQueueLock";

    @Override
    public void joinQueue(String userId) {
        RLock lock = matchRedissonClient.getLock(LOCK_NAME);
        lock.lock();
        try {
            double time = System.currentTimeMillis();
            matchRedisTemplate.opsForZSet().add(MATCH_QUEUE, userId, time);
        } finally {
            lock.unlock();
        }
    }

//    @Override
//    @Synchronized
//    public void joinQueue(String userId) {
//        double time = System.currentTimeMillis();
//        matchRedisTemplate.opsForZSet().add("matchQueue", userId, time);
//    }

//    @Synchronized
//    @Scheduled(fixedRate = 2000)
//    private void checkQueueSize() {
//        // SessionCallback 내에 트랜잭션 구현
//        matchRedisTemplate.execute(new SessionCallback<Object>() {
//            @Override
//            public Object execute(RedisOperations operations) {
//                List<Pair<String, Double>> waitingList = new ArrayList<>();
//                try {
//                    // watch함수로 대기 큐의 변경 감지
//                    operations.watch("matchQueue");
//
//                    // 대기 큐의 크기가 3 보다 작으면 그만
//                    Long size = operations.opsForZSet().size("matchQueue");
//                    if (size < 3) return null;
//
//                    while (waitingList.size() != 3) {
//                        // 1순위 사람 뽑기
//                        Set<ZSetOperations.TypedTuple<String>> rangeWithScores = operations.opsForZSet().rangeWithScores("matchQueue", 0, 0);
//                        if (rangeWithScores == null || rangeWithScores.isEmpty()) break;
//
//                        ZSetOperations.TypedTuple<String> next = rangeWithScores.iterator().next();
//                        String findUserId = next.getValue();
//                        Double score = next.getScore();
//
//                        // 게임 서버 redis에 접근해 유효성 검사
//                        Object state = gameRedisTemplate.opsForHash().get("connectingMember", findUserId);
//                        if (state != null && state.toString().equals("WAITING")) {
//                            waitingList.add(Pair.of(findUserId, score));
//                        }
//                        operations.opsForZSet().remove("matchQueue", findUserId);
//                    }
//
//                    // 게임 서버에 보낼 리스트의 크기가 3보다 작으면 다시 대기큐에 넣고 돌아가기
//                    if (waitingList.size() < 3) {
//                        recoverList(waitingList);
//                        return null;
//                    }
//
//                    // 트랜잭션 시작
//                    operations.multi();
//
//                    for (Pair<String, Double> userAndScore : waitingList) {
//                        operations.opsForZSet().remove("matchQueue", userAndScore.getFirst());
//                    }
//
//                    // 트랜잭션 실행
//                    operations.exec();
//
//                    List<GameServerPlayerListRequestDto> playerList = new ArrayList<>();
//                    for (Pair<String, Double> userAndScore : waitingList)
//                        playerList.add(GameServerPlayerListRequestDto.builder()
//                                .userId(userAndScore.getFirst())
//                                .build());
//
//                    try {
//                        webClientService.post(playerList);
//                    } catch (Exception e) {
//                        // WebClient 호출 실패시 대기 큐에 다시 추가
//                        recoverList(waitingList);
//                        throw e;
//                    }
//
//                } catch (Exception e) {
//                    // 에러 발생하면 에러 메시지 찍고 대기 큐에 다시 넣기
//                    log.error(e.getMessage());
//                    recoverList(waitingList);
//                }
//                return null;
//            }
//
//        });
//    }

//    @Synchronized
//    @Scheduled(fixedRate = 2000)
//    private void checkQueueSize() {
//        List<Pair<String, Double>> waitingList = new ArrayList<>();
//        try {
//            // 대기 큐의 크기가 3 보다 작으면 그만
//            if (getSize() < 3) return;
//
//            while (waitingList.size() != 3) {
//                // 1순위 사람 뽑기
//                Set<ZSetOperations.TypedTuple<String>> rangeWithScores = getSet();
//                if (rangeWithScores == null || rangeWithScores.isEmpty()) break;
//
//                ZSetOperations.TypedTuple<String> next = rangeWithScores.iterator().next();
//                String findUserId = next.getValue();
//                Double score = next.getScore();
//
//                // 게임 서버 redis에 접근해 유효성 검사
//                Object state = validate(findUserId);
//                if (state != null && state.toString().equals("WAITING")) {
//                    waitingList.add(Pair.of(findUserId, score));
//                }
//                deleteData(findUserId);
//            }
//
//            // 게임 서버에 보낼 리스트의 크기가 3보다 작으면 다시 대기큐에 넣고 돌아가기
//            if (waitingList.size() < 3) {
//                recoverList(waitingList);
//                return;
//            }
//
//            for (Pair<String, Double> userAndScore : waitingList) {
//                deleteData(userAndScore.getFirst());
//            }
//
//            List<GameServerPlayerListRequestDto> playerList = new ArrayList<>();
//            for (Pair<String, Double> userAndScore : waitingList)
//                playerList.add(GameServerPlayerListRequestDto.builder()
//                        .userId(userAndScore.getFirst())
//                        .build());
//
//            try {
//                webClientService.post(playerList);
//            } catch (Exception e) {
//                // WebClient 호출 실패시 대기 큐에 다시 추가
//                recoverList(waitingList);
//                throw e;
//            }
//
//        } catch (Exception e) {
//            // 에러 발생하면 에러 메시지 찍고 대기 큐에 다시 넣기
//            log.error(e.getMessage());
//            recoverList(waitingList);
//        }
//    }

    @Synchronized
    @Scheduled(fixedRate = 2000)
    private void checkQueueSize() {
        RLock lock = matchRedissonClient.getLock(LOCK_NAME);
        lock.lock();
        List<Pair<String, Double>> waitingList = new ArrayList<>();
        try {
            // 대기 큐의 크기가 3 보다 작으면 그만
            if (getSize() < 3) return;

            while (waitingList.size() != 3) {
                // 1순위 사람 뽑기
                Set<ZSetOperations.TypedTuple<String>> rangeWithScores = getSet();
                if (rangeWithScores == null || rangeWithScores.isEmpty()) break;

                ZSetOperations.TypedTuple<String> next = rangeWithScores.iterator().next();
                String findUserId = next.getValue();
                Double score = next.getScore();

                // 게임 서버 redis에 접근해 유효성 검사
                Object state = validate(findUserId);
                if (state != null && state.toString().equals("WAITING")) {
                    waitingList.add(Pair.of(findUserId, score));
                }
                deleteData(findUserId);
            }

            // 게임 서버에 보낼 리스트의 크기가 3보다 작으면 다시 대기큐에 넣고 돌아가기
            if (waitingList.size() < 3) {
                recoverList(waitingList);
                return;
            }

            for (Pair<String, Double> userAndScore : waitingList) {
                deleteData(userAndScore.getFirst());
            }

            List<GameServerPlayerListRequestDto> playerList = new ArrayList<>();
            for (Pair<String, Double> userAndScore : waitingList)
                playerList.add(GameServerPlayerListRequestDto.builder()
                        .userId(userAndScore.getFirst())
                        .build());

            try {
                webClientService.post(playerList);
            } catch (Exception e) {
                // WebClient 호출 실패시 대기 큐에 다시 추가
                recoverList(waitingList);
                throw e;
            }

        } catch (Exception e) {
            // 에러 발생하면 에러 메시지 찍고 대기 큐에 다시 넣기
            log.error(e.getMessage());
            recoverList(waitingList);
        } finally {
            lock.unlock();
        }
    }

    @Synchronized
    private void recoverList(List<Pair<String, Double>> waitingList) {
        for (Pair<String, Double> userAndScore : waitingList) {
            matchRedisTemplate.opsForZSet().add("matchQueue", userAndScore.getFirst(), userAndScore.getSecond());
        }
    }

    @Synchronized
    private void deleteData(String key) {
        matchRedisTemplate.opsForZSet().remove("matchQueue", key);
    }

    @Synchronized
    private long getSize() {
        return matchRedisTemplate.opsForZSet().size("matchQueue");
    }

    @Synchronized
    private Object validate(String findUserId) {
        return gameRedisTemplate.opsForHash().get("connectingMember", findUserId);
    }

    @Synchronized
    private Set<ZSetOperations.TypedTuple<String>> getSet() {
        return matchRedisTemplate.opsForZSet().rangeWithScores("matchQueue", 0, 0);
    }
}

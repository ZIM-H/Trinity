package com.trinity.match.domain.matchQ.service;

import com.trinity.match.domain.matchQ.dto.request.GameServerPlayerListRequestDto;
import com.trinity.match.global.redis.service.RedisServiceImpl;
import com.trinity.match.global.webClient.WebClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.util.Pair;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchQServiceImpl implements MatchQService {

    private final RedisServiceImpl redisService;
    private final RedissonClient matchRedissonClient;
    private final WebClientService webClientService;

    private static final String LOCK_NAME = "matchQueueLock";

    // ===============================================
    @Override
    public boolean joinQueue(String userId) {
        RLock lock = matchRedissonClient.getLock(LOCK_NAME);
        lock.lock();
        try {
            return redisService.addUser(userId);
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        } finally {
            lock.unlock();
        }
    }

    @Scheduled(fixedRate = 2000)
    private void checkQueueSize() {
        RLock lock = matchRedissonClient.getLock(LOCK_NAME);
        lock.lock();
        List<Pair<String, Double>> waitingList = new ArrayList<>();
        try {
            // 대기 큐의 크기가 3 보다 작으면 그만
            if (redisService.getSize() < 3) return;

            while (waitingList.size() != 3) {
                // 1순위 사람 뽑기
                Set<ZSetOperations.TypedTuple<String>> rangeWithScores = redisService.getSet();
                if (rangeWithScores == null || rangeWithScores.isEmpty()) break;

                ZSetOperations.TypedTuple<String> next = rangeWithScores.iterator().next();
                String findUserId = next.getValue();
                Double score = next.getScore();

                // 게임 서버 redis에 접근해 유효성 검사
                Object state = redisService.validate(findUserId);

                log.info("findUserId : " + findUserId + " " + state.toString());

                if (state != null && state.toString().equals("WAITING")) {
                    waitingList.add(Pair.of(findUserId, score));
                }
                redisService.deleteData(findUserId);
            }

            // 게임 서버에 보낼 리스트의 크기가 3보다 작으면 다시 대기큐에 넣고 돌아가기
            if (waitingList.size() < 3) {
                redisService.recoverList(waitingList);
                return;
            }

            for (Pair<String, Double> userAndScore : waitingList) {
                redisService.deleteData(userAndScore.getFirst());
            }

            List<GameServerPlayerListRequestDto> playerList = new ArrayList<>();
            for (Pair<String, Double> userAndScore : waitingList)
                playerList.add(GameServerPlayerListRequestDto.builder()
                        .userId(userAndScore.getFirst())
                        .build());

            webClientService.post(playerList, waitingList);

        } catch (Exception e) {
            // 에러 발생하면 에러 메시지 찍고 대기 큐에 다시 넣기
            log.error(e.getMessage());
            redisService.recoverList(waitingList);
        } finally {
            lock.unlock();
        }
    }



    // ====================================================================
//    static PriorityQueue<Player> pq = new PriorityQueue<Player>((o1, o2) -> Double.compare(o1.time, o2.time));
//
//    public static class Player{
//        String userId;
//        double time;
//
//        public Player(String userId, double time) {
//            this.userId = userId;
//            this.time = time;
//        }
//    }
//
//    @Override
//    public boolean joinQueue(String userId) {
//        double time = System.currentTimeMillis();
//        Player player = new Player(userId, time);
//        pq.offer(player);
//
//        try {
//            log.info("pq.size() : " + pq.size());
//            if (pq.size() >= 3) {
//                log.info("pq.size()가 3 이상인 경우만");
//                checkQueueSize();
//            }
//        } catch (Exception e) {
//            log.info(e.getMessage());
//            return false;
//        }
//
//        return true;
//    }
//
//    private void checkQueueSize() {
//        RLock lock = matchRedissonClient.getLock(LOCK_NAME);
//        lock.lock();
//
//        List<Player> waitingList = new ArrayList<>();
//        try {
//            Thread.sleep(2000);
//
//            while (waitingList.size() != 3) {
//                if (pq.size() == 0) break;
//
//                // 1순위 사람 뽑기
//                Player findPlayer = pq.poll();
//
//                Object state = redisService.validate(findPlayer.userId);
//
//                log.info("userId : " + findPlayer.userId + " " + state.toString());
//
//                if (state != null && state.toString().equals("WAITING")) {
//                    log.info("waitingList에 삽입");
//                    waitingList.add(findPlayer);
//                }
//            }
//
//            log.info("waitingList.size() : " + waitingList.size());
//
//            // 게임 서버에 보낼 리스트의 크기가 3보다 작으면 다시 대기큐에 넣고 돌아가기
//            if (waitingList.size() < 3) {
//                log.info("복구 전 pq.size() : " + pq.size());
//
//                for (Player player : waitingList) pq.offer(player);
//
//                log.info("복구 후 pq.size() : " + pq.size());
//
//                return;
//            }
//
//            List<GameServerPlayerListRequestDto> playerList = new ArrayList<>();
//            for (Player player : waitingList)
//                playerList.add(GameServerPlayerListRequestDto.builder()
//                        .userId(player.userId)
//                        .build());
//
//            webClientService.post(playerList, waitingList);
//
//        } catch (Exception e) {
//            // 에러 발생하면 에러 메시지 찍고 대기 큐에 다시 넣기
//            log.error(e.getMessage());
////            recoverList(waitingList);
//            for (Player player : waitingList) pq.offer(player);
//        } finally {
//            lock.unlock();
//        }
//    }
}

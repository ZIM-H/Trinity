package com.trinity.trinity.domain.logic.service;

import com.trinity.trinity.domain.logic.dto.*;
import com.trinity.trinity.global.redis.service.GameRoomRedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameRoomServiceImpl implements GameRoomService {

    private final GameRoomRedisService gameRoomRedisService;

    @Override
    public boolean gameLogic(GameRoom gameRoom) {
        gameRoom.setRoundNo(gameRoom.getRoundNo() + 1);
        FirstRoom firstRoom = gameRoom.getFirstRoom();
        SecondRoom secondRoom = gameRoom.getSecondRoom();
        ThirdRoom thirdRoom = gameRoom.getThirdRoom();

        gameRoom.setFoodAmount(gameRoom.getFoodAmount() - 1);
        if (gameRoom.isCarbonCaptureNotice()) gameRoom.setCarbonCaptureNotice(false);

        // 자동 보호막
        if (thirdRoom.isBarrierDevTry()) {
            thirdRoom.modifyBarrierDevTry(false);
            if (thirdRoom.getUserId().equals(thirdRoom.getDeveloper())) {
                thirdRoom.modifyBarrierStatus(thirdRoom.getBarrierStatus() + 1);
            } else {
                thirdRoom.modifyBarrierStatus(1);
                thirdRoom.modifyDeveloper(thirdRoom.getUserId());
            }
        }

        // 소행성
        if (thirdRoom.isAsteroidStatus()) {
            if (thirdRoom.isAsteroidDestroyTry()) {
                thirdRoom.modifyAsteroidDestroyTry(false);

                checkFarm(gameRoom, firstRoom, secondRoom, thirdRoom);
            } else {
                if (thirdRoom.getBarrierStatus() < 2) {
                    if (secondRoom.isFarmTry()) checkFarm(gameRoom, firstRoom, secondRoom, thirdRoom);
                    secondRoom.setFarmStatus(false);
                    gameRoom.setFertilizerAmount(0);
                } else {
                    checkFarm(gameRoom, firstRoom, secondRoom, thirdRoom);
                }
            }

            thirdRoom.modifyAsteroidStatus(false);
        } else checkFarm(gameRoom, firstRoom, secondRoom, thirdRoom);

        if (gameRoom.getFoodAmount() == 0) {
            log.info("식량 없어서 뒤짐");
            return false;
        }

        // 정수 시스템부터 ㄱㄱ
        if (firstRoom.getPurifierStatus() > 0) {
            firstRoom.modifyPurifierStatus(firstRoom.getPurifierStatus() + 1);
            if (firstRoom.isPurifierTry()) {
                firstRoom.modifyPurifierTry(false);
                firstRoom.modifyPurifierStatus(0);
                secondRoom.setTaurineFilterStatus(true);
            }

            if (firstRoom.getPurifierStatus() == 3) {
                log.info("정수 시스템 고장나서 뒤짐");
                return false;
            }

        } else {
            if (firstRoom.isPurifierTry()) {
                firstRoom.modifyPurifierTry(false);
                firstRoom.modifyPurifierStatus(1);
                secondRoom.setTaurineFilterStatus(false);
            }
        }

        // 이산화탄소 포집기
        if (secondRoom.getCarbonCaptureStatus() > 0) {
            secondRoom.setCarbonCaptureStatus(secondRoom.getCarbonCaptureStatus() + 1);
            if (secondRoom.isCarbonCaptureTry()) {
                secondRoom.setCarbonCaptureTry(false);
                secondRoom.setCarbonCaptureTryCount(secondRoom.getCarbonCaptureTryCount() + 1);
            }
            if (secondRoom.getCarbonCaptureTryCount() == 2) {
                secondRoom.setCarbonCaptureStatus(0);
                secondRoom.setCarbonCaptureTryCount(0);
            }
            if (secondRoom.getCarbonCaptureStatus() == 3) {
                log.info("이산화탄소 포집기 고장나서 뒤짐");
                return false;
            }
        }

        // 비료 생성기 업그레이드
        if (firstRoom.isFertilizerUpgradeTry()) {
            firstRoom.modifyFertilizerUpgradeTry(false);
            firstRoom.modifyFertilizerUpgradeStatus(firstRoom.getFertilizerUpgradeStatus() + 1);
        }

        if (firstRoom.getFertilizerUpgradeStatus() == 3) makeFertilizer(firstRoom, secondRoom, thirdRoom, 3);
        else makeFertilizer(firstRoom, secondRoom, thirdRoom, 2);

        // 타우린
        if (secondRoom.isTaurineFilterTry()) {
            secondRoom.setTaurineFilterTry(false);
            secondRoom.setTaurineFilterStatus(false);
            firstRoom.modifyPurifierStatus(1);
        }

        // 블랙홀
        if (thirdRoom.isBlackholeStatus()) {
            if (gameRoom.getRoundNo() + 2 <= 12) gameRoom.getBlackholeStatus()[gameRoom.getRoundNo() + 2] = true;
        }

        gameRoomRedisService.saveGameRoomToTemp(gameRoom);

        return true;
    }

    @Override
    public void morningGameLogic(GameRoom gameRoom) {
        SecondRoom secondRoom = gameRoom.getSecondRoom();
        ThirdRoom thirdRoom = gameRoom.getThirdRoom();
        Events events = gameRoom.getEvents();

        // 블랙홀 영향권인지 판단
        if (gameRoom.getBlackholeStatus()[gameRoom.getRoundNo()]) {
            movePlayer(0, gameRoom);
            thirdRoom.modifyBlackholeStatus(false);
        } else {
            movePlayer(1, gameRoom);
        }

        // 1일차에만 랜덤 이벤트 추출
        if (gameRoom.getRoundNo() == 1) {
            shuffleEvent(events);
        }

        // 라운드 수와 이벤트 종류에 따라 계산한 event 종류 저장
        int eventIdx = 0;

        int[][] eventArrays = {
                events.getAsteroid(),
                events.getBlackhole(),
                events.getBirthday(),
                events.getSickness(),
                events.getPurifier(),
                events.getCarbonCapture()
        };

        int[] eventWeights = {1, 2, 4, 8, 16, 32};

        for (int i = 0; i < 6; i++) {
            int[] arr = eventArrays[i];
            int value = eventWeights[i];

            for (int j = 0; j < arr.length; j++) {
                if (arr[j] == gameRoom.getRoundNo()) {
                    eventIdx += value;
                    // 선택된 이벤트에 따라 데이터 변경
                    validateEvent(i, gameRoom);
                    break;
                }
            }
        }

        // 식사는 10일에 고정
        if (gameRoom.getRoundNo() == 10) {
            eventIdx += 64;
        }

        gameRoom.setEvent(eventIdx);

        // 이산화탄소 고장 일수 2일차인지 판단
        if (secondRoom.getCarbonCaptureStatus() == 2) gameRoom.setCarbonCaptureNotice(true);

        // 로직 끝
        gameRoomRedisService.saveGameRoomToTemp(gameRoom);
        gameRoomRedisService.saveGameRoom(gameRoom);
    }

    @Override
    public boolean checkEndGame(GameRoom gameRoom) {
        if (gameRoom.getRoundNo() == 13) {
            gameRoomRedisService.deleteGameRoom(gameRoom.getGameRoomId());
            return true;
        }
        return false;
    }

    @Override
    public void endGame(String gameRoomId) {
        gameRoomRedisService.deleteGameRoom(gameRoomId);
    }


    private void shuffleEvent(Events events) {

        // 이벤트 등장 일수 랜덤 추출
        int[] randomEvent = shuffleArray(events.getAsteroid(), 12);
        events.setAsteroid(randomEvent);

        randomEvent = shuffleArray(events.getBlackhole(), 9);
        events.setBlackhole(randomEvent);

        randomEvent = shuffleArray(events.getBirthday(), 12);
        events.setBirthday(randomEvent);

        randomEvent = shuffleArray(events.getSickness(), 12);
        events.setSickness(randomEvent);

        randomEvent = shuffleArray(events.getPurifier(), 9);
        events.setPurifier(randomEvent);

        randomEvent = shuffleArray(events.getCarbonCapture(), 9);
        events.setCarbonCapture(randomEvent);
    }

    // 랜덤 이벤트 추출
    private int[] shuffleArray(int[] array, int size) {
        Integer[] days = new Integer[size];

        for (int i = 0; i < size; i++) {
            days[i] = i + 1;
        }

        List<Integer> list = Arrays.asList(days);
        Collections.shuffle(list);

        for (int i = 0; i < array.length; i++) {
            array[i] = list.get(i);
        }

        return array;
    }

    private void movePlayer(int direction, GameRoom gameRoom) {
        FirstRoom firstRoom = gameRoom.getFirstRoom();
        SecondRoom secondRoom = gameRoom.getSecondRoom();
        ThirdRoom thirdRoom = gameRoom.getThirdRoom();

        // 정방향
        if (direction == 1) {
            String temp = thirdRoom.getUserId();
            thirdRoom.modifyUserId(secondRoom.getPlayer());
            secondRoom.setPlayer(firstRoom.getUserId());
            firstRoom.modifyUserId(temp);

            return;
        }

        // 역방향
        String temp = thirdRoom.getUserId();
        thirdRoom.modifyUserId(firstRoom.getUserId());
        firstRoom.modifyUserId(secondRoom.getPlayer());
        secondRoom.setPlayer(temp);
    }

    private void validateEvent(int eventIdx, GameRoom gameRoom) {
        FirstRoom firstRoom = gameRoom.getFirstRoom();
        SecondRoom secondRoom = gameRoom.getSecondRoom();
        ThirdRoom thirdRoom = gameRoom.getThirdRoom();

        switch (eventIdx) {
            case 0:
                thirdRoom.modifyAsteroidStatus(true);
                break;
            case 1:
                thirdRoom.modifyBlackholeStatus(true);
                break;
            case 2:
                if (secondRoom.getCarbonCaptureStatus() == 0) secondRoom.setCarbonCaptureStatus(1);
                break;
            case 3:
                firstRoom.modifyPurifierStatus(1);
                break;
            case 4:
                gameRoom.setPlayerStatus(true);
                break;
            case 5:
                gameRoom.setBirthday(true);
                break;
            case 6:
                gameRoom.setFoodAmount(gameRoom.getFoodAmount() + 1);
                break;
        }
    }

    private void makeFertilizer(FirstRoom firstRoom, SecondRoom secondRoom, ThirdRoom thirdRoom, int fertilizer) {
        if (firstRoom.isMakeFertilizerTry()) {
            firstRoom.modifyMakeFertilizerTry(false);
            firstRoom.modifyFertilizerAmount(firstRoom.getFertilizerAmount() + fertilizer);
        }
        if (secondRoom.isMakeFertilizerTry()) {
            secondRoom.setMakeFertilizerTry(false);
            secondRoom.setFertilizerAmount(secondRoom.getFertilizerAmount() + fertilizer);
        }
        if (thirdRoom.isMakeFertilizerTry()) {
            thirdRoom.modifyMakeFertilizerTry(false);
            thirdRoom.modifyFertilizerAmount(thirdRoom.getFertilizerAmount() + fertilizer);
        }
    }

    private void checkFarm(GameRoom gameRoom, FirstRoom firstRoom, SecondRoom secondRoom, ThirdRoom thirdRoom) {
        if (!secondRoom.isFarmStatus()) {
            if (secondRoom.isFarmTry()) {
                secondRoom.setFarmTry(false);
                secondRoom.setFarmStatus(true);
                // 식량 생산
                makeFood(gameRoom, firstRoom, secondRoom, thirdRoom);
            }
        } else {
            // 식량 생산
            makeFood(gameRoom, firstRoom, secondRoom, thirdRoom);
        }
    }

    private void makeFood(GameRoom gameRoom, FirstRoom firstRoom, SecondRoom secondRoom, ThirdRoom thirdRoom) {
        int count = 0;
        if (firstRoom.isInputFertilizerTry()) {
            firstRoom.modifyInputFertilizerTry(false);
            firstRoom.modifyFertilizerAmount(firstRoom.getFertilizerAmount() - 1);
            count++;
        }
        if (secondRoom.isInputFertilizerTry()) {
            secondRoom.setInputFertilizerTry(false);
            secondRoom.setFertilizerAmount(secondRoom.getFertilizerAmount() - 1);
            count++;
        }
        if (thirdRoom.isInputFertilizerTry()) {
            thirdRoom.modifyInputFertilizerTry(false);
            thirdRoom.modifyFertilizerAmount(thirdRoom.getFertilizerAmount() - 1);
            count++;
        }

        int fertilizer = gameRoom.getFertilizerAmount() + count;
        if (fertilizer >= 4) {
            gameRoom.setFertilizerAmount(0);
            gameRoom.setFoodAmount(gameRoom.getFoodAmount() + 2);
        } else {
            gameRoom.setFertilizerAmount(fertilizer);
        }
    }
}

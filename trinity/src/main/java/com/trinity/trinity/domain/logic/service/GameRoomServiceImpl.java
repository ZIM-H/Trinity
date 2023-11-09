package com.trinity.trinity.domain.logic.service;

import com.trinity.trinity.domain.control.dto.PlayerDto;
import com.trinity.trinity.domain.control.dto.request.FirstRoomPlayerRequestDto;
import com.trinity.trinity.domain.control.dto.request.SecondRoomPlayerRequestDto;
import com.trinity.trinity.domain.control.dto.request.ThirdRoomPlayerRequestDto;
import com.trinity.trinity.domain.logic.dto.*;
import com.trinity.trinity.global.redis.service.GameRoomRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class GameRoomServiceImpl implements GameRoomService {

    private final GameRoomRedisService gameRoomRedisService;
    private final CreateService createService;

    @Override
    public GameRoom createGameRoom(List<PlayerDto> players) {
        String gameRoomId = UUID.randomUUID().toString();

        FirstRoom firstRoom = createService.createFirstRoom(players.get(0).getUserId());
        SecondRoom secondRoom = createService.createSecondRoom(players.get(1).getUserId());
        ThirdRoom thirdRoom = createService.createThirdRoom(players.get(2).getUserId());

        GameRoom gameRoom = GameRoom.builder()
                .gameRoomId(gameRoomId)
                .foodAmount(3)
                .fertilizerAmount(0)
                .playerStatus(false)
                .birthday(false)
                .carbonCaptureNotice(false)
                .blackholeStatus(new boolean[13])
                .events(Events.builder().build())
                .roundNo(1)
                .firstRoom(firstRoom)
                .secondRoom(secondRoom)
                .thirdRoom(thirdRoom)
                .build();

        gameRoomRedisService.createGameRoom(gameRoom);

        return gameRoom;
    }

    @Override
    public boolean gameLogic(GameRoom gameRoom) {
//        GameRoom gameRoom = gameRoomRedisService.getGameRoom(gameRoomId);
        gameRoom.setRoundNo(gameRoom.getRoundNo() + 1);
        FirstRoom firstRoom = gameRoom.getFirstRoom();
        SecondRoom secondRoom = gameRoom.getSecondRoom();
        ThirdRoom thirdRoom = gameRoom.getThirdRoom();

        gameRoom.setFoodAmount(gameRoom.getFoodAmount() - 1);
        if (gameRoom.isCarbonCaptureNotice()) gameRoom.setCarbonCaptureNotice(false);

        // 자동 보호막
        if (thirdRoom.isBarrierDevTry()) {
            thirdRoom.setBarrierDevTry(false);
            if (thirdRoom.getPlayer().equals(thirdRoom.getDeveloper())) {
                thirdRoom.setBarrierStatus(thirdRoom.getBarrierStatus() + 1);
            } else {
                thirdRoom.setBarrierStatus(1);
                thirdRoom.setDeveloper(thirdRoom.getPlayer());
            }
        }

        // 소행성
        if (thirdRoom.isAsteroidStatus()) {
            if (thirdRoom.isAsteroidDestroyTry()) {
                thirdRoom.setAsteroidDestroyTry(false);

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

            thirdRoom.setAsteroidStatus(false);
        } else checkFarm(gameRoom, firstRoom, secondRoom, thirdRoom);

        if (gameRoom.getFoodAmount() == 0) {
            System.out.println("식량 없어서 뒤짐");
            return false;
        }

        // 정수 시스템부터 ㄱㄱ
        if (firstRoom.getPurifierStatus() > 0) {
            firstRoom.setPurifierStatus(firstRoom.getPurifierStatus() + 1);
            if (firstRoom.isPurifierTry()) {
                firstRoom.setPurifierTry(false);
                firstRoom.setPurifierStatus(0);
                secondRoom.setTaurineFilterStatus(true);
            }

            if (firstRoom.getPurifierStatus() == 3) {
                System.out.println("정수 시스템 고장나서 뒤짐");
                return false;
            }

        } else {
            if (firstRoom.isPurifierTry()) {
                firstRoom.setPurifierTry(false);
                firstRoom.setPurifierStatus(1);
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
                System.out.println("이산화탄소 포집기 고장나서 뒤짐");
                return false;
            }
        }

        // 비료 생성기 업그레이드
        if (firstRoom.isFertilizerUpgradeTry()) {
            firstRoom.setFertilizerUpgradeTry(false);
            firstRoom.setFertilizerUpgradeStatus(firstRoom.getFertilizerUpgradeStatus() + 1);
        }

        if (firstRoom.getFertilizerUpgradeStatus() == 3) makeFertilizer(firstRoom, secondRoom, thirdRoom, 3);
        else makeFertilizer(firstRoom, secondRoom, thirdRoom, 2);

        // 타우린
        if (secondRoom.isTaurineFilterTry()) {
            secondRoom.setTaurineFilterTry(false);
            secondRoom.setTaurineFilterStatus(false);
            firstRoom.setPurifierStatus(1);
        }

        // 블랙홀
        if (thirdRoom.isBlackholeStatus()) {
            if (gameRoom.getRoundNo() + 2 <= 12) gameRoom.getBlackholeStatus()[gameRoom.getRoundNo() + 2] = true;
        }

        gameRoomRedisService.saveGameRoomToTemp(gameRoom);
        gameRoomRedisService.saveGameRoom(gameRoom);

        return true;
    }

    @Override
    public void morningGameLogic(GameRoom gameRoom) {
//        GameRoom gameRoom = gameRoomRedisService.getGameRoom(gameRoomId);
        SecondRoom secondRoom = gameRoom.getSecondRoom();
        ThirdRoom thirdRoom = gameRoom.getThirdRoom();
        Events events = gameRoom.getEvents();

        System.out.println("firstRoom : " + gameRoom.getFirstRoom().getPlayer());
        System.out.println("secondRoom : " + gameRoom.getSecondRoom().getPlayer());
        System.out.println("thirdRoom : " + gameRoom.getThirdRoom().getPlayer());


        // 블랙홀 영향권인지 판단
        if (gameRoom.getBlackholeStatus()[gameRoom.getRoundNo()]) {
            movePlayer(0, gameRoom);
            thirdRoom.setBlackholeStatus(false);
        } else {
            System.out.println("정방향띠");
            movePlayer(1, gameRoom);

            System.out.println("변환 후");
            System.out.println("firstRoom : " + gameRoom.getFirstRoom().getPlayer());
            System.out.println("secondRoom : " + gameRoom.getSecondRoom().getPlayer());
            System.out.println("thirdRoom : " + gameRoom.getThirdRoom().getPlayer());
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

    @Override
    public boolean checkEndGame(GameRoom gameRoom) {
//        GameRoom gameRoom = (GameRoom) gameRoomRedisService.getGameRoom(gameRoomId);
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

    private void movePlayer(int direction, GameRoom gameRoom) {
//        GameRoom gameRoom = gameRoomRedisService.getGameRoom(gameRoomId);
        FirstRoom firstRoom = gameRoom.getFirstRoom();
        SecondRoom secondRoom = gameRoom.getSecondRoom();
        ThirdRoom thirdRoom = gameRoom.getThirdRoom();

        System.out.println("firstRoom : " + firstRoom.getPlayer());
        System.out.println("secondRoom : " + secondRoom.getPlayer());
        System.out.println("thirdRoom : " + thirdRoom.getPlayer());

        // 정방향
        if (direction == 1) {
            String temp = thirdRoom.getPlayer();
            thirdRoom.setPlayer(secondRoom.getPlayer());
            secondRoom.setPlayer(firstRoom.getPlayer());
            firstRoom.setPlayer(temp);

            System.out.println("firstRoom : " + firstRoom.getPlayer());
            System.out.println("secondRoom : " + secondRoom.getPlayer());
            System.out.println("thirdRoom : " + thirdRoom.getPlayer());

            return;
        }

        // 역방향
        String temp = thirdRoom.getPlayer();
        thirdRoom.setPlayer(firstRoom.getPlayer());
        firstRoom.setPlayer(secondRoom.getPlayer());
        secondRoom.setPlayer(temp);

        System.out.println("firstRoom : " + firstRoom.getPlayer());
        System.out.println("secondRoom : " + secondRoom.getPlayer());
        System.out.println("thirdRoom : " + thirdRoom.getPlayer());
    }

    private void validateEvent(int eventIdx, GameRoom gameRoom) {
//        GameRoom gameRoom = gameRoomRedisService.getGameRoom(gameRoomId);
        FirstRoom firstRoom = gameRoom.getFirstRoom();
        SecondRoom secondRoom = gameRoom.getSecondRoom();
        ThirdRoom thirdRoom = gameRoom.getThirdRoom();

        switch (eventIdx) {
            case 0:
                thirdRoom.setAsteroidStatus(true);
                break;
            case 1:
                thirdRoom.setBlackholeStatus(true);
                break;
            case 2:
                if (secondRoom.getCarbonCaptureStatus() == 0) secondRoom.setCarbonCaptureStatus(1);
                break;
            case 3:
                firstRoom.setPurifierStatus(1);
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
            firstRoom.setMakeFertilizerTry(false);
            firstRoom.setFertilizerAmount(firstRoom.getFertilizerAmount() + fertilizer);
        }
        if (secondRoom.isMakeFertilizerTry()) {
            secondRoom.setMakeFertilizerTry(false);
            secondRoom.setFertilizerAmount(secondRoom.getFertilizerAmount() + fertilizer);
        }
        if (thirdRoom.isMakeFertilizerTry()) {
            thirdRoom.setMakeFertilizerTry(false);
            thirdRoom.setFertilizerAmount(thirdRoom.getFertilizerAmount() + fertilizer);
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
            firstRoom.setInputFertilizerTry(false);
            firstRoom.setFertilizerAmount(firstRoom.getFertilizerAmount() - 1);
            count++;
        }
        if (secondRoom.isInputFertilizerTry()) {
            secondRoom.setInputFertilizerTry(false);
            secondRoom.setFertilizerAmount(secondRoom.getFertilizerAmount() - 1);
            count++;
        }
        if (thirdRoom.isInputFertilizerTry()) {
            thirdRoom.setInputFertilizerTry(false);
            thirdRoom.setFertilizerAmount(thirdRoom.getFertilizerAmount() - 1);
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

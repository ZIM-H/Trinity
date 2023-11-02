package com.trinity.trinity.gameRoom.service;

import com.trinity.trinity.DTO.request.FirstRoomPlayerRequestDto;
import com.trinity.trinity.DTO.request.GameStartPlayerListRequestDto;
import com.trinity.trinity.DTO.request.SecondRoomPlayerRequestDto;
import com.trinity.trinity.DTO.request.ThridRoomPlayerRequestDto;
import com.trinity.trinity.gameRoom.dto.*;
import com.trinity.trinity.redisUtil.GameRoomRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GameRoomServiceImpl implements GameRoomService {

    private final GameRoomRedisService gameRoomRedisService;
    private final CreateService createService;

    @Override
    public String createGameRoom(List<GameStartPlayerListRequestDto> players) {
        String gameRoomId = UUID.randomUUID().toString();

        FirstRoom firstRoom = createService.createFirstRoom(players.get(0).getUserId());
        SecondRoom secondRoom = createService.createSecondRoom(players.get(1).getUserId());
        ThirdRoom thirdRoom = createService.createThirdRoom(players.get(1).getUserId());

        Round round = createService.createRound(firstRoom, secondRoom, thirdRoom);

        GameRoom gameRoom = GameRoom.builder()
                .gameRoomId(gameRoomId)
                .foodAmount(3)
                .fertilizerAmount(0)
                .roundNo(1)
                .round(round)
                .playerStatus(false)
                .birthday(false)
                .build();

        gameRoomRedisService.createGameRoom(gameRoom);

        return gameRoomId;
    }

    @Override
    public void updateFirstRoom(FirstRoomPlayerRequestDto firstRoomPlayerRequestDto) {
        // 게임방 가져오고
        GameRoom gameRoom = gameRoomRedisService.getGameRoom(firstRoomPlayerRequestDto.getGameRoomId());
        // 게임방 1번방 정보 넣기
        Round round = gameRoom.getRound();

        if (round == null) {
            gameRoom.setRound(new Round());
            round = gameRoom.getRound();
        }

        round.modifyFirstRoom(firstRoomPlayerRequestDto.getFirstRoom());

        gameRoomRedisService.saveGameRoomToTemp(gameRoom);
    }

    @Override
    public void updateSecondRoom(SecondRoomPlayerRequestDto secondRoomPlayerRequestDto) {
        GameRoom gameRoom = gameRoomRedisService.getGameRoom(secondRoomPlayerRequestDto.getGameRoomId());

        Round round = gameRoom.getRound();

        if (round == null) {
            gameRoom.setRound(new Round());
            round = gameRoom.getRound();
        }

        round.modifySecondRoom(secondRoomPlayerRequestDto.getSecondRoom());

        gameRoomRedisService.saveGameRoomToTemp(gameRoom);
    }

    @Override
    public void updateThridRoom(ThridRoomPlayerRequestDto thirdRoomPlayerRequestDto) {
        GameRoom gameRoom = gameRoomRedisService.getGameRoom(thirdRoomPlayerRequestDto.getGameRoomId());

        Round round = gameRoom.getRound();

        if (round == null) {
            gameRoom.setRound(new Round());
            round = gameRoom.getRound();
        }

        round.modifyThirdRoom(thirdRoomPlayerRequestDto.getThirdRoom());

        gameRoomRedisService.saveGameRoomToTemp(gameRoom);
    }

    @Override
    public boolean gameLogic(String gameRoomId) {
        GameRoom gameRoom = gameRoomRedisService.getGameRoom(gameRoomId);
        FirstRoom firstRoom = gameRoom.getRound().getFirstRoom();
        SecondRoom secondRoom = gameRoom.getRound().getSecondRoom();
        ThirdRoom thirdRoom = gameRoom.getRound().getThirdRoom();

        gameRoom.setFoodAmount(gameRoom.getFoodAmount() - 1);

        // 공중 정원
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

        if (gameRoom.getFoodAmount() == 0) return false;

        // 정수 시스템부터 ㄱㄱ
        if (firstRoom.getPurifierStatus() > 0) {
            firstRoom.setPurifierStatus(firstRoom.getPurifierStatus() + 1);
            if (firstRoom.isPurifierTry()) {
                firstRoom.setPurifierTry(false);
                firstRoom.setPurifierStatus(0);
                secondRoom.setTaurineFilterStatus(true);
            }

            if (firstRoom.getPurifierStatus() == 3) return false;

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
        if (thirdRoom.getBlackholeStatus() > 0) thirdRoom.setBlackholeStatus(thirdRoom.getBlackholeStatus() + 1);

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
            if (thirdRoom.getBarrierStatus() < 2) secondRoom.setFarmStatus(false);
            thirdRoom.setAsteroidStatus(false);
        }

        gameRoomRedisService.saveGameRoom(gameRoom);

        return true;
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

    private void makeFood(GameRoom gameRoom, FirstRoom firstRoom, SecondRoom secondRoom, ThirdRoom thirdRoom) {
        int count = 0;
        if (firstRoom.isInputFertilizerTry()) {
            firstRoom.setInputFertilizerTry(false);
            firstRoom.setFertilizerAmount(firstRoom.getFertilizerAmount() - 1);
            count++;
        }
        if (secondRoom.isInputFertilizerTry())  {
            secondRoom.setInputFertilizerTry(false);
            secondRoom.setFertilizerAmount(secondRoom.getFertilizerAmount() - 1);
            count++;
        }
        if (thirdRoom.isInputFertilizerTry())  {
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

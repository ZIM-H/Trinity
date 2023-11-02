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
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
            gameRoom.modifyRound(new Round());
            round = gameRoom.getRound();
        }

        round.modifyFirstRoom(firstRoomPlayerRequestDto.getFirstRoom());

        gameRoomRedisService.saveGameRoom(gameRoom);
    }

    @Override
    public void updateSecondRoom(SecondRoomPlayerRequestDto secondRoomPlayerRequestDto) {
        GameRoom gameRoom = gameRoomRedisService.getGameRoom(secondRoomPlayerRequestDto.getGameRoomId());

        Round round = gameRoom.getRound();

        if (round == null) {
            gameRoom.modifyRound(new Round());
            round = gameRoom.getRound();
        }

        round.modifySecondRoom(secondRoomPlayerRequestDto.getSecondRoom());

        gameRoomRedisService.saveGameRoom(gameRoom);
    }

    @Override
    public void updateThridRoom(ThridRoomPlayerRequestDto thirdRoomPlayerRequestDto) {
        GameRoom gameRoom = gameRoomRedisService.getGameRoom(thirdRoomPlayerRequestDto.getGameRoomId());

        Round round = gameRoom.getRound();

        if (round == null) {
            gameRoom.modifyRound(new Round());
            round = gameRoom.getRound();
        }

        round.modifyThirdRoom(thirdRoomPlayerRequestDto.getThirdRoom());

        gameRoomRedisService.saveGameRoom(gameRoom);
    }

    public void morningGameLogic(String gameRoomId){
        GameRoom gameRoom = gameRoomRedisService.getGameRoom(gameRoomId);
        FirstRoom firstRoom = gameRoom.getRound().getFirstRoom();
        SecondRoom secondRoom = gameRoom.getRound().getSecondRoom();
        ThirdRoom thirdRoom = gameRoom.getRound().getThirdRoom();


        // 블랙홀
        if(thirdRoom.getBlackholeStatus() == 3) {
            movePlayer(0, gameRoomId);
            thirdRoom.setBlackholeStatus(0);
            return;
        }

        movePlayer(1, gameRoomId);

        // 랜덤 이벤트 추출
        Random random = new Random();

        List<Integer> eventList = IntStream.range(0, 7)
                .boxed()
                .collect(Collectors.toList());

        while(eventList.size() > 0){
            int idx = random.nextInt(eventList.size());
            int eventIdx = eventList.get(idx);

            if(!vaildateEvent(eventIdx, gameRoomId)){
                eventList.remove(eventIdx);
            }
            else {
                gameRoom.setEvent(eventIdx);
                break;
            }
        }

        // 이산화탄소 고장 일수 2일차인지 판단
        if(secondRoom.getCarbonCaptureStatus() == 2) gameRoom.setCarbonCaptureNotice(true);

    }

    private void movePlayer(int direction, String gameRoomId) {
        GameRoom gameRoom = gameRoomRedisService.getGameRoom(gameRoomId);
        FirstRoom firstRoom = gameRoom.getRound().getFirstRoom();
        SecondRoom secondRoom = gameRoom.getRound().getSecondRoom();
        ThirdRoom thirdRoom = gameRoom.getRound().getThirdRoom();


        // 정방향
        if(direction == 1){
            String temp = thirdRoom.getPlayer();
            thirdRoom.setPlayer(secondRoom.getPlayer());
            secondRoom.setPlayer(firstRoom.getPlayer());
            firstRoom.setPlayer(temp);
            return;
        }

        // 역방향
        String temp = thirdRoom.getPlayer();
        thirdRoom.setPlayer(firstRoom.getPlayer());
        firstRoom.setPlayer(secondRoom.getPlayer());
        secondRoom.setPlayer(temp);

    }

    private boolean vaildateEvent(int eventIdx, String gameRoomId) {
        GameRoom gameRoom = gameRoomRedisService.getGameRoom(gameRoomId);
        FirstRoom firstRoom = gameRoom.getRound().getFirstRoom();
        SecondRoom secondRoom = gameRoom.getRound().getSecondRoom();
        ThirdRoom thirdRoom = gameRoom.getRound().getThirdRoom();

        switch (eventIdx) {
            case 0:
                if(!asteroidEvent(thirdRoom)) return false;
                break;
            case 1:
                if(!blackHoleEvent(thirdRoom)) return false;
                break;
            case 2:
                if(!carbonCaptureEvent(secondRoom)) return false;
                break;
            case 3:
                if(!purifierEvent(firstRoom)) return false;
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

        return true;
    }

    private boolean purifierEvent(FirstRoom firstRoom) {
        if (firstRoom.getPurifierStatus() > 0) return false;

        firstRoom.setPurifierStatus(1);
        return true;
    }

    private boolean carbonCaptureEvent(SecondRoom secondRoom) {
        if(secondRoom.getCarbonCaptureStatus() > 0)  return false;

        secondRoom.setCarbonCaptureStatus(1);
        return true;
    }

    private boolean blackHoleEvent(ThirdRoom thirdRoom) {
        if (thirdRoom.getBlackholeStatus() > 0) return false;

        thirdRoom.setBlackholeStatus(1);
        return true;
    }

    private boolean asteroidEvent(ThirdRoom thirdRoom) {
        if(thirdRoom.getBarrierStatus() == 2 || thirdRoom.isAsteroidStatus()) return false;

        thirdRoom.setAsteroidStatus(true);
        return true;
    }

}

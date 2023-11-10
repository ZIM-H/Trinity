package com.trinity.trinity.domain.logic.service;

import com.trinity.trinity.domain.control.dto.PlayerDto;
import com.trinity.trinity.domain.logic.dto.*;
import com.trinity.trinity.global.redis.service.GameRoomRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateService {

    private final GameRoomRedisService gameRoomRedisService;

    public GameRoom createGameRoom(List<PlayerDto> players) {
        String gameRoomId = UUID.randomUUID().toString();

        FirstRoom firstRoom = createFirstRoom(players.get(0).getUserId());
        SecondRoom secondRoom = createSecondRoom(players.get(1).getUserId());
        ThirdRoom thirdRoom = createThirdRoom(players.get(2).getUserId());

        Events events = Events.builder().build();
        events.shuffleEvent();

        GameRoom gameRoom = GameRoom.builder()
                .gameRoomId(gameRoomId)
                .foodAmount(3)
                .fertilizerAmount(0)
                .playerStatus(false)
                .birthday(false)
                .carbonCaptureNotice(false)
                .blackholeStatus(new boolean[13])
                .events(events)
                .roundNo(1)
                .firstRoom(firstRoom)
                .secondRoom(secondRoom)
                .thirdRoom(thirdRoom)
                .build();

        gameRoomRedisService.createGameRoom(gameRoom);

        return gameRoom;
    }

    public ThirdRoom createThirdRoom(String userId) {
        return ThirdRoom.builder()
                .fertilizerAmount(3)
                .userId(userId)
                .message("")
                .asteroidStatus(false)
                .blackholeStatus(false)
                .barrierStatus(0)
                .barrierDevTry(false)
                .developer("")
                .inputFertilizerTry(false)
                .makeFertilizerTry(false)
                .asteroidDestroyTry(false)
                .build();
    }

    public SecondRoom createSecondRoom(String userId) {
        return SecondRoom.builder()
                .fertilizerAmount(3)
                .userId(userId)
                .message("")
                .carbonCaptureStatus(0)
                .carbonCaptureTry(false)
                .carbonCaptureTryCount(0)
                .farmStatus(true)
                .farmTry(false)
                .taurineFilterTry(false)
                .taurineFilterStatus(true)
                .build();
    }

    public FirstRoom createFirstRoom(String userId) {
        return FirstRoom.builder()
                .fertilizerAmount(3)
                .userId(userId)
                .message("")
                .fertilizerUpgradeStatus(0)
                .fertilizerUpgradeTry(false)
                .purifierStatus(0)
                .purifierTry(false)
                .build();
    }
}

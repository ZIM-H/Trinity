package com.trinity.trinity.domain.logic.service;

import com.trinity.trinity.domain.logic.dto.FirstRoom;
import com.trinity.trinity.domain.logic.dto.SecondRoom;
import com.trinity.trinity.domain.logic.dto.ThirdRoom;
import org.springframework.stereotype.Service;

@Service
public class CreateService {
    public ThirdRoom createThirdRoom(String playerId) {
        return ThirdRoom.builder()
                .fertilizerAmount(3)
                .player(playerId)
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

    public SecondRoom createSecondRoom(String playerId) {
        return SecondRoom.builder()
                .fertilizerAmount(3)
                .player(playerId)
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

    public FirstRoom createFirstRoom(String playerId) {
        return FirstRoom.builder()
                .fertilizerAmount(3)
                .player(playerId)
                .message("")
                .fertilizerUpgradeStatus(0)
                .fertilizerUpgradeTry(false)
                .purifierStatus(0)
                .purifierTry(false)
                .build();
    }
}

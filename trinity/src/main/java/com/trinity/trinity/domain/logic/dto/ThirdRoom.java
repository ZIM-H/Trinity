package com.trinity.trinity.domain.logic.dto;

import com.trinity.trinity.domain.control.dto.request.ThirdRoomPlayerRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
//@Setter
@NoArgsConstructor
public class ThirdRoom {
    private int fertilizerAmount;
    private String userId;
    private String message;
    private boolean asteroidStatus;
    private boolean blackholeStatus;
    private int barrierStatus;
    private boolean barrierDevTry;
    private String developer;
    private boolean inputFertilizerTry;
    private boolean makeFertilizerTry;
    private boolean asteroidDestroyTry;

    @Builder
    public ThirdRoom(int fertilizerAmount, String userId, String message, boolean asteroidStatus, boolean blackholeStatus, int barrierStatus, boolean barrierDevTry, String developer, boolean inputFertilizerTry, boolean makeFertilizerTry, boolean asteroidDestroyTry) {
        this.fertilizerAmount = fertilizerAmount;
        this.userId = userId;
        this.message = message;
        this.asteroidStatus = asteroidStatus;
        this.blackholeStatus = blackholeStatus;
        this.barrierStatus = barrierStatus;
        this.barrierDevTry = barrierDevTry;
        this.developer = developer;
        this.inputFertilizerTry = inputFertilizerTry;
        this.makeFertilizerTry = makeFertilizerTry;
        this.asteroidDestroyTry = asteroidDestroyTry;
    }

    public static ThirdRoom toDto(ThirdRoomPlayerRequestDto dto) {
        return ThirdRoom.builder()
                .userId(dto.getUserId())
                .message(dto.getMessage())
                .inputFertilizerTry(dto.isInputFertilizerTry())
                .makeFertilizerTry(dto.isMakeFertilizerTry())
                .asteroidStatus(dto.isAsteroidStatus())
                .asteroidDestroyTry(dto.isAsteroidDestroyTry())
                .barrierDevTry(dto.isBarrierDevTry())
                .build();
    }

    public void modifyDto(ThirdRoom oldRoom) {
        this.fertilizerAmount = oldRoom.getFertilizerAmount();
        this.barrierStatus = oldRoom.getBarrierStatus();
        this.developer = oldRoom.getDeveloper();
        this.blackholeStatus = oldRoom.isBlackholeStatus();
    }

    public void modifyBarrierDevTry(boolean barrierDevTry) {
        this.barrierDevTry = barrierDevTry;
    }

    public void modifyBarrierStatus(int status) {
        this.barrierStatus = status;
    }

    public void modifyDeveloper(String developer) {
        this.developer = developer;
    }

    public void modifyAsteroidDestroyTry(boolean asteroidDestroyTry) {
        this.asteroidDestroyTry = asteroidDestroyTry;
    }

    public void modifyAsteroidStatus(boolean asteroidStatus) {
        this.asteroidStatus = asteroidStatus;
    }

    public void modifyBlackholeStatus(boolean blackholeStatus) {
        this.blackholeStatus = blackholeStatus;
    }

    public void modifyUserId(String userId) {
        this.userId = userId;
    }

    public void modifyMakeFertilizerTry(boolean makeFertilizerTry) {
        this.makeFertilizerTry = makeFertilizerTry;
    }

    public void modifyFertilizerAmount(int fertilizerAmount) {
        this.fertilizerAmount = fertilizerAmount;
    }

    public void modifyInputFertilizerTry(boolean inputFertilizerTry) {
        this.inputFertilizerTry = inputFertilizerTry;
    }
}

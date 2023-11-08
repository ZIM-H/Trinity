package com.trinity.trinity.domain.logic.dto;

import com.trinity.trinity.domain.control.dto.request.ThirdRoomPlayerRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ThirdRoom {
    private int fertilizerAmount;
    private String player;
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
    public ThirdRoom(int fertilizerAmount, String player, String message, boolean asteroidStatus, boolean blackholeStatus, int barrierStatus, boolean barrierDevTry, String developer, boolean inputFertilizerTry, boolean makeFertilizerTry, boolean asteroidDestroyTry) {
        this.fertilizerAmount = fertilizerAmount;
        this.player = player;
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
                .player(dto.getUserId())
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
}

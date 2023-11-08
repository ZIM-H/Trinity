package com.trinity.trinity.domain.control.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ThirdRoomPlayerRequestDto {
    private String userId;
    private String gameRoomId;
    private String roomNo;
    private String message;
    private boolean inputFertilizerTry;
    private boolean makeFertilizerTry;
    private boolean asteroidDestroyTry;
    private boolean barrierDevTry;
    private boolean asteroidStatus;
}

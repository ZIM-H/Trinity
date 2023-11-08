package com.trinity.trinity.domain.control.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SecondRoomPlayerRequestDto {
    private String userId;
    private String message;
    private String gameRoomId;
    private String roomNo;
    private boolean InputFertilizerTry;
    private boolean makeFertilizerTry;
    private boolean carbonCaptureTry;
    private boolean farmTry;
    private boolean taurineFilterTry;
}

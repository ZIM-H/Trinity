package com.trinity.trinity.DTO.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FirstRoomPlayerRequestDto {
    private String userId;
    private String gameRoomId;
    private String roomNo;
    private String message;
    private boolean inputFertilizerTry;
    private boolean makeFertilizerTry;
    private boolean fertilizerUpgradeTry;
    private boolean purifierTry;
}


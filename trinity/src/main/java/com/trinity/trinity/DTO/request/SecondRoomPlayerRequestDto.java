package com.trinity.trinity.DTO.request;

import com.trinity.trinity.gameRoom.dto.SecondRoom;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SecondRoomPlayerRequestDto {
    private String userId;
    private String gameRoomId;
    private SecondRoom secondRoom;
}

package com.trinity.trinity.DTO.request;

import com.trinity.trinity.gameRoom.dto.ThirdRoom;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ThridRoomPlayerRequestDto {
    private String userId;
    private String gameRoomId;
    private ThirdRoom thirdRoom;
}

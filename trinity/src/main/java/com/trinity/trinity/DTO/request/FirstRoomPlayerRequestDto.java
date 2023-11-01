package com.trinity.trinity.DTO.request;

import com.trinity.trinity.gameRoom.dto.FirstRoom;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FirstRoomPlayerRequestDto {
    private String userId;
    private String gameRoomId;
    private FirstRoom firstRoom;
}

package com.trinity.trinity.DTO.response;

import com.trinity.trinity.gameRoom.dto.FirstRoom;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FirstRoomPlayerResponseDto {
    private int roomNo;
    private FirstRoom firstRoom;
    private String event;

    @Builder
    public FirstRoomPlayerResponseDto(int roomNo, FirstRoom firstRoom, String event) {
        this.roomNo = roomNo;
        this.firstRoom = firstRoom;
        this.event = event;
    }
}

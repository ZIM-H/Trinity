package com.trinity.trinity.domain.control.dto.response;

import com.trinity.trinity.domain.logic.dto.FirstRoom;
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

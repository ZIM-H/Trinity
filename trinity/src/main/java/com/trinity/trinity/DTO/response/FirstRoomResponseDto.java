package com.trinity.trinity.DTO.response;

import com.trinity.trinity.gameRoom.dto.FirstRoom;
import com.trinity.trinity.gameRoom.dto.GameRoom;
import lombok.Builder;

public class FirstRoomResponseDto {
    private FirstRoom firstRoom;

    private String gameRoomId;
    private int fertilizerAmount;
    private boolean playerStatus;
    private boolean birthday;
    private int event;
    private boolean carbonCaptureNotice;

    @Builder
    public FirstRoomResponseDto(FirstRoom firstRoom, String gameRoomId, int fertilizerAmount, boolean playerStatus, boolean birthday, int event, boolean carbonCaptureNotice) {
        this.firstRoom = firstRoom;
        this.gameRoomId = gameRoomId;
        this.fertilizerAmount = fertilizerAmount;
        this.playerStatus = playerStatus;
        this.birthday = birthday;
        this.event = event;
        this.carbonCaptureNotice = carbonCaptureNotice;
    }

    public void modifyThirdRoomResponseDto(GameRoom gameRoom) {
        this.firstRoom = gameRoom.getRound().getFirstRoom();
        this.gameRoomId = gameRoom.getGameRoomId();
        this.fertilizerAmount = gameRoom.getFertilizerAmount();
        this.playerStatus = gameRoom.isPlayerStatus();
        this.birthday = gameRoom.isBirthday();
        this.event = gameRoom.getEvent();;
        this.carbonCaptureNotice = gameRoom.isCarbonCaptureNotice();
    }
}

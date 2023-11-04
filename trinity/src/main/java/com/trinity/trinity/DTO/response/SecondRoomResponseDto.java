package com.trinity.trinity.DTO.response;

import com.trinity.trinity.gameRoom.dto.GameRoom;
import com.trinity.trinity.gameRoom.dto.SecondRoom;
import lombok.Builder;

public class SecondRoomResponseDto {
    private SecondRoom secondRoom;

    private String gameRoomId;
    private int fertilizerAmount;
    private boolean playerStatus;
    private boolean birthday;
    private int event;
    private boolean carbonCaptureNotice;

    @Builder
    public SecondRoomResponseDto(SecondRoom secondRoom, String gameRoomId, int fertilizerAmount, boolean playerStatus, boolean birthday, int event, boolean carbonCaptureNotice) {
        this.secondRoom = secondRoom;
        this.gameRoomId = gameRoomId;
        this.fertilizerAmount = fertilizerAmount;
        this.playerStatus = playerStatus;
        this.birthday = birthday;
        this.event = event;
        this.carbonCaptureNotice = carbonCaptureNotice;
    }

    public void modifyThirdRoomResponseDto(GameRoom gameRoom) {
        this.secondRoom = gameRoom.getRound().getSecondRoom();
        this.gameRoomId = gameRoom.getGameRoomId();
        this.fertilizerAmount = gameRoom.getFertilizerAmount();
        this.playerStatus = gameRoom.isPlayerStatus();
        this.birthday = gameRoom.isBirthday();
        this.event = gameRoom.getEvent();;
        this.carbonCaptureNotice = gameRoom.isCarbonCaptureNotice();
    }
}

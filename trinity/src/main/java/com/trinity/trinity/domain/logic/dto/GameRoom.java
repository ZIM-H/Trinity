package com.trinity.trinity.domain.logic.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GameRoom {
    private String gameRoomId;
    private int foodAmount;
    private int fertilizerAmount;
    private boolean playerStatus;
    private boolean birthday;
    private int event;
    private boolean carbonCaptureNotice;
    private boolean[] blackholeStatus;
    private Events events;
    private int roundNo;
    private FirstRoom firstRoom;
    private SecondRoom secondRoom;
    private ThirdRoom thirdRoom;

    @Builder
    public GameRoom(String gameRoomId, int foodAmount, int fertilizerAmount, int roundNo, boolean playerStatus, boolean birthday, int event, boolean carbonCaptureNotice, boolean[] blackholeStatus, Events events, FirstRoom firstRoom, SecondRoom secondRoom, ThirdRoom thirdRoom) {
        this.gameRoomId = gameRoomId;
        this.foodAmount = foodAmount;
        this.fertilizerAmount = fertilizerAmount;
        this.roundNo = roundNo;
        this.playerStatus = playerStatus;
        this.birthday = birthday;
        this.event = event;
        this.carbonCaptureNotice = carbonCaptureNotice;
        this.blackholeStatus = blackholeStatus;
        this.events = events;
        this.firstRoom = firstRoom;
        this.secondRoom = secondRoom;
        this.thirdRoom = thirdRoom;
    }

    public void modifyFirstRoom(FirstRoom firstRoom) {
        this.firstRoom = firstRoom;
    }

    public void modifySecondRoom(SecondRoom secondRoom) {
        this.secondRoom = secondRoom;
    }

    public void modifyThirdRoom(ThirdRoom thirdRoom) {
        this.thirdRoom = thirdRoom;
    }

    public void modifyGameRound(int roundNo) {
        this.roundNo = roundNo;
    }

    public void modifyFoodAmount(int foodAmount) {
        this.foodAmount = foodAmount;
    }

    public void modifyCarbonCaptureNotice(boolean status) {
        this.carbonCaptureNotice = status;
    }

    public void modifyFertilizerAmount(int fertilizerAmount) {
        this.fertilizerAmount = fertilizerAmount;
    }

    public void modifyEvent(int event) {
        this.event = event;
    }

    public void modifyBirthday(boolean birthday) {
        this.birthday = birthday;
    }

    public void modifyPlayerStatus(boolean playerStatus) {
        this.playerStatus = playerStatus;
    }

    public void modifyGameRoomId(String gameRoomId) {
        this.gameRoomId = gameRoomId;
    }
}

package com.trinity.trinity.gameRoom.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Events {
    private int[] asteroid;
    private int[] blackhole;
    private int[] carbonCapture;
    private int[] purifier;
    private int[] sickness;
    private int[] birthday;

    @Builder
    public Events() {
        this.asteroid = new int[6];
        this.blackhole = new int[4];
        this.carbonCapture = new int[1];
        this.purifier = new int[1];
        this.sickness = new int[1];
        this.birthday = new int[1];
    }
}

package com.trinity.trinity.domain.logic.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

    public void shuffleEvent() {
        // 이벤트 등장 일수 랜덤 추출
        this.asteroid = shuffleArray(this.asteroid, 12);
        this.blackhole = shuffleArray(this.blackhole, 9);
        this.birthday = shuffleArray(this.birthday, 12);
        this.sickness = shuffleArray(this.sickness, 12);
        this.purifier = shuffleArray(this.purifier, 9);
        this.carbonCapture = shuffleArray(this.carbonCapture, 9);
    }

    private int[] shuffleArray(int[] array, int size) {
        Integer[] days = new Integer[size];

        for (int i = 0; i < size; i++) {
            days[i] = i + 1;
        }

        List<Integer> list = Arrays.asList(days);
        Collections.shuffle(list);

        for (int i = 0; i < array.length; i++) {
            array[i] = list.get(i);
        }

        return array;
    }

}

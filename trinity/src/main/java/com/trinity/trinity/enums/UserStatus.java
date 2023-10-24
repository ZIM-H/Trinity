package com.trinity.trinity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserStatus {
    LOBBY("로비"),
    WAITING("대기 중"),
    LOADING("로딩 중"),
    PLAYING("게임 중");

    private final String status;
}

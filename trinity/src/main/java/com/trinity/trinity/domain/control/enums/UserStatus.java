package com.trinity.trinity.domain.control.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserStatus {
    LOBBY("로비"),
    WAITING("대기 중"),
    PLAYING("게임 중");

    private final String status;
}

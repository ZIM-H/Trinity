package com.trinity.trinity.domain.control.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GameOverDto {
    private String type;
    private String status;
    private String reason;

    @Builder
    public GameOverDto(String status, String reason) {
        this.type = "gameOver";
        this.status = status;
        this.reason = reason;
    }
}

package com.trinity.trinity.domain.control.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GameOverDto {
    private String type;
    private String status;

    @Builder
    public GameOverDto(String status) {
        this.type = "gameOver";
        this.status = status;
    }
}

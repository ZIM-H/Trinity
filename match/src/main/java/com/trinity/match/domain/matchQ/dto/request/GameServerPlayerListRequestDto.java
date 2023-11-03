package com.trinity.match.domain.matchQ.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GameServerPlayerListRequestDto {
    private String userId;

    @Builder
    public GameServerPlayerListRequestDto(String userId) {
        this.userId = userId;
    }
}

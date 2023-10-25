package com.trinity.trinity.DTO.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserConnectResponse {
    private String userId;
    private String userStatus;

    @Builder
    public UserConnectResponse(String userId, String userStatus) {
        this.userId = userId;
        this.userStatus = userStatus;
    }

}

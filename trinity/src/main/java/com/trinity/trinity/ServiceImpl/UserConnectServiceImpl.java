package com.trinity.trinity.ServiceImpl;

import com.trinity.trinity.DTO.response.UserConnectResponse;
import com.trinity.trinity.DTO.response.UserMatchResponse;
import com.trinity.trinity.Service.UserConnectService;
import com.trinity.trinity.enums.UserStatus;
import com.trinity.trinity.redisUtil.RedisService;

import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

public class UserConnectServiceImpl implements UserConnectService {

    private RedisService redisService;
    @Override
    public UserConnectResponse connectToGameServer() {
        String userId = UUID.randomUUID().toString();
        redisService.saveData(userId, UserStatus.LOBBY.getStatus());

        return UserConnectResponse.builder()
                .userId(userId)
                .userStatus(UserStatus.LOBBY.getStatus())
                .build();
    }

    @Override
    public UserMatchResponse matchMakeing() {
        return null;
    }


}

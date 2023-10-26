package com.trinity.trinity.service;

import com.trinity.trinity.DTO.response.UserConnectResponse;
import com.trinity.trinity.DTO.response.UserMatchResponse;

public interface UserConnectService {
    UserConnectResponse connectToGameServer();

    UserMatchResponse matchMaking();
}

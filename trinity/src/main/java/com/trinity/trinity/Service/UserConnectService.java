package com.trinity.trinity.Service;

import com.trinity.trinity.DTO.response.UserConnectResponse;
import com.trinity.trinity.DTO.response.UserMatchResponse;

import java.util.Optional;

public interface UserConnectService {
    UserConnectResponse connectToGameServer();

    UserMatchResponse matchMakeing();
}

package com.trinity.trinity.controller;

import com.trinity.trinity.Service.UserConnectService;
import com.trinity.trinity.enums.UserStatus;
import com.trinity.trinity.redisUtil.RedisService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
@RequestMapping("/game")
public class UserConnectController {

    private UserConnectService userConnectService;

    @GetMapping({"", "/"})
    public ResponseEntity<Object> connectToGameServer() {
        return ResponseEntity.ok()
                .body(userConnectService.connectToGameServer());
    }

    @GetMapping("/match")
    public ResponseEntity<Object> matchMaking(@RequestParam String userId) {
        userConnectService.matchMakeing();
        return ResponseEntity.ok().build();
    }
}

package com.trinity.trinity.controller;

import com.trinity.trinity.service.UserConnectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/game")
public class UserConnectController {

    private final UserConnectService userConnectService;

    @GetMapping({"", "/"})
    public ResponseEntity<Object> connectToGameServer() {
        return ResponseEntity.ok()
                .body(userConnectService.connectToGameServer());
    }

    @GetMapping("/match")
    public ResponseEntity<Object> matchMaking(@RequestParam String userId) {
        userConnectService.matchMaking();
        return ResponseEntity.ok().build();
    }
}

package com.trinity.trinity.controller;

import com.trinity.trinity.DTO.request.GameStartPlayerListRequestDto;
import com.trinity.trinity.service.UserConnectService;
import com.trinity.trinity.webSocket.WebSocketFrameHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/game")
public class UserConnectController {

    private final UserConnectService userConnectService;
    private final WebSocketFrameHandler webSocketFrameHandler;

    @GetMapping({"", "/"})
    public ResponseEntity<Object> connectToGameServer() {
        return ResponseEntity.ok()
                .body(userConnectService.connectToGameServer());
    }

    @GetMapping("/match/{userId}")
    public ResponseEntity<Object> matchMaking(@Validated @PathVariable String userId) {
        userConnectService.matchMaking(userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/players")
    public ResponseEntity<Void> takePlayer(@Validated @RequestBody List<GameStartPlayerListRequestDto> players) {
        userConnectService.createGameRoom(players);
        return ResponseEntity.ok().build();
    }
}

package com.trinity.trinity.domain.control.api;

import com.trinity.trinity.domain.control.dto.PlayerDto;
import com.trinity.trinity.domain.control.service.GameConnectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/game")
public class GameConnectController {

    private final GameConnectService gameConnectService;

    @GetMapping({"", "/"})
    public ResponseEntity<?> connectToGameServer() {
        return ResponseEntity.ok()
                .body(gameConnectService.connectToGameServer());
    }

    @GetMapping("/match/{userId}")
    public ResponseEntity<?> matchMaking(@Validated @PathVariable String userId) {
        gameConnectService.matchMaking(userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/players")
    public ResponseEntity<?> takePlayer(@Validated @RequestBody List<PlayerDto> players) {
        gameConnectService.createGameRoom(players);
        return ResponseEntity.ok().build();
    }
}

package com.trinity.trinity.controller;

import com.trinity.trinity.service.UserConnectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

//    @PostMapping("/players")
//    public ResponseEntity<Void> takePlayer(@RequestBody List<Object> players) {
//        gameRoomService.makeGameRoom(players);
//        return ResponseEntity.ok().build();
//    }
}

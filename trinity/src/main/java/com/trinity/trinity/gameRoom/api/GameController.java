package com.trinity.trinity.gameRoom.api;

import com.trinity.trinity.DTO.request.FirstRoomPlayerRequestDto;
import com.trinity.trinity.DTO.request.GameServerPlayerListRequestDto;
import com.trinity.trinity.DTO.request.SecondRoomPlayerRequestDto;
import com.trinity.trinity.DTO.request.ThirdRoomPlayerRequestDto;
import com.trinity.trinity.gameRoom.service.GameRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class GameController {

    private final GameRoomService gameRoomService;
    @PostMapping("/first")
    public ResponseEntity<?> firstTestController(@Validated @RequestBody FirstRoomPlayerRequestDto firstRoomPlayerRequestDto) {
        gameRoomService.updateFirstRoom(firstRoomPlayerRequestDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/second")
    public ResponseEntity<?> secondTestController(@Validated @RequestBody SecondRoomPlayerRequestDto secondRoomPlayerRequestDto) {
        gameRoomService.updateSecondRoom(secondRoomPlayerRequestDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/third")
    public ResponseEntity<?> thirdTestController(@Validated @RequestBody ThirdRoomPlayerRequestDto thirdRoomPlayerRequestDto) {
        gameRoomService.updateThridRoom(thirdRoomPlayerRequestDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/start")
    public ResponseEntity<?> gameMatch(@Validated @RequestBody List<GameServerPlayerListRequestDto> players) {
        gameRoomService.createGameRoom(players);
        return ResponseEntity.ok().build();
    }
}

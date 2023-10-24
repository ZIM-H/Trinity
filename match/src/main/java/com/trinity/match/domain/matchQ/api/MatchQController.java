package com.trinity.match.domain.matchQ.api;

import com.trinity.match.domain.matchQ.service.MatchQServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MatchQController implements MatchQSwaggerController {

    private final MatchQServiceImpl matchQService;

    @Override
    public ResponseEntity<?> joinQueue(String userId) {

        return ResponseEntity.ok().body("sucess");
    }
}

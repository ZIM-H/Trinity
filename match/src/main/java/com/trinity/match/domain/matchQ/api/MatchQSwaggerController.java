package com.trinity.match.domain.matchQ.api;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/match")
public interface MatchQSwaggerController {

    @GetMapping("/join/{userId}")
    ResponseEntity<?> joinQueue(@Validated @PathVariable String userId);
}

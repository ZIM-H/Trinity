package com.trinity.match.domain.matchQ.service;

import org.springframework.transaction.annotation.Transactional;

public interface MatchQService {

    @Transactional
    void joinQueue(String userId);
}

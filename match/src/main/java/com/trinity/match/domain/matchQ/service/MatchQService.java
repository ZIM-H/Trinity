package com.trinity.match.domain.matchQ.service;

import org.springframework.data.util.Pair;

import java.util.List;

public interface MatchQService {

    boolean joinQueue(String userId);

    void recoverList(List<Pair<String, Double>> waitingList);
}

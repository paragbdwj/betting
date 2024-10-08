package com.elephants.betting.src.service;

import com.elephants.betting.src.request.MatchPageRequest;
import com.elephants.betting.src.response.MatchPageResponse;
import com.elephants.betting.src.response.MatchResultResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class MatchPageService {
    private final CricketExchangeService cricketExchangeService;
    private final Scheduler scheduler;
    public MatchPageResponse getMatchPage(MatchPageRequest request) throws IOException {
        return scheduler.getMatchIdToMatchPageResponse().getOrDefault(request.getMatchId(), MatchPageResponse.builder().build());
    }
}

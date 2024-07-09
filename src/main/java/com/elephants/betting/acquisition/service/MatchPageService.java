package com.elephants.betting.acquisition.service;

import com.elephants.betting.acquisition.request.MatchPageRequest;
import com.elephants.betting.acquisition.response.MatchPageResponse;
import com.elephants.betting.acquisition.response.MatchResultResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class MatchPageService {
    private final CricketExchange cricketExchange;
    public MatchPageResponse getMatchPage(MatchPageRequest request) throws IOException {
        MatchResultResponse matchResultResponse = cricketExchange.getMatchResult(request.getMatchResultRequest());

    }
}

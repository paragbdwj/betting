package com.elephants.betting.src.controller;

import com.elephants.betting.src.request.MatchPageRequest;
import com.elephants.betting.src.request.MatchResultRequest;
import com.elephants.betting.src.response.CricExchangeResponse;
import com.elephants.betting.src.response.CricExchangeResponse.CricExchangeAttributes;
import com.elephants.betting.src.response.MatchPageResponse;
import com.elephants.betting.src.response.MatchResultResponse;
import com.elephants.betting.src.service.CricketExchangeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


//TODO : remove the below controller
@Slf4j
@RestController
@RequiredArgsConstructor
public class TestController {
    private final CricketExchangeService cricketExchangeService;

    @GetMapping(value = "/test")
    public void f() {
        try {
            CricExchangeResponse response = cricketExchangeService.getCricExchangeResponse(null);
            log.info("cricExchange Response : {}", response);
            for(CricExchangeAttributes attributes : response.getMatches()) {
                try {
                    MatchPageResponse resultResponse = cricketExchangeService.getMatchResult(MatchPageRequest.builder().matchId(101).build());
                    log.info("for teams : {}  and {} got output : {}", attributes.getTeamOne(), attributes.getTeamTwo(), resultResponse);
                } catch (Exception e) {
                    log.error("Caught exception for : {} with stack_trace : {}", attributes, ExceptionUtils.getStackTrace(e));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

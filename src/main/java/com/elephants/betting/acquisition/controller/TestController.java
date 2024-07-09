package com.elephants.betting.acquisition.controller;

import com.elephants.betting.acquisition.request.CricExchangeRequest;
import com.elephants.betting.acquisition.request.MatchResultRequest;
import com.elephants.betting.acquisition.response.CricExchangeResponse;
import com.elephants.betting.acquisition.response.CricExchangeResponse.CricExchangeAttributes;
import com.elephants.betting.acquisition.response.MatchResultResponse;
import com.elephants.betting.acquisition.service.CricketExchange;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;


//TODO : remove the below controller
@Slf4j
@RestController
@RequiredArgsConstructor
public class TestController {
    private final CricketExchange cricketExchange;

    @GetMapping(value = "/test")
    public void f() {
        try {
            CricExchangeResponse response = cricketExchange.getCricExchangeResponse(null);
            log.info("cricExchange Response : {}", response);
            for(CricExchangeAttributes attributes : response.getMatches()) {
                try {
                    MatchResultResponse resultResponse = cricketExchange.getMatchResult(MatchResultRequest.builder().url(attributes.getHRef()).build());
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

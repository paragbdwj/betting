package com.elephants.betting.src.service;

import com.elephants.betting.common.helper.DatabaseHelper;
import com.elephants.betting.src.model.CricketMatches;
import com.elephants.betting.src.request.HomePageRequest;
import com.elephants.betting.src.request.MatchPageRequest;
import com.elephants.betting.src.response.HomePageResponse;
import com.elephants.betting.src.response.MatchPageResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@Service
public class Scheduler {
    @Getter
    private List<CricketMatches> matchesToShowOnHomePage;
    @Getter
    private Map<Integer, MatchPageResponse> matchIdToMatchPageResponse;
    private final HomePageService homePageService;
    private final CricketExchangeService cricketExchangeService;

    @Scheduled(fixedDelay = 1000)
    public void updateMatchMap() {
        HomePageResponse homePageResponse = null;
        try {
            homePageResponse = homePageService.getHomePage(HomePageRequest.builder().build());
        } catch (IOException e) {
            log.error("getting error in updateMatchMap");
            return;
        }
        this.matchesToShowOnHomePage = homePageResponse.getMatches();
        log.info("matchesToShowOnHomePage updated");
    }

    @Scheduled(fixedDelay = 1000)
    public void updateCricketOddStateManagement() {
        List<Integer> matchIds = matchesToShowOnHomePage.stream().map(CricketMatches::getMatchId).toList();
        matchIdToMatchPageResponse = matchIds.stream().map(matchId -> {
            try {
                return cricketExchangeService.getMatchResult(MatchPageRequest.builder().matchId(matchId).build());
            } catch (Exception e) {
                log.error("caught error for matchId : {}",matchId);
                return MatchPageResponse.builder().matchId(-1).build();
            }
        }).collect(Collectors.toMap(MatchPageResponse::getMatchId, matchPageResponse -> matchPageResponse, (k1, k2) -> k1));
    }

    @Scheduled(fixedDelay = 100)
    public void cricketMatchUpdate() {
    }
}

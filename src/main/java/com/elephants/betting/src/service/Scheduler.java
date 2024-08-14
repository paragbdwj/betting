package com.elephants.betting.src.service;

import com.elephants.betting.common.helper.DatabaseHelper;
import com.elephants.betting.src.model.CricketMatchOddState;
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
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
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
    private final DatabaseHelper databaseHelper;

    @Scheduled(fixedDelay = 100)
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

    @Scheduled(fixedDelay = 100)
    public void updateCricketOddStateManagement() {
        List<Integer> matchIds = matchesToShowOnHomePage.stream().map(CricketMatches::getMatchId).toList();
        matchIdToMatchPageResponse = matchIds.stream().map(matchId -> {
            try {
                return cricketExchangeService.getMatchResult(MatchPageRequest.builder().matchId(matchId).build());
            } catch (Exception e) {
                log.error("caught error for matchId : {}",matchId);
                return MatchPageResponse.builder().matchId(matchId).build();
            }
        }).collect(Collectors.toMap(MatchPageResponse::getMatchId, matchPageResponse -> matchPageResponse, (k1, k2) -> k1));
    }

    @Scheduled(fixedDelay = 100)
    public void cricketOddsRefreshUpdate() {
        List<Integer> matchIds = matchesToShowOnHomePage.stream().map(CricketMatches::getMatchId).toList();
        Map<Integer, CricketMatchOddState> matchIdToCricketMatchOddStateMap = databaseHelper.getAllCricketMoneyByMatchIdList(matchIds).stream().collect(Collectors.toMap(CricketMatchOddState::getMatchId, Function.identity(), (k1, k2) -> k2));
        List<CricketMatchOddState> cricketMatchOddStateList = new ArrayList<>();
        matchIdToMatchPageResponse.values().stream().forEach(matchPageResponse -> {
            Integer matchId = matchPageResponse.getMatchId();
            CricketMatchOddState newCricketMatchOddState = new CricketMatchOddState();
            String lastBallRun = CollectionUtils.isEmpty(matchPageResponse.getLastBallsResults()) ?"-1" : matchPageResponse.getLastBallsResults().get(0);
            if(matchIdToCricketMatchOddStateMap.containsKey(matchId)) {
                newCricketMatchOddState = matchIdToCricketMatchOddStateMap.get(matchId);
                if(!lastBallRun.equalsIgnoreCase(newCricketMatchOddState.getRunResult())) {
                    setState(newCricketMatchOddState, lastBallRun);
                }
            } else {
                setState(newCricketMatchOddState, lastBallRun);
                newCricketMatchOddState.setMatchId(matchId);
            }
            cricketMatchOddStateList.add(newCricketMatchOddState);
        });
        databaseHelper.saveAllCricketMatchStateOddManagement(cricketMatchOddStateList);
    }

    private void setState(CricketMatchOddState newCricketMatchOddState, String lastBallRun) {
        newCricketMatchOddState.setRunResult(lastBallRun);
        newCricketMatchOddState.setRunZeroMoney(1.0);
        newCricketMatchOddState.setRunOneMoney(1.0);
        newCricketMatchOddState.setRunTwoMoney(1.0);
        newCricketMatchOddState.setRunThreeMoney(1.0);
        newCricketMatchOddState.setRunFourMoney(1.0);
        newCricketMatchOddState.setRunFiveMoney(1.0);
        newCricketMatchOddState.setRunSixMoney(1.0);
        newCricketMatchOddState.setWicketMoney(1.0);
        newCricketMatchOddState.setUpdatedAt(LocalDateTime.now());
    }
}

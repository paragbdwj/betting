package com.elephants.betting.src.service;

import com.elephants.betting.common.helper.DatabaseHelper;
import com.elephants.betting.src.enums.WinningStatus;
import com.elephants.betting.src.model.CricketMatchOddState;
import com.elephants.betting.src.model.CricketMatches;
import com.elephants.betting.src.model.Payout;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@Service
public class Scheduler {
    @Getter
    private List<CricketMatches> matchesToShowOnHomePage = new ArrayList<>();
    @Getter
    private Map<Integer, MatchPageResponse> matchIdToMatchPageResponse = new HashMap<>();
    private final HomePageService homePageService;
    private final CricketExchangeService cricketExchangeService;
    private final DatabaseHelper databaseHelper;
    private final ExecutorService executorService = Executors.newFixedThreadPool(20);


    @Scheduled(fixedDelay = 60000)
    public void updateMatchMap() {
        HomePageResponse homePageResponse = null;
        try {
            homePageResponse = homePageService.getHomePage(HomePageRequest.builder().build());
        } catch (IOException e) {
            log.error("getting error in updateMatchMap");
            return;
        }
        this.matchesToShowOnHomePage = homePageResponse.getMatches();
        updateCricketMatchOddForNonLive();
        log.info("matchesToShowOnHomePage updated");
    }

    @Scheduled(fixedDelay = 1000)
    public void updateCricketOddStateManagementLive() {
        matchesToShowOnHomePage.stream()
                .filter(match -> Boolean.TRUE.equals(match.getIsLiveMatch()))
                .forEach(match -> {
                    CompletableFuture.supplyAsync(() -> updateMatchPageResponseMap(match.getMatchId()), executorService)
                            .thenAccept(matchPageResponse -> CompletableFuture.runAsync(() -> updateWinningStatusOfUser(matchPageResponse)));
                });
    }

    private void updateWinningStatusOfUser(MatchPageResponse matchPageResponse) {
        List<Payout> getPayoutListForWinningStatusInProgress = databaseHelper.findByWinningStatusAndMatchId(matchPageResponse.getMatchId(), WinningStatus.IN_PROGRESS.getWinningStatus());
        if(CollectionUtils.isEmpty(getPayoutListForWinningStatusInProgress)) {
            return;
        }
        databaseHelper.setWinningStatusBasisBallStatus(matchPageResponse, getPayoutListForWinningStatusInProgress);
    }

    private MatchPageResponse updateMatchPageResponseMap(int matchId) {
        MatchPageResponse matchPageResponse = MatchPageResponse.builder().matchId(matchId).build();
        try {
            matchPageResponse  = cricketExchangeService.getMatchResult(MatchPageRequest.builder().matchId(matchId).build());
            matchIdToMatchPageResponse.put(matchId, matchPageResponse);
        } catch (IOException e) {
            log.error("caught error for matchId : {}",matchId);
            matchIdToMatchPageResponse.put(matchId, matchPageResponse);
        }
        return matchPageResponse;
    }

    public void updateCricketMatchOddForNonLive() {
        for(CricketMatches cricketMatch : matchesToShowOnHomePage) {
            if(!cricketMatch.isUpdated() && Boolean.FALSE.equals(cricketMatch.getIsLiveMatch())) {
                cricketMatch.setUpdated(true);
                updateMatchPageResponseMap(cricketMatch.getMatchId());
            }
        }
    }

    @Scheduled(fixedDelay = 100)
    public void cricketOddsRefreshUpdate() {
        List<Integer> matchIds = matchesToShowOnHomePage.stream().filter(CricketMatches::getIsLiveMatch).map(CricketMatches::getMatchId).toList();
        Map<Integer, CricketMatchOddState> matchIdToCricketMatchOddStateMap = databaseHelper.getAllCricketMoneyByMatchIdList(matchIds).stream().collect(Collectors.toMap(CricketMatchOddState::getMatchId, Function.identity(), (k1, k2) -> k2));
        List<CricketMatchOddState> cricketMatchOddStateList = new ArrayList<>();
        matchIdToMatchPageResponse.values().forEach(matchPageResponse -> {
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

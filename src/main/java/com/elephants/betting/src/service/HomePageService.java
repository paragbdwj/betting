package com.elephants.betting.src.service;

import com.elephants.betting.src.exception.PayoutNotFoundException;
import com.elephants.betting.src.model.CricketMatches;
import com.elephants.betting.src.model.CricketMatchOddState;
import com.elephants.betting.src.request.CricExchangeRequest;
import com.elephants.betting.src.request.HomePageRequest;
import com.elephants.betting.src.response.CricExchangeResponse;
import com.elephants.betting.src.response.CricExchangeResponse.CricExchangeAttributes;
import com.elephants.betting.src.response.HomePageResponse;
import com.elephants.betting.common.helper.DatabaseHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class HomePageService {
    private final DatabaseHelper databaseHelper;
    private final CricketExchangeService cricketExchangeService;

    public HomePageResponse getHomePage(HomePageRequest request) throws PayoutNotFoundException, IOException {
        CricExchangeResponse cricExchangeResponse = cricketExchangeService.getCricExchangeResponse(new CricExchangeRequest());
        List<CricketMatches> matches = saveDetailsInDB(cricExchangeResponse);
        return createHomePageResponse(matches);
    }

//    private void saveInCricketMoneyDb(List<CricketMatches> matches) {
//        List<CricketMatchOddState> cricketMatchOddStateList = getCricketMoneyList(matches);
//        List<CricketMatchOddState> existingCricketMatchOddStateList = databaseHelper.getAllCricketMoneyByMatchIdList(cricketMatchOddStateList.stream().map(CricketMatchOddState::getMatchId).toList());
//        Map<Integer, CricketMatchOddState> matchIdToCricketMoneyMap = createMatchIdToCricketMoneyMap(existingCricketMatchOddStateList);
//        for(CricketMatchOddState cricketMatchOddState : cricketMatchOddStateList) {
//            if(matchIdToCricketMoneyMap.containsKey(cricketMatchOddState.getMatchId())) {
//                cricketMatchOddState.setId(matchIdToCricketMoneyMap.get(cricketMatchOddState.getMatchId()).getId());
//            } else {
//                // TODO : look into this below
//                cricketMatchOddState.setRunZeroMoney(1.0);
//                cricketMatchOddState.setRunOneMoney(1.0);
//                cricketMatchOddState.setRunTwoMoney(1.0);
//                cricketMatchOddState.setRunThreeMoney(1.0);
//                cricketMatchOddState.setRunFourMoney(1.0);
//                cricketMatchOddState.setRunFiveMoney(1.0);
//                cricketMatchOddState.setRunSixMoney(1.0);
//                cricketMatchOddState.setWicketMoney(1.0);
//            }
//        }
//        databaseHelper.saveAllCricketMoneies(cricketMatchOddStateList);
//    }

//    private Map<Integer, CricketMatchOddState> createMatchIdToCricketMoneyMap(List<CricketMatchOddState> existingCricketMatchOddStateList) {
//        Map<Integer, CricketMatchOddState> matchIdToCricketMoneyMap = new HashMap<>();
//        for(CricketMatchOddState cricketMatchOddState : existingCricketMatchOddStateList) {
//            matchIdToCricketMoneyMap.put(cricketMatchOddState.getMatchId(), cricketMatchOddState);
//        }
//        return matchIdToCricketMoneyMap;
//    }

//    private List<CricketMatchOddState> getCricketMoneyList(List<CricketMatches> matches) {
//        return matches.stream()
//                .map(match -> CricketMatchOddState.builder()
//                        .matchId(match.getMatchId())
//                        .build())
//                .toList();
//    }

    private List<CricketMatches> saveDetailsInDB(CricExchangeResponse cricExchangeResponse) {
        List<CricketMatches> cricketMatchesList = getCricketMatchesList(cricExchangeResponse.getMatches());
        List<CricketMatches> getExistingCricketMatches = databaseHelper.getCricketMatchesListByUrls(cricketMatchesList.stream().map(CricketMatches::getUrl).toList());
        Map<String, CricketMatches> urlToCricketMatchesMap = createUrlToCricketMatchesMap(getExistingCricketMatches);
        for(CricketMatches cricketMatches : cricketMatchesList) {
            if(urlToCricketMatchesMap.containsKey(cricketMatches.getUrl())) {
                cricketMatches.setMatchId(urlToCricketMatchesMap.get(cricketMatches.getUrl()).getMatchId());
            }
        }
        databaseHelper.saveAllCricketMatches(cricketMatchesList);
        return cricketMatchesList;
    }

    private Map<String, CricketMatches> createUrlToCricketMatchesMap(List<CricketMatches> cricketMatchesList) {
        Map<String, CricketMatches> urlToCricketMatchesMap = new HashMap<>();
        for(CricketMatches cricketMatches : cricketMatchesList) {
            urlToCricketMatchesMap.put(cricketMatches.getUrl(), cricketMatches);
        }
        return urlToCricketMatchesMap;
    }

    private List<CricketMatches> getCricketMatchesList(List<CricExchangeAttributes> matches) {
        return matches.stream()
                .map(cricExchangeAttributes ->
                        CricketMatches
                                .builder()
                                .url(cricExchangeAttributes.getUrl())
                                .isLiveMatch(cricExchangeAttributes.isLiveMatch())
                                .teamOne(cricExchangeAttributes.getTeamOne())
                                .teamTwo(cricExchangeAttributes.getTeamTwo())
                                .teamOneScore(cricExchangeAttributes.getTeamOneScore())
                                .teamTwoScore(cricExchangeAttributes.getTeamTwoScore())
                                .teamOneOvers(cricExchangeAttributes.getTeamOneOvers())
                                .teamTwoOvers(cricExchangeAttributes.getTeamTwoOvers())
                                .upcomingTime(cricExchangeAttributes.getUpcomingTime())
                                .currentTeam(cricExchangeAttributes.getCurrentTeam())
                                .teamOneFlag(cricExchangeAttributes.getTeamOneFlag())
                                .teamTwoFlag(cricExchangeAttributes.getTeamTwoFlag())
                                .isTestMatch(cricExchangeAttributes.isTestMatch())
                                .build()).toList();
    }

    private HomePageResponse createHomePageResponse(List<CricketMatches> matches) {
        return HomePageResponse.builder()
                .matches(matches)
                .build();
    }
}

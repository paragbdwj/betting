package com.elephants.betting.src.service;

import com.elephants.betting.src.exception.PayoutNotFoundException;
import com.elephants.betting.src.model.CricketMatches;
import com.elephants.betting.src.model.CricketMoney;
import com.elephants.betting.src.model.Payout;
import com.elephants.betting.src.model.User;
import com.elephants.betting.src.request.CricExchangeRequest;
import com.elephants.betting.src.request.HomePageRequest;
import com.elephants.betting.src.response.CricExchangeResponse;
import com.elephants.betting.src.response.CricExchangeResponse.CricExchangeAttributes;
import com.elephants.betting.src.response.HomePageResponse;
import com.elephants.betting.common.helper.DatabaseHelper;
import com.elephants.betting.src.response.HomePageResponse.UserData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class HomePageService {
    private final DatabaseHelper databaseHelper;
    private final CricketExchangeService cricketExchangeService;

    public HomePageResponse getHomePage(HomePageRequest request) throws PayoutNotFoundException, IOException {
        Payout payout = getPayoutDetails(request);
        User userDetails = getUserDetails(request);
        CricExchangeResponse cricExchangeResponse = cricketExchangeService.getCricExchangeResponse(new CricExchangeRequest());
        List<CricketMatches> matches = saveDetailsInDB(cricExchangeResponse);
        CompletableFuture.runAsync(() -> saveInCricketMoneyDb(matches));
        return createHomePageResponse(payout, userDetails, matches);
    }

    private void saveInCricketMoneyDb(List<CricketMatches> matches) {
        List<CricketMoney> cricketMoneyList = getCricketMoneyList(matches);
        List<CricketMoney> existingCricketMoneyList = databaseHelper.getAllCricketMoneyByMatchIdList(cricketMoneyList.stream().map(CricketMoney::getMatchId).toList());
        Map<Integer, CricketMoney> matchIdToCricketMoneyMap = createMatchIdToCricketMoneyMap(existingCricketMoneyList);
        for(CricketMoney cricketMoney : cricketMoneyList) {
            if(matchIdToCricketMoneyMap.containsKey(cricketMoney.getMatchId())) {
                cricketMoney.setId(matchIdToCricketMoneyMap.get(cricketMoney.getMatchId()).getId());
            }
        }
        databaseHelper.saveAllCricketMoneies(cricketMoneyList);
    }

    private Map<Integer, CricketMoney> createMatchIdToCricketMoneyMap(List<CricketMoney> existingCricketMoneyList) {
        Map<Integer, CricketMoney> matchIdToCricketMoneyMap = new HashMap<>();
        for(CricketMoney cricketMoney : existingCricketMoneyList) {
            matchIdToCricketMoneyMap.put(cricketMoney.getMatchId(), cricketMoney);
        }
        return matchIdToCricketMoneyMap;
    }

    private List<CricketMoney> getCricketMoneyList(List<CricketMatches> matches) {
        return matches.stream()
                .map(match -> CricketMoney.builder()
                        .matchId(match.getMatchId())
                        .wicketMoney(1.0)
                        .runZeroMoney(1.0)
                        .runOneMoney(1.0)
                        .runTwoMoney(1.0)
                        .runThreeMoney(1.0)
                        .runFourMoney(1.0)
                        .runFiveMoney(1.0)
                        .runSixMoney(1.0)
                        .build())
                .toList();
    }

    private List<CricketMatches> saveDetailsInDB(CricExchangeResponse cricExchangeResponse) {
        List<CricketMatches> cricketMatchesList = getCricketMatchesList(cricExchangeResponse.getMatches());
        List<CricketMatches> getExistingCricketMatches = databaseHelper.getCricketMatchesListByUrls(cricketMatchesList.stream().map(CricketMatches::getUrl).toList());
        Map<String, CricketMatches> urlToCricketMatchesMap = createUrlToCricketMatchesMap(getExistingCricketMatches);
        for(CricketMatches cricketMatches : cricketMatchesList) {
            if(urlToCricketMatchesMap.containsKey(cricketMatches.getUrl())) {
                cricketMatches.setMatchId(urlToCricketMatchesMap.get(cricketMatches.getUrl()).getMatchId());
            }
        }
        return databaseHelper.saveAllCricketMatches(cricketMatchesList);
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
                                .build()).toList();
    }

    private HomePageResponse createHomePageResponse(Payout payout, User userDetails, List<CricketMatches> matches) {
        return HomePageResponse.builder()
                .userData(UserData.builder()
                        .userMoney(payout.getTotalAmount())
                        .userName(userDetails.getUsername())
                        .build())
                .matches(matches)
                .build();
    }

    private User getUserDetails(HomePageRequest request) {
        return databaseHelper.getUserByUserId(request.getUserId());
    }

    private Payout getPayoutDetails(HomePageRequest request) {
        return databaseHelper.getPayoutByUserId(request.getUserId());
    }

}

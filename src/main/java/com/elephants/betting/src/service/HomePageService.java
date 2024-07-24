package com.elephants.betting.src.service;

import com.elephants.betting.src.exception.PayoutNotFoundException;
import com.elephants.betting.src.exception.UserNotFoundException;
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
import java.util.List;
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
        CricExchangeResponse cricketDetailsPojo = cricketExchangeService.getCricExchangeResponse(new CricExchangeRequest());
        List<CricketMatches> matches = saveDetailsInDB(cricketDetailsPojo);
        CompletableFuture.runAsync(() -> saveInCricketMoneyDb(matches));
        return createHomePageResponse(payout, userDetails, matches);
    }

    private void saveInCricketMoneyDb(List<CricketMatches> matches) {
        List<CricketMoney> cricketMoneyList = getCricketMoneyList(matches);
        databaseHelper.saveAllCricketMoneies(cricketMoneyList);
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

    private List<CricketMatches> saveDetailsInDB(CricExchangeResponse cricketDetailsPojo) {
        // TODO : ensure uniqueness of the matchId
        return databaseHelper.saveAllCricketMatches(getCricketMatchesList(cricketDetailsPojo.getMatches()));
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

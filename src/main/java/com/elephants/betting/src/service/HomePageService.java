package com.elephants.betting.src.service;

import com.elephants.betting.src.exception.PayoutNotFoundException;
import com.elephants.betting.src.exception.UserNotFoundException;
import com.elephants.betting.src.model.CricketMatches;
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
        return createHomePageResponse(payout, userDetails, matches);
    }

    private List<CricketMatches> saveDetailsInDB(CricExchangeResponse cricketDetailsPojo) {
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

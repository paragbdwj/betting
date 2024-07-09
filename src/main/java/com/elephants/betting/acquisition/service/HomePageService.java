package com.elephants.betting.acquisition.service;

import com.elephants.betting.acquisition.exception.PayoutNotFoundException;
import com.elephants.betting.acquisition.exception.UserNotFoundException;
import com.elephants.betting.acquisition.model.Payout;
import com.elephants.betting.acquisition.model.User;
import com.elephants.betting.acquisition.pojo.CricketDetailsPojo;
import com.elephants.betting.acquisition.request.HomePageRequest;
import com.elephants.betting.acquisition.response.HomePageResponse;
import com.elephants.betting.common.helper.CricketDetailsHelper;
import com.elephants.betting.common.helper.DatabaseHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class HomePageService {
    private final DatabaseHelper databaseHelper;
    private final CricketDetailsHelper cricketDetailsHelper;

    public HomePageResponse getHomePage(HomePageRequest request) throws PayoutNotFoundException{
        Payout payout = getPayoutDetails(request);
        User userDetails = getUserDetails(request);
        CricketDetailsPojo cricketDetailsPojo = cricketDetailsHelper.getCricketDetails(request);
        // TODO : complete this below
        return HomePageResponse.builder()
                .build();
    }

    private User getUserDetails(HomePageRequest request) {
        Optional<User> userOptional = databaseHelper.getUserByUserId(request.getUserId());
        if(userOptional.isEmpty()) {
            throw new UserNotFoundException("user details not found for userId : " + request.getUserId());
        }
        return userOptional.get();
    }

    private Payout getPayoutDetails(HomePageRequest request) {
        Optional<Payout> payoutOptional = databaseHelper.getPayoutByUserId(request.getUserId());
        if(payoutOptional.isEmpty()) {
            throw new PayoutNotFoundException("payout details not found for userId : " + request.getUserId());
        }
        return payoutOptional.get();
    }

}

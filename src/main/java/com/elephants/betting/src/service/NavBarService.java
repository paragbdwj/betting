package com.elephants.betting.src.service;

import com.elephants.betting.common.helper.DatabaseHelper;
import com.elephants.betting.src.exception.WrongCredentialsException;
import com.elephants.betting.src.model.Payout;
import com.elephants.betting.src.model.User;
import com.elephants.betting.src.request.GetHistoricalTransactionRequest;
import com.elephants.betting.src.request.UpdatePasswordRequest;
import com.elephants.betting.src.response.GetHistoricalTransactionsResponse;
import com.elephants.betting.src.response.UpdatePasswordResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class NavBarService {
    private final DatabaseHelper databaseHelper;

    public UpdatePasswordResponse updatePassword(UpdatePasswordRequest request) {
        User user = databaseHelper.getUserByUserId(request.getUserId());
        if(!request.getOldPassword().equals(user.getPassword())) {
            throw new WrongCredentialsException("current password entered wrong for userId : " + request.getUserId());
        }
        user.setPassword(request.getNewPassword());
        user.setUpdatedAt(LocalDateTime.now());
        databaseHelper.saveUser(user);
        return createUpdatePasswordResponse();
    }

    private UpdatePasswordResponse createUpdatePasswordResponse() {
        return UpdatePasswordResponse.builder()
                .success(true)
                .build();
    }

    public GetHistoricalTransactionsResponse getHistoricalTransactions(GetHistoricalTransactionRequest request) {
        List<Payout> payoutList = databaseHelper.getTopNTransactionsByUserId(request.getUserId(), 10);
        return GetHistoricalTransactionsResponse.builder()
                .success(true)
                .getHistoricalTransactionList(payoutList.stream().map(payout -> GetHistoricalTransactionsResponse.GetHistoricalTransactionPojo.builder()
                        .odd(payout.getOdds())
                        .moneyOnStake(payout.getMoneyOnStake())
                        .matchDetails(payout.getMatchDetails())
                        .date(payout.getCreatedAt())
                        .oddState(payout.getOddState())
                        .stateOfWinning(payout.getWinningStatus())
                        .build()).toList())
                .build();
    }
}

package com.elephants.betting.src.service;

import com.elephants.betting.common.helper.DatabaseHelper;
import com.elephants.betting.src.request.GetPayoutRequest;
import com.elephants.betting.src.response.GetPayoutResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PayoutService {
    private final DatabaseHelper databaseHelper;

    public GetPayoutResponse getPayout(GetPayoutRequest request) {
        return GetPayoutResponse.builder()
                .currentPayout(databaseHelper.getPayoutByUserId(request.getUserId()))
                .success(true)
                .build();
    }
}

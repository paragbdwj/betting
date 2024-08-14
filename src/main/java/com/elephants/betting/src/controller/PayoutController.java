package com.elephants.betting.src.controller;

import com.elephants.betting.common.utils.LogUtils;
import com.elephants.betting.src.request.GetPayoutRequest;
import com.elephants.betting.src.request.UpdateOddsRequest;
import com.elephants.betting.src.response.GetPayoutResponse;
import com.elephants.betting.src.response.UpdateOddsResponse;
import com.elephants.betting.src.service.PayoutService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.elephants.betting.common.constants.APIConstants.NavBarAPIs.GET_HISTORICAL_TRANSACTIONS;
import static com.elephants.betting.common.constants.APIConstants.OddsAPIs.UPDATE_ODDS;
import static com.elephants.betting.common.constants.APIConstants.UserAPIs.GET_PAYOUT_CONTROLLER;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PayoutController {

    /*
    razorpay integration should be done here
     */
    private final PayoutService payoutService;
    @PostMapping(value = GET_PAYOUT_CONTROLLER,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetPayoutResponse> getPayout(@RequestBody GetPayoutRequest request) {
        GetPayoutResponse getPayoutResponse = GetPayoutResponse.builder()
                .success(false)
                .build();
        try {
            LogUtils.getRequestLog(GET_PAYOUT_CONTROLLER, request);
            getPayoutResponse = payoutService.getPayout(request);
            LogUtils.getResponseLog(GET_PAYOUT_CONTROLLER, getPayoutResponse);
        } catch (Exception e) {
            LogUtils.getExceptionLog(GET_PAYOUT_CONTROLLER, request, e);
        }
        return ResponseEntity.status(200).body(getPayoutResponse);
    }
}

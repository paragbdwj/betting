package com.elephants.betting.src.controller;

import com.elephants.betting.common.utils.LogUtils;
import com.elephants.betting.src.exception.WrongCredentialsException;
import com.elephants.betting.src.request.GetHistoricalTransactionRequest;
import com.elephants.betting.src.request.UpdatePasswordRequest;
import com.elephants.betting.src.response.GetHistoricalTransactionsResponse;
import com.elephants.betting.src.response.UpdatePasswordResponse;
import com.elephants.betting.src.service.NavBarService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.elephants.betting.common.constants.APIConstants.NavBarAPIs.GET_HISTORICAL_TRANSACTIONS;
import static com.elephants.betting.common.constants.APIConstants.NavBarAPIs.UPDATE_PASSWORD_API;


@Slf4j
@RestController
@RequiredArgsConstructor
public class NavBarController {
    private final NavBarService navBarService;

    @PostMapping(value = UPDATE_PASSWORD_API,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UpdatePasswordResponse> updatePassword(@RequestBody UpdatePasswordRequest request) {
        UpdatePasswordResponse updatePasswordResponse = UpdatePasswordResponse.builder()
                .success(false)
                .build();
        HttpStatus httpStatusCode = HttpStatus.OK;
        try {
            LogUtils.getRequestLog(UPDATE_PASSWORD_API, request);
            updatePasswordResponse = navBarService.updatePassword(request);
            LogUtils.getResponseLog(UPDATE_PASSWORD_API, updatePasswordResponse);
        } catch (WrongCredentialsException e) {
            httpStatusCode = HttpStatus.OK;
            LogUtils.getWrongCredentialsExceptionLog(UPDATE_PASSWORD_API, request, e);
        } catch (Exception e) {
            httpStatusCode = HttpStatus.INTERNAL_SERVER_ERROR;
            LogUtils.getExceptionLog(UPDATE_PASSWORD_API, request, e);
        }
        return ResponseEntity.status(httpStatusCode.value()).body(updatePasswordResponse);
    }

    @PostMapping(value = GET_HISTORICAL_TRANSACTIONS,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetHistoricalTransactionsResponse> getHistoricalTransactions(@RequestBody GetHistoricalTransactionRequest request) {
        GetHistoricalTransactionsResponse response = GetHistoricalTransactionsResponse.builder()
                .success(false)
                .build();
        HttpStatus httpStatusCode = HttpStatus.OK;
        try {
            LogUtils.getRequestLog(GET_HISTORICAL_TRANSACTIONS, request);
            response = navBarService.getHistoricalTransactions(request);
            LogUtils.getResponseLog(GET_HISTORICAL_TRANSACTIONS, response);
        } catch (Exception e) {
            httpStatusCode = HttpStatus.INTERNAL_SERVER_ERROR;
            LogUtils.getExceptionLog(GET_HISTORICAL_TRANSACTIONS, request, e);
        }
        return ResponseEntity.status(httpStatusCode.value()).body(response);
    }
}

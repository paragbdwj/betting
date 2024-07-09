package com.elephants.betting.acquisition.controller;

import com.elephants.betting.acquisition.request.IsFirstTimeUserRequest;
import com.elephants.betting.acquisition.response.IsFirstTimeUserResponse;
import com.elephants.betting.acquisition.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.elephants.betting.common.constants.APIConstants.LoginAPIs.IS_FIRST_TIME_USER_API;
import static com.elephants.betting.common.utils.LogUtils.ErrorLogUtils.EXCEPTION_LOG;

//TODO : handle exceptions in each api
@RestController
@Slf4j
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;

    /*
        is_first_time_user api tells whether user is logging/signing in for first time or not
    */
    @PostMapping(value = IS_FIRST_TIME_USER_API,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IsFirstTimeUserResponse> isFirstTimeUser(IsFirstTimeUserRequest request) {
        //set default response accordingly
        IsFirstTimeUserResponse isFirstTimeUser = IsFirstTimeUserResponse.builder()
                .isValidUser(false)
                .isFirstTimeUser(false)
                .build();
        try {
            isFirstTimeUser = loginService.isFirstTimeUser(request);
        } catch (Exception e) {
            log.error(EXCEPTION_LOG, IS_FIRST_TIME_USER_API, ExceptionUtils.getStackTrace(e));
        }
        return ResponseEntity.status(200).body(isFirstTimeUser);
    }

}

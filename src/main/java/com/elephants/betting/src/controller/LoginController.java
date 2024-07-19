package com.elephants.betting.src.controller;

import com.elephants.betting.src.request.LoginUserRequest;
import com.elephants.betting.src.response.LoginVerifyUserResponse;
import com.elephants.betting.src.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.elephants.betting.common.constants.APIConstants.LoginAPIs.IS_VERIFIED_USER_API;
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
    @PostMapping(value = IS_VERIFIED_USER_API,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginVerifyUserResponse> isVerifiedUser(LoginUserRequest request) {
        //set default response accordingly
        LoginVerifyUserResponse isFirstTimeUser = LoginVerifyUserResponse.builder()
                .isValidUser(false)
                .build();
        try {
            isFirstTimeUser = loginService.loginVerifyUser(request);
        } catch (Exception e) {
            log.error(EXCEPTION_LOG, IS_VERIFIED_USER_API, ExceptionUtils.getStackTrace(e));
        }
        return ResponseEntity.status(200).body(isFirstTimeUser);
    }

}

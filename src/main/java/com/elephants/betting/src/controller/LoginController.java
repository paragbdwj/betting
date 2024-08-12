package com.elephants.betting.src.controller;

import com.elephants.betting.common.utils.LogUtils;
import com.elephants.betting.src.request.IsVerifiedUserRequest;
import com.elephants.betting.src.request.LoginUserRequest;
import com.elephants.betting.src.request.UserLogoutRequest;
import com.elephants.betting.src.response.IsVerifiedUserResponse;
import com.elephants.betting.src.response.LoginVerifyUserResponse;
import com.elephants.betting.src.response.UserLogoutResponse;
import com.elephants.betting.src.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.elephants.betting.common.constants.APIConstants.AdminAPIs.UPDATE_USER_DETAILS;
import static com.elephants.betting.common.constants.APIConstants.LoginAPIs.IS_LOGGED_IN;
import static com.elephants.betting.common.constants.APIConstants.LoginAPIs.IS_VERIFIED_USER_API;
import static com.elephants.betting.common.constants.APIConstants.LoginAPIs.USER_LOGOUT;

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
    public ResponseEntity<LoginVerifyUserResponse> isVerifiedUser(@RequestBody LoginUserRequest request) {
        //set default response accordingly
        LoginVerifyUserResponse loginVerifyUserResponse = LoginVerifyUserResponse.builder()
                .isValidUser(false)
                .build();
        try {
            LogUtils.getRequestLog(IS_VERIFIED_USER_API, request);
            loginVerifyUserResponse = loginService.loginVerifyUser(request);
            LogUtils.getResponseLog(IS_VERIFIED_USER_API, loginVerifyUserResponse);
        } catch (Exception e) {
            LogUtils.getExceptionLog(IS_VERIFIED_USER_API, request, e);
        }
        return ResponseEntity.status(200).body(loginVerifyUserResponse);
    }

    @PostMapping(value = IS_LOGGED_IN,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IsVerifiedUserResponse> isLoggedInUser(@RequestBody IsVerifiedUserRequest request) {
        //set default response accordingly
        IsVerifiedUserResponse isVerifiedUserResponse = IsVerifiedUserResponse.builder()
                .isLoggedIn(false)
                .build();
        try {
            LogUtils.getRequestLog(IS_LOGGED_IN, request);
            isVerifiedUserResponse = loginService.isLoggedInUser(request);
            LogUtils.getResponseLog(IS_LOGGED_IN, isVerifiedUserResponse);
        } catch (Exception e) {
            LogUtils.getExceptionLog(IS_LOGGED_IN, request, e);
        }
        return ResponseEntity.status(200).body(isVerifiedUserResponse);
    }

    @PostMapping(value = USER_LOGOUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserLogoutResponse> logoutUser(@RequestBody UserLogoutRequest request) {
        //set default response accordingly
        UserLogoutResponse userLogoutResponse = UserLogoutResponse.builder()
                .success(false)
                .build();
        try {
            LogUtils.getRequestLog(USER_LOGOUT, request);
            userLogoutResponse = loginService.logoutUser(request);
            LogUtils.getResponseLog(USER_LOGOUT, userLogoutResponse);
        } catch (Exception e) {
            LogUtils.getExceptionLog(USER_LOGOUT, request, e);
        }
        return ResponseEntity.status(200).body(userLogoutResponse);
    }

}

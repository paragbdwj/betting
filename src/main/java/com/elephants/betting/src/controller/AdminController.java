package com.elephants.betting.src.controller;

import com.elephants.betting.common.utils.LogUtils;
import com.elephants.betting.src.request.OnboardNewUserRequest;
import com.elephants.betting.src.request.UpdateUserDetailsRequest;
import com.elephants.betting.src.response.OnboardNewUserResponse;
import com.elephants.betting.src.response.UpdateUserDetailsResponse;
import com.elephants.betting.src.service.OnboardNewUserService;
import com.elephants.betting.src.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.elephants.betting.common.constants.APIConstants.AdminAPIs.ONBOARD_NEW_USER;
import static com.elephants.betting.common.constants.APIConstants.AdminAPIs.UPDATE_USER_DETAILS;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AdminController {

    private final OnboardNewUserService onboardNewUserService;
    private final UserService userService;
    @PostMapping(value = ONBOARD_NEW_USER,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OnboardNewUserResponse> onboardNewUser(@RequestBody OnboardNewUserRequest request) {
        OnboardNewUserResponse response = OnboardNewUserResponse.builder()
                .success(false)
                .build();
        try {
            LogUtils.getRequestLog(ONBOARD_NEW_USER, request);
            response = onboardNewUserService.onboardNewUser(request);
            LogUtils.getResponseLog(ONBOARD_NEW_USER, response);
        } catch (Exception e) {
            LogUtils.getExceptionLog(ONBOARD_NEW_USER, request, e);
        }
        return ResponseEntity.status(200).body(response);
    }

    @PostMapping(value = UPDATE_USER_DETAILS,
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UpdateUserDetailsResponse> updateUserDetails(@RequestBody UpdateUserDetailsRequest request) {
        UpdateUserDetailsResponse response = UpdateUserDetailsResponse.builder()
                .success(false)
                .build();
        try {
            LogUtils.getRequestLog(UPDATE_USER_DETAILS, request);
            response = userService.updateUserDetails(request);
            LogUtils.getResponseLog(UPDATE_USER_DETAILS, response);
        } catch (Exception e) {
            LogUtils.getExceptionLog(UPDATE_USER_DETAILS, request, e);
        }
        return ResponseEntity.status(200).body(response);
    }
}

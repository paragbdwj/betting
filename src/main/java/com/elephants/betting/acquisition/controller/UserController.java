package com.elephants.betting.acquisition.controller;

import com.elephants.betting.acquisition.request.UpdateUserRequest;
import com.elephants.betting.acquisition.response.UpdateUserResponse;
import com.elephants.betting.acquisition.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.elephants.betting.common.constants.APIConstants.UserAPIs.UPDATE_USER_DETAILS;
import static com.elephants.betting.common.utils.LogUtils.ErrorLogUtils.EXCEPTION_LOG;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;

    /*
    simple update user api to navigate through the database
    */
    @PostMapping(value = UPDATE_USER_DETAILS,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UpdateUserResponse> updateUser(UpdateUserRequest request) {
        // set default response to false
        UpdateUserResponse updateUserResponse = UpdateUserResponse.builder()
                .success(false)
                .build();
        try {
            updateUserResponse = userService.updateUser(request);
        } catch (Exception e) {
            log.error(EXCEPTION_LOG, UPDATE_USER_DETAILS, ExceptionUtils.getStackTrace(e));

        }
        return ResponseEntity.status(200).body(updateUserResponse);
    }
}

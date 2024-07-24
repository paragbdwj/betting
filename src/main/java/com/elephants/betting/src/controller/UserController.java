package com.elephants.betting.src.controller;

import com.elephants.betting.common.utils.LogUtils;
import com.elephants.betting.src.request.UpdateUserRequest;
import com.elephants.betting.src.response.UpdateUserResponse;
import com.elephants.betting.src.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.elephants.betting.common.constants.APIConstants.UserAPIs.UPDATE_USER;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;

    /*
    simple update user api to navigate through the database
    */
    @PostMapping(value = UPDATE_USER,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UpdateUserResponse> updateUser(@RequestBody UpdateUserRequest request) {
        // set default response to false
        UpdateUserResponse updateUserResponse = UpdateUserResponse.builder()
                .success(false)
                .build();
        try {
            LogUtils.getRequestLog(UPDATE_USER, request);
            updateUserResponse = userService.updateUser(request);
            LogUtils.getResponseLog(UPDATE_USER, updateUserResponse);
        } catch (Exception e) {
            LogUtils.getExceptionLog(UPDATE_USER, request, e);

        }
        return ResponseEntity.status(200).body(updateUserResponse);
    }
}

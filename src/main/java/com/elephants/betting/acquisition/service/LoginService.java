package com.elephants.betting.acquisition.service;

import com.elephants.betting.acquisition.model.User;
import com.elephants.betting.acquisition.request.IsFirstTimeUserRequest;
import com.elephants.betting.acquisition.response.IsFirstTimeUserResponse;
import com.elephants.betting.common.helper.DatabaseHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoginService {
    private final DatabaseHelper databaseHelper;
    public IsFirstTimeUserResponse isFirstTimeUser(IsFirstTimeUserRequest request) {
        // considering userName to be unique (check this in DB itself)
        Optional<User> user = databaseHelper.getUserByUserName(request.getUserName());
        if(user.isEmpty()) {
            return IsFirstTimeUserResponse.builder()
                    .isFirstTimeUser(false)
                    .isValidUser(false)
                    .build();
        }
        return IsFirstTimeUserResponse.builder()
                .isValidUser(user.get().getIsValid())
                .isFirstTimeUser(user.get().getIsFirstTimeUser())
                .build();
    }
}

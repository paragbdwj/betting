package com.elephants.betting.src.service;

import com.elephants.betting.src.model.User;
import com.elephants.betting.src.request.IsFirstTimeUserRequest;
import com.elephants.betting.src.response.IsFirstTimeUserResponse;
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
                    .isValidUser(false)
                    .build();
        }
        return IsFirstTimeUserResponse.builder()
                .isValidUser(user.get().getIsValid())
                .build();
    }
}

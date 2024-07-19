package com.elephants.betting.src.service;

import com.elephants.betting.src.model.User;
import com.elephants.betting.src.request.LoginUserRequest;
import com.elephants.betting.src.response.LoginVerifyUserResponse;
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
    public LoginVerifyUserResponse loginVerifyUser(LoginUserRequest request) {
        // considering userName to be unique (check this in DB itself)
        Optional<User> user = databaseHelper.getUserByUserName(request.getUserName());
        if(user.isEmpty()) {
            return LoginVerifyUserResponse.builder()
                    .isValidUser(false)
                    .userId(null)
                    .build();
        }
        return LoginVerifyUserResponse.builder()
                .isValidUser(user.get().getIsValid())
                .userId(user.get().getId())
                .build();
    }
}

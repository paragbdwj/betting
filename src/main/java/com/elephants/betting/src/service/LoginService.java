package com.elephants.betting.src.service;

import com.elephants.betting.src.model.User;
import com.elephants.betting.src.request.IsVerifiedUserRequest;
import com.elephants.betting.src.request.LoginUserRequest;
import com.elephants.betting.src.request.UserLogoutRequest;
import com.elephants.betting.src.response.IsVerifiedUserResponse;
import com.elephants.betting.src.response.LoginVerifyUserResponse;
import com.elephants.betting.common.helper.DatabaseHelper;
import com.elephants.betting.src.response.UserLogoutResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoginService {
    private final DatabaseHelper databaseHelper;
    public LoginVerifyUserResponse loginVerifyUser(LoginUserRequest request) {
        // considering userName to be unique (check this in DB itself)
        Optional<User> user = databaseHelper.getUserByUserName(request.getUserName());
        if(user.isEmpty() || !user.get().getIsValid() || !user.get().getPassword().equals(request.getPassword())) {
            return LoginVerifyUserResponse.builder()
                    .isValidUser(false)
                    .userId(null)
                    .build();
        }
        User userData = user.get();
        userData.setIsLoggedIn(true);
        databaseHelper.saveUser(userData);
        return LoginVerifyUserResponse.builder()
                .isValidUser(true)
                .userId(user.get().getId())
                .build();
    }

    public IsVerifiedUserResponse isLoggedInUser(IsVerifiedUserRequest request) throws BadRequestException {
        if(Objects.isNull(request.getUserId())) {
            throw new BadRequestException("userId can't be null");
        }
        User user = databaseHelper.getUserByUserId(request.getUserId());
        return IsVerifiedUserResponse.builder()
                .isLoggedIn(user.getIsLoggedIn())
                .build();
    }

    public UserLogoutResponse logoutUser(UserLogoutRequest request) throws BadRequestException {
        if(Objects.isNull(request.getUserId())) {
            throw new BadRequestException("userId can't be null");
        }
        User user = databaseHelper.getUserByUserId(request.getUserId());
        user.setIsLoggedIn(false);
        databaseHelper.saveUser(user);
        return UserLogoutResponse.builder()
                .success(true)
                .build();
    }
}

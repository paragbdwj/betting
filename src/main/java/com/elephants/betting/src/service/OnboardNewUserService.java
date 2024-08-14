package com.elephants.betting.src.service;

import com.elephants.betting.src.model.Payout;
import com.elephants.betting.src.model.User;
import com.elephants.betting.src.request.OnboardNewUserRequest;
import com.elephants.betting.src.response.OnboardNewUserResponse;
import com.elephants.betting.common.helper.DatabaseHelper;
import com.elephants.betting.common.utils.RandomUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class OnboardNewUserService {
    private final DatabaseHelper databaseHelper;
    /*
         1. This will return username and password
         2. user_name length should be greater than 4
     */
    public OnboardNewUserResponse onboardNewUser(OnboardNewUserRequest request) {
        User user = buildUser(request);
        user = databaseHelper.saveUser(user);
        setMoneyInPayout(user, request);
        return buildResponse(user);
    }

    private void setMoneyInPayout(User user, OnboardNewUserRequest request) {
        CompletableFuture.runAsync(() -> saveMoneyInPayout(user, request));
    }

    private void saveMoneyInPayout(User user, OnboardNewUserRequest request) {
        databaseHelper.savePayout(Payout.builder()
                        .userId(user.getId())
                        .totalAmount(request.getMoney())
                        .isOddTransaction(false)
                        .createdAt(LocalDateTime.now())
                .build());
    }

    private OnboardNewUserResponse buildResponse(User user) {
        return OnboardNewUserResponse.builder()
                .userName(user.getUsername())
                .password(user.getPassword())
                .success(true)
                .build();
    }

    private User buildUser(OnboardNewUserRequest request) {
        return User.builder()
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .isValid(true)
                .username(request.getName().substring(0, 4) + RandomUtils.generateRandomString(4))
                .password(RandomUtils.generateRandomString(12))
                .onboarderName(request.getOnboarderName())
                .name(request.getName())
                .build();
    }
}

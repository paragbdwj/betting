package com.elephants.betting.common.helper;

import com.elephants.betting.acquisition.model.Payout;
import com.elephants.betting.acquisition.model.User;
import com.elephants.betting.acquisition.repository.PayoutRepository;
import com.elephants.betting.acquisition.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class DatabaseHelper {
    private final UserRepository userRepository;
    private final PayoutRepository payoutRepository;

    public Optional<User> getUserByUserId(Integer userId) {
        return userRepository.findById(userId);
    }

    public Optional<User> getUserByUserName(String userName) {
        return userRepository.findByUsername(userName);
    }

    public Optional<Payout> getPayoutByUserId(Integer userId) {
        return payoutRepository.findByUserId(userId);
    }
}

package com.elephants.betting.src.service;

import com.elephants.betting.src.config.NullAwareBeanUtilsBean;
import com.elephants.betting.src.exception.PayoutNotFoundException;
import com.elephants.betting.src.model.Payout;
import com.elephants.betting.src.model.User;
import com.elephants.betting.src.request.UpdateUserDetailsRequest;
import com.elephants.betting.src.request.UpdateUserRequest;
import com.elephants.betting.src.response.UpdateUserDetailsResponse;
import com.elephants.betting.src.response.UpdateUserResponse;
import com.elephants.betting.common.helper.DatabaseHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final DatabaseHelper databaseHelper;
    public UpdateUserResponse updateUser(UpdateUserRequest request) {
        User existingUser = databaseHelper.getUserByUserId(request.getUser().getId());
        setUpdatedFieldsInExistingUser(existingUser, request);
        databaseHelper.saveUser(existingUser);
        return UpdateUserResponse.builder()
                .success(true)
                .build();
    }

    private void setUpdatedFieldsInExistingUser(User existingUser, UpdateUserRequest request) {
        // copy non-null properties from updated_user to existing_user
        try {
            BeanUtilsBean notNull = new NullAwareBeanUtilsBean();
            notNull.copyProperties(existingUser, request.getUser());
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Error updating user", e);
        }
        // update the updated_at field
        existingUser.setUpdatedAt(LocalDateTime.now());
    }

    public UpdateUserDetailsResponse updateUserDetails(UpdateUserDetailsRequest request) {
        Optional.ofNullable(request.getMoney()).ifPresent(money -> databaseHelper.updateMoneyInPayout(request.isAddition(), request.getUserId(), request.getMoney()));
        return UpdateUserDetailsResponse.builder()
                .success(true)
                .build();
    }


}

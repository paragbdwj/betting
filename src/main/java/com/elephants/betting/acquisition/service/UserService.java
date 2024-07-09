package com.elephants.betting.acquisition.service;

import com.elephants.betting.acquisition.config.NullAwareBeanUtilsBean;
import com.elephants.betting.acquisition.config.RestTemplateConfig;
import com.elephants.betting.acquisition.model.User;
import com.elephants.betting.acquisition.repository.UserRepository;
import com.elephants.betting.acquisition.request.UpdateUserRequest;
import com.elephants.betting.acquisition.response.UpdateUserResponse;
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

    private final UserRepository userRepository;
    private final RestTemplateConfig restTemplateConfig;

    public UpdateUserResponse updateUser(UpdateUserRequest request) {
        Optional<User> optionalUser = userRepository.findById(request.getUser().getId());
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();

            // copy non-null properties from updated_user to existing_user
            try {
                BeanUtilsBean notNull = new NullAwareBeanUtilsBean();
                notNull.copyProperties(existingUser, request.getUser());
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException("Error updating user", e);
            }

            // update the updated_at field
            existingUser.setUpdatedAt(LocalDateTime.now());

            userRepository.save(existingUser);

            return UpdateUserResponse.builder()
                    .success(true)
                    .build();

        }
        // send default unsuccessful response
        return UpdateUserResponse.builder()
                .success(false)
                .build();
    }
}

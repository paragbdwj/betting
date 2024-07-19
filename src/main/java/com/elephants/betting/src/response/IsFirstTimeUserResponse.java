package com.elephants.betting.src.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(SnakeCaseStrategy.class)
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class IsFirstTimeUserResponse {
//    private boolean isFirstTimeUser; // not necessary to build in MVP
    private boolean isValidUser;
    private Integer userId;
}

/*  user_name : abc
    password : 123

    not going with below approach asof now :-
    is_first_time : 0 and valid_user : 1  -> log in the user
    is_first_time : 1 and valid_user : 0  -> incorrect username/password
    is_first_time : 0 and valid_user : 0  -> incorrect username/password
    is_first_time : 1 and valid_user : 1  -> enter new password

    valid_user : 0 -> incorrect username/password
    valid_user : 1 -> cache xo and ox and redirect to next page
 */
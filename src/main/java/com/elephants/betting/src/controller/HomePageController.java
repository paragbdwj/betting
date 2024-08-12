package com.elephants.betting.src.controller;

import com.elephants.betting.common.utils.LogUtils;
import com.elephants.betting.src.request.HomePageRequest;
import com.elephants.betting.src.response.HomePageResponse;
import com.elephants.betting.src.service.HomePageService;
import com.elephants.betting.src.service.HomePageServiceNew;
import com.mysql.cj.log.Log;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.elephants.betting.common.constants.APIConstants.AdminAPIs.UPDATE_USER_DETAILS;
import static com.elephants.betting.common.constants.APIConstants.PageAPIs.GET_HOME_PAGE;


@Slf4j
@RestController
@RequiredArgsConstructor
public class HomePageController {
    private final HomePageServiceNew homePageService;

    /*
       this api tells the money user have in his account currently and the matches that are currently happening
    */
    @PostMapping(value = GET_HOME_PAGE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HomePageResponse> getHomePage(@RequestBody HomePageRequest request) {
        HomePageResponse homePageResponse = null;
        try {
            LogUtils.getRequestLog(GET_HOME_PAGE, request);
            homePageResponse = homePageService.getHomePageAPI(request);
            LogUtils.getResponseLog(GET_HOME_PAGE, homePageResponse);
        } catch (Exception e) {
            LogUtils.getExceptionLog(GET_HOME_PAGE, request, e);
        }
        return ResponseEntity.status(200).body(homePageResponse);
    }

}

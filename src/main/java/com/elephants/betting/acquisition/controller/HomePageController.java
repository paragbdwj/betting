package com.elephants.betting.acquisition.controller;

import com.elephants.betting.acquisition.request.HomePageRequest;
import com.elephants.betting.acquisition.response.HomePageResponse;
import com.elephants.betting.acquisition.service.HomePageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.elephants.betting.common.constants.APIConstants.PageAPIs.GET_HOME_PAGE;
import static com.elephants.betting.common.utils.LogUtils.ErrorLogUtils.EXCEPTION_LOG;


@Slf4j
@RestController
@RequiredArgsConstructor
public class HomePageController {
    private final HomePageService homePageService;

    /*
       this api tells the money user have in his account currently and the matches that are currently happening
    */
    @PostMapping(value = GET_HOME_PAGE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HomePageResponse> getHomePage(HomePageRequest request) {
        HomePageResponse homePageResponse = null;
        try {
            homePageResponse = homePageService.getHomePage(request);
        } catch (Exception e) {
            log.error(EXCEPTION_LOG, GET_HOME_PAGE, ExceptionUtils.getStackTrace(e));
        }
        return ResponseEntity.status(200).body(homePageResponse);
    }

}

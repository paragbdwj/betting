package com.elephants.betting.src.controller;

import com.elephants.betting.common.utils.LogUtils;
import com.elephants.betting.src.request.MatchPageRequest;
import com.elephants.betting.src.response.MatchPageResponse;
import com.elephants.betting.src.service.MatchPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.elephants.betting.common.constants.APIConstants.PageAPIs.GET_MATCH_PAGE;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MatchPageController {
    private final MatchPageService matchPageService;

    @PostMapping(value = GET_MATCH_PAGE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MatchPageResponse> getMatchPage(@RequestBody MatchPageRequest request) {
        MatchPageResponse matchPageResponse = null;
        try {
            LogUtils.getRequestLog(GET_MATCH_PAGE, request);
            matchPageResponse = matchPageService.getMatchPage(request);
            LogUtils.getResponseLog(GET_MATCH_PAGE, matchPageResponse);
        } catch (Exception e) {
            LogUtils.getExceptionLog(GET_MATCH_PAGE, request, e);
        }
        return ResponseEntity.status(200).body(matchPageResponse);
    }
}

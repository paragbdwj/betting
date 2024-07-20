package com.elephants.betting.src.controller;

import com.elephants.betting.src.request.MatchPageRequest;
import com.elephants.betting.src.response.MatchPageResponse;
import com.elephants.betting.src.service.MatchPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.elephants.betting.common.constants.APIConstants.PageAPIs.GET_MATCH_PAGE;
import static com.elephants.betting.common.utils.LogUtils.ErrorLogUtils.EXCEPTION_LOG;

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
            matchPageResponse = matchPageService.getMatchPage(request);
        } catch (Exception e) {
            log.error(EXCEPTION_LOG, GET_MATCH_PAGE, ExceptionUtils.getStackTrace(e));
        }
        return ResponseEntity.status(200).body(matchPageResponse);
    }
}

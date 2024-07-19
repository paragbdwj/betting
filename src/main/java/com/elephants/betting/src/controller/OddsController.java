package com.elephants.betting.src.controller;

import com.elephants.betting.src.request.GiveOddsRequest;
import com.elephants.betting.src.request.UpdateOddsRequest;
import com.elephants.betting.src.response.GiveOddsResponse;
import com.elephants.betting.src.response.UpdateOddsResponse;
import com.elephants.betting.src.service.OddsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.elephants.betting.common.constants.APIConstants.OddsAPIs.GIVE_ODDS;
import static com.elephants.betting.common.constants.APIConstants.OddsAPIs.UPDATE_ODDS;
import static com.elephants.betting.common.utils.LogUtils.ErrorLogUtils.EXCEPTION_LOG;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OddsController {
    private final OddsService oddsService;

    @PostMapping(value = GIVE_ODDS,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GiveOddsResponse> giveOdds(GiveOddsRequest request) {
        GiveOddsResponse giveOddsResponse = null;
        try {
            giveOddsResponse = oddsService.giveOdds(request);
        } catch (Exception e) {
            log.error(EXCEPTION_LOG, GIVE_ODDS, ExceptionUtils.getStackTrace(e));
        }
        return ResponseEntity.status(200).body(giveOddsResponse);
    }

    @PostMapping(value = UPDATE_ODDS,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UpdateOddsResponse> updateOdds(UpdateOddsRequest request) {
        UpdateOddsResponse updateOddsResponse = null;
        try {
            updateOddsResponse = oddsService.updateOdds(request);
        } catch (Exception e) {
            log.error(EXCEPTION_LOG, UPDATE_ODDS, ExceptionUtils.getStackTrace(e));
        }
        return ResponseEntity.status(200).body(updateOddsResponse);
    }

}

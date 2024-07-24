package com.elephants.betting.src.controller;

import com.elephants.betting.common.utils.LogUtils;
import com.elephants.betting.src.request.GiveOddsRequest;
import com.elephants.betting.src.request.UpdateOddsRequest;
import com.elephants.betting.src.response.GiveOddsResponse;
import com.elephants.betting.src.response.UpdateOddsResponse;
import com.elephants.betting.src.service.OddsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.elephants.betting.common.constants.APIConstants.OddsAPIs.GIVE_ODDS;
import static com.elephants.betting.common.constants.APIConstants.OddsAPIs.UPDATE_ODDS;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OddsController {
    private final OddsService oddsService;

    @PostMapping(value = GIVE_ODDS,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GiveOddsResponse> giveOdds(@RequestBody GiveOddsRequest request) {
        GiveOddsResponse giveOddsResponse = null;
        try {
            LogUtils.getRequestLog(GIVE_ODDS, request);
            giveOddsResponse = oddsService.giveOdds(request);
            LogUtils.getResponseLog(GIVE_ODDS, giveOddsResponse);
        } catch (Exception e) {
            LogUtils.getExceptionLog(GIVE_ODDS, request, e);
        }
        return ResponseEntity.status(200).body(giveOddsResponse);
    }

    @PostMapping(value = UPDATE_ODDS,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UpdateOddsResponse> updateOdds(@RequestBody UpdateOddsRequest request) {
        UpdateOddsResponse updateOddsResponse = null;
        try {
            LogUtils.getRequestLog(UPDATE_ODDS, request);
            updateOddsResponse = oddsService.updateOdds(request);
            LogUtils.getResponseLog(UPDATE_ODDS, updateOddsResponse);
        } catch (Exception e) {
            LogUtils.getExceptionLog(UPDATE_ODDS, request, e);
        }
        return ResponseEntity.status(200).body(updateOddsResponse);
    }

}

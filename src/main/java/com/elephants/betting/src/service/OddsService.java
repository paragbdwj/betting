package com.elephants.betting.src.service;

import com.elephants.betting.common.constants.ApplicationProperties;
import com.elephants.betting.common.helper.DatabaseHelper;
import com.elephants.betting.src.exception.CricketOddsNotFoundException;
import com.elephants.betting.src.model.CricketMoney;
import com.elephants.betting.src.model.Payout;
import com.elephants.betting.src.model.User;
import com.elephants.betting.src.request.GiveOddsRequest;
import com.elephants.betting.src.request.OnboardNewUserRequest;
import com.elephants.betting.src.request.UpdateOddsRequest;
import com.elephants.betting.src.response.GiveOddsResponse;
import com.elephants.betting.src.response.UpdateOddsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class OddsService {
    private final DatabaseHelper databaseHelper;
    private final ApplicationProperties applicationProperties;
    public GiveOddsResponse giveOdds(GiveOddsRequest request) {
        CricketMoney cricketMoney = databaseHelper.getOddsCricketByMatchId(request.getMatchId());
        return calculateOddsBasisMoney(cricketMoney);
    }

    private GiveOddsResponse calculateOddsBasisMoney(CricketMoney cricketMoney) {
        double totalMoneyAfterCut = getTotalMoney(cricketMoney) * (1 - applicationProperties.getInHouseCutPercentage());
        return GiveOddsResponse.builder()
                .runZeroOdds(totalMoneyAfterCut/cricketMoney.getRunZeroMoney())
                .runOneOdds(totalMoneyAfterCut/cricketMoney.getRunOneMoney())
                .runTwoOdds(totalMoneyAfterCut/cricketMoney.getRunTwoMoney())
                .runThreeOdds(totalMoneyAfterCut/cricketMoney.getRunThreeMoney())
                .runFourOdds(totalMoneyAfterCut/cricketMoney.getRunFourMoney())
                .runFiveOdds(totalMoneyAfterCut/cricketMoney.getRunFiveMoney())
                .runSixOdds(totalMoneyAfterCut/cricketMoney.getRunSixMoney())
                .wicketOdds(totalMoneyAfterCut/cricketMoney.getWicketMoney())
                .build();
    }

    private double getTotalMoney(CricketMoney cricketMoney) {
        return cricketMoney.getRunOneMoney()
                + cricketMoney.getRunTwoMoney()
                + cricketMoney.getRunThreeMoney()
                + cricketMoney.getRunFourMoney()
                + cricketMoney.getRunFiveMoney()
                + cricketMoney.getRunSixMoney()
                + cricketMoney.getWicketMoney()
                + cricketMoney.getRunZeroMoney();
    }

    public UpdateOddsResponse updateOdds(UpdateOddsRequest request) {
        Payout payout = databaseHelper.updateMoneyInPayout(false, request.getUserId(), request.getUserMoney());
        CricketMoney cricketMoney = databaseHelper.updateOddMoneyInDatabaseBasisStateName(true, request.getMatchId(), request.getStateName(), request.getUserMoney());
        return UpdateOddsResponse.builder()
                .oddsResponse(calculateOddsBasisMoney(cricketMoney))
                .userMoney(payout.getTotalAmount())
                .build();
    }

}

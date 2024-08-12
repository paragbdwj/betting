package com.elephants.betting.src.service;

import com.elephants.betting.common.constants.ApplicationProperties;
import com.elephants.betting.common.helper.DatabaseHelper;
import com.elephants.betting.common.utils.MathUtils;
import com.elephants.betting.src.model.CricketMatchOddState;
import com.elephants.betting.src.model.Payout;
import com.elephants.betting.src.request.GiveOddsRequest;
import com.elephants.betting.src.request.UpdateOddsRequest;
import com.elephants.betting.src.response.GiveOddsResponse;
import com.elephants.betting.src.response.UpdateOddsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OddsService {
    private final DatabaseHelper databaseHelper;
    private final ApplicationProperties applicationProperties;
    public GiveOddsResponse giveOdds(GiveOddsRequest request) {
        CricketMatchOddState cricketMatchOddState = databaseHelper.getOddsCricketByMatchId(request.getMatchId());
        return calculateOddsBasisMoney(cricketMatchOddState);
    }

    private GiveOddsResponse calculateOddsBasisMoney(CricketMatchOddState cricketMatchOddState) {
        double totalMoneyAfterCut = getTotalMoney(cricketMatchOddState) * (1 - applicationProperties.getInHouseCutPercentage());
        return GiveOddsResponse.builder()
                .runZeroOdds(MathUtils.roundOffToTwoDecimalPlaces(totalMoneyAfterCut/ cricketMatchOddState.getRunZeroMoney()))
                .runOneOdds(MathUtils.roundOffToTwoDecimalPlaces(totalMoneyAfterCut/ cricketMatchOddState.getRunOneMoney()))
                .runTwoOdds(MathUtils.roundOffToTwoDecimalPlaces(totalMoneyAfterCut/ cricketMatchOddState.getRunTwoMoney()))
                .runThreeOdds(MathUtils.roundOffToTwoDecimalPlaces(totalMoneyAfterCut/ cricketMatchOddState.getRunThreeMoney()))
                .runFourOdds(MathUtils.roundOffToTwoDecimalPlaces(totalMoneyAfterCut/ cricketMatchOddState.getRunFourMoney()))
                .runFiveOdds(MathUtils.roundOffToTwoDecimalPlaces(totalMoneyAfterCut/ cricketMatchOddState.getRunFiveMoney()))
                .runSixOdds(MathUtils.roundOffToTwoDecimalPlaces(totalMoneyAfterCut/ cricketMatchOddState.getRunSixMoney()))
                .wicketOdds(MathUtils.roundOffToTwoDecimalPlaces(totalMoneyAfterCut/ cricketMatchOddState.getWicketMoney()))
                .build();
    }

    private double getTotalMoney(CricketMatchOddState cricketMatchOddState) {
        return cricketMatchOddState.getRunOneMoney()
                + cricketMatchOddState.getRunTwoMoney()
                + cricketMatchOddState.getRunThreeMoney()
                + cricketMatchOddState.getRunFourMoney()
                + cricketMatchOddState.getRunFiveMoney()
                + cricketMatchOddState.getRunSixMoney()
                + cricketMatchOddState.getWicketMoney()
                + cricketMatchOddState.getRunZeroMoney();
    }

    public UpdateOddsResponse updateOdds(UpdateOddsRequest request) {
        Payout payout = databaseHelper.updateMoneyInPayoutBasisRequest(false, request);
        CricketMatchOddState cricketMatchOddState = databaseHelper.updateOddMoneyInDatabaseBasisStateName(true, request.getMatchId(), request.getStateName(), request.getMoneyOnStake());
        return UpdateOddsResponse.builder()
                .oddsResponse(calculateOddsBasisMoney(cricketMatchOddState))
                .userMoney(payout.getTotalAmount())
                .success(true)
                .build();
    }

}

package com.elephants.betting.common.helper;

import com.elephants.betting.src.enums.StateName;
import com.elephants.betting.src.exception.CricketOddsNotFoundException;
import com.elephants.betting.src.exception.PayoutNotFoundException;
import com.elephants.betting.src.exception.UserNotFoundException;
import com.elephants.betting.src.model.CricketMatches;
import com.elephants.betting.src.model.CricketMoney;
import com.elephants.betting.src.model.Payout;
import com.elephants.betting.src.model.User;
import com.elephants.betting.src.repository.CricketMatchesRepository;
import com.elephants.betting.src.repository.CricketMoneyRepository;
import com.elephants.betting.src.repository.PayoutRepository;
import com.elephants.betting.src.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class DatabaseHelper {
    private final UserRepository userRepository;
    private final PayoutRepository payoutRepository;
    private final CricketMatchesRepository cricketMatchesRepository;
    private final CricketMoneyRepository cricketMoneyRepository;

    public User getUserByUserId(Integer userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isEmpty()) {
            throw new UserNotFoundException("user details not found for userId : " + userId);
        }
        return userOptional.get();
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> getUserByUserName(String userName) {
        return userRepository.findByUsername(userName);
    }

    public Payout getPayoutByUserId(Integer userId) {
        Optional<Payout> payoutOptional = payoutRepository.findByUserId(userId);
        if(payoutOptional.isEmpty()) {
            throw new PayoutNotFoundException("payout details not found for userId : " + userId);
        }
        return payoutOptional.get();
    }

    public Payout savePayout(Payout payout) {
        return payoutRepository.save(payout);
    }

    public List<CricketMatches> saveAllCricketMatches(List<CricketMatches> cricketMatchesList) {
        return cricketMatchesRepository.saveAll(cricketMatchesList);
    }

    public CricketMoney saveCricketMoney(CricketMoney cricketMoney) {
        return cricketMoneyRepository.save(cricketMoney);
    }
    public CricketMoney getOddsCricketByMatchId(int matchId) {
        Optional<CricketMoney> cricketOddsOptional = cricketMoneyRepository.getByMatchId(matchId);
        if(cricketOddsOptional.isEmpty()) {
            throw new CricketOddsNotFoundException("in give_odds func, criketOdds not found for match_id : " + matchId);
        }
        return cricketOddsOptional.get();
    }

    public CricketMoney updateOddMoneyInDatabaseBasisStateName(boolean isAddition, int matchId, String stateName, double userMoney) {
        CricketMoney cricketMoney = getOddsCricketByMatchId(matchId);
        if(StateName.BALL_WICKET.getStateName().equalsIgnoreCase(stateName)) {
            cricketMoney.setWicketMoney(updateMoney(cricketMoney.getWicketMoney(), isAddition, userMoney));
        } else if(StateName.BALL_SCORE_ZERO.getStateName().equalsIgnoreCase(stateName)) {
            cricketMoney.setRunZeroMoney(updateMoney(cricketMoney.getRunZeroMoney(), isAddition, userMoney));
        } else if(StateName.BALL_SCORE_ONE.getStateName().equalsIgnoreCase(stateName)) {
            cricketMoney.setWicketMoney(updateMoney(cricketMoney.getRunOneMoney(), isAddition, userMoney));
        } else if(StateName.BALL_SCORE_TWO.getStateName().equalsIgnoreCase(stateName)) {
            cricketMoney.setWicketMoney(updateMoney(cricketMoney.getRunTwoMoney(), isAddition, userMoney));
        } else if(StateName.BALL_SCORE_THREE.getStateName().equalsIgnoreCase(stateName)) {
            cricketMoney.setWicketMoney(updateMoney(cricketMoney.getRunThreeMoney(), isAddition, userMoney));
        } else if(StateName.BALL_SCORE_FOUR.getStateName().equalsIgnoreCase(stateName)) {
            cricketMoney.setWicketMoney(updateMoney(cricketMoney.getRunFourMoney(), isAddition, userMoney));
        } else if(StateName.BALL_SCORE_FIVE.getStateName().equalsIgnoreCase(stateName)) {
            cricketMoney.setWicketMoney(updateMoney(cricketMoney.getRunFiveMoney(), isAddition, userMoney));
        } else if(StateName.BALL_SCORE_SIX.getStateName().equalsIgnoreCase(stateName)) {
            cricketMoney.setWicketMoney(updateMoney(cricketMoney.getRunSixMoney(), isAddition, userMoney));
        }
        return saveCricketMoney(cricketMoney);
    }

    public Payout updateMoneyInPayout(boolean isAddition, Integer userId, double money) {
        Payout payout = getPayoutByUserId(userId);
        payout.setTotalAmount(updateMoney(payout.getTotalAmount(), isAddition, money));
        return savePayout(payout);
    }

    private Double updateMoney(double totalAmount, boolean isAddition, double money) {
        return totalAmount + (isAddition? money: -money);
    }
}

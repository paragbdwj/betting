package com.elephants.betting.common.helper;

import com.elephants.betting.src.config.NullAwareBeanUtilsBean;
import com.elephants.betting.src.enums.StateName;
import com.elephants.betting.src.enums.WinningStatus;
import com.elephants.betting.src.exception.CricketMatchesNotFoundException;
import com.elephants.betting.src.exception.CricketOddsNotFoundException;
import com.elephants.betting.src.exception.PayoutNotFoundException;
import com.elephants.betting.src.exception.UserNotFoundException;
import com.elephants.betting.src.model.CricketMatches;
import com.elephants.betting.src.model.CricketMatchOddState;
import com.elephants.betting.src.model.Payout;
import com.elephants.betting.src.model.User;
import com.elephants.betting.src.repository.CricketMatchesRepository;
import com.elephants.betting.src.repository.CricketOddStateManagementRepository;
import com.elephants.betting.src.repository.PayoutRepository;
import com.elephants.betting.src.repository.UserRepository;
import com.elephants.betting.src.request.UpdateOddsRequest;
import com.elephants.betting.src.response.MatchPageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class DatabaseHelper {
    private final UserRepository userRepository;
    private final PayoutRepository payoutRepository;
    private final CricketMatchesRepository cricketMatchesRepository;
    private final CricketOddStateManagementRepository cricketOddStateManagementRepository;

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
        List<Payout> payoutList = payoutRepository.findTopPayoutsByUserIdOrderByCreatedAtDesc(userId, PageRequest.of(0, 1));
        if(payoutList.isEmpty()) {
            throw new PayoutNotFoundException("payout details not found for userId : " + userId);
        }
        return payoutList.get(0);
    }

    public Payout getOddTransactionPayoutByUserId(Integer userId) {
        List<Payout> payoutList = payoutRepository.findTopPayoutsByUserIdOrderByCreatedAtDesc(userId, PageRequest.of(0, 1));
        if(payoutList.isEmpty()) {
            throw new PayoutNotFoundException("payout details not found for userId : " + userId);
        }
        return payoutList.get(0);
    }

    public Payout savePayout(Payout payout) {
        return payoutRepository.save(payout);
    }

    public List<CricketMatches> saveAllCricketMatches(List<CricketMatches> cricketMatchesList) {
        return cricketMatchesRepository.saveAll(cricketMatchesList);
    }

    public CricketMatchOddState saveCricketMoney(CricketMatchOddState cricketMatchOddState) {
        return cricketOddStateManagementRepository.save(cricketMatchOddState);
    }

    public CricketMatches findByMatchId(int matchId) {
        Optional<CricketMatches> cricketMatchesOptional = cricketMatchesRepository.findById(matchId);
        if(cricketMatchesOptional.isEmpty()) {
            throw new CricketMatchesNotFoundException("in find_bu_match_id func, cricket_matches not found for id : " + matchId);
        }
        return cricketMatchesOptional.get();
    }
    public CricketMatchOddState getOddsCricketByMatchId(int matchId) {
        Optional<CricketMatchOddState> cricketOddsOptional = cricketOddStateManagementRepository.getByMatchId(matchId);
        if(cricketOddsOptional.isEmpty()) {
            throw new CricketOddsNotFoundException("in give_odds func, criketOdds not found for match_id : " + matchId);
        }
        return cricketOddsOptional.get();
    }

    public CricketMatchOddState updateOddMoneyInDatabaseBasisStateName(boolean isAddition, int matchId, String stateName, double userMoney) {
        CricketMatchOddState cricketMatchOddState = getOddsCricketByMatchId(matchId);
        if(StateName.BALL_WICKET.getStateName().equalsIgnoreCase(stateName)) {
            cricketMatchOddState.setWicketMoney(updateMoney(cricketMatchOddState.getWicketMoney(), isAddition, userMoney));
        } else if(StateName.BALL_SCORE_ZERO.getStateName().equalsIgnoreCase(stateName)) {
            cricketMatchOddState.setRunZeroMoney(updateMoney(cricketMatchOddState.getRunZeroMoney(), isAddition, userMoney));
        } else if(StateName.BALL_SCORE_ONE.getStateName().equalsIgnoreCase(stateName)) {
            cricketMatchOddState.setRunOneMoney(updateMoney(cricketMatchOddState.getRunOneMoney(), isAddition, userMoney));
        } else if(StateName.BALL_SCORE_TWO.getStateName().equalsIgnoreCase(stateName)) {
            cricketMatchOddState.setRunTwoMoney(updateMoney(cricketMatchOddState.getRunTwoMoney(), isAddition, userMoney));
        } else if(StateName.BALL_SCORE_THREE.getStateName().equalsIgnoreCase(stateName)) {
            cricketMatchOddState.setRunThreeMoney(updateMoney(cricketMatchOddState.getRunThreeMoney(), isAddition, userMoney));
        } else if(StateName.BALL_SCORE_FOUR.getStateName().equalsIgnoreCase(stateName)) {
            cricketMatchOddState.setRunFourMoney(updateMoney(cricketMatchOddState.getRunFourMoney(), isAddition, userMoney));
        } else if(StateName.BALL_SCORE_FIVE.getStateName().equalsIgnoreCase(stateName)) {
            cricketMatchOddState.setRunFiveMoney(updateMoney(cricketMatchOddState.getRunFiveMoney(), isAddition, userMoney));
        } else if(StateName.BALL_SCORE_SIX.getStateName().equalsIgnoreCase(stateName)) {
            cricketMatchOddState.setRunSixMoney(updateMoney(cricketMatchOddState.getRunSixMoney(), isAddition, userMoney));
        }
        return saveCricketMoney(cricketMatchOddState);
    }

    public Payout updateMoneyInPayout(boolean isAddition, Integer userId, double money) throws InvocationTargetException, IllegalAccessException {
        Payout payout = new Payout();
        BeanUtilsBean notNull = new NullAwareBeanUtilsBean();
        notNull.copyProperties(payout, getPayoutByUserId(userId));
        payout.setId(null);
        payout.setTotalAmount(updateMoney(payout.getTotalAmount(), isAddition, money));
        payout.setOddTransaction(false);
        payout.setCreatedAt(LocalDateTime.now());
        return savePayout(payout);
    }

    private Double updateMoney(double totalAmount, boolean isAddition, double money) {
        return totalAmount + (isAddition? money: -money);
    }

    public List<CricketMatchOddState> saveAllCricketMatchStateOddManagement(List<CricketMatchOddState> cricketMatchOddStateList) {
        return cricketOddStateManagementRepository.saveAll(cricketMatchOddStateList);
    }

    public List<CricketMatchOddState> getAllCricketMoneyByMatchIdList(List<Integer> matchIds) {
        return cricketOddStateManagementRepository.findAllByMatchIdIn(matchIds);
    }

    public List<CricketMatches> getCricketMatchesListByUrls(List<String> urls) {
        return cricketMatchesRepository.findAllByUrlIn(urls);
    }

    public Payout updateMoneyInPayoutBasisRequest(boolean isAddition, UpdateOddsRequest request) {
        Payout payout = getOddTransactionPayoutByUserId(request.getUserId());
        Payout newPayout = new Payout();
        newPayout.setTotalAmount(updateMoney(payout.getTotalAmount(), isAddition, request.getMoneyOnStake()));
        newPayout.setCreatedAt(LocalDateTime.now());
        newPayout.setOdds(request.getOddValue());
        newPayout.setMatchDetails(request.getTeamOneName() + "$" + request.getTeamTwoName() + "$" + request.getTeamOneBall() + "$" + request.getTeamTwoBall());
        newPayout.setOddState(request.getStateName());
        newPayout.setMoneyOnStake(request.getMoneyOnStake());
        newPayout.setOddTransaction(true);
        newPayout.setWinningStatus(WinningStatus.IN_PROGRESS.getWinningStatus());
        newPayout.setMatchId(request.getMatchId());
        newPayout.setUserId(payout.getUserId());
        return savePayout(newPayout);
    }

    public List<Payout> getTop20TransactionsByUserId(int userId) {
        return payoutRepository.findTop20ByUserIdAndIsOddTransactionOrderByCreatedAtDesc(userId, true);
    }

    public List<Payout> findByWinningStatusAndMatchId(int matchId, String winningStatus) {
        int pageNumber = 0, pageSize = 10;
        int maxIntegrity = 10000;
        List<Payout> payoutList = new ArrayList<>();
        while(maxIntegrity-- > 0) {
            List<Payout> tempPayoutList = payoutRepository.findAllByWinningStatusAndMatchId(winningStatus, matchId, PageRequest.of(pageNumber, pageSize));
            if(CollectionUtils.isEmpty(tempPayoutList)) {
                break;
            }
            payoutList.addAll(tempPayoutList);
            pageSize++;
        }
        return payoutList;
    }

    public List<CricketMatches> getAllLiveMatches(){
        return cricketMatchesRepository.findByIsLiveMatch(true);
    }

    public void setWinningStatusBasisBallStatus(MatchPageResponse matchPageResponse, List<Payout> getPayoutListForWinningStatusInProgress) {
        // TODO : lastBallResults revisit
        List<Payout> updatedPayouts = new ArrayList<>();
        for( Payout payout : getPayoutListForWinningStatusInProgress) {
            if(StateName.getRunStatusToStateName().getOrDefault(matchPageResponse.getLastBallsResults().get(0), StateName.NULL_STATE).getStateName().equalsIgnoreCase(payout.getOddState())) {
                payout.setWinningStatus(WinningStatus.WIN.getWinningStatus());
                payout.setTotalAmount(payout.getTotalAmount() + payout.getMoneyOnStake());
            } else {
                payout.setWinningStatus(WinningStatus.LOST.getWinningStatus());
            }
            updatedPayouts.add(payout);
        }
        payoutRepository.saveAll(updatedPayouts);
    }
}

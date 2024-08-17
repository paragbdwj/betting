package com.elephants.betting.src.repository;

import com.elephants.betting.src.model.Payout;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PayoutRepository extends JpaRepository<Payout, Integer> {
    List<Payout> findAllByWinningStatusAndMatchId(String winningStatus,int matchId, Pageable pageable);
    List<Payout> findTop20ByUserIdAndIsOddTransactionOrderByCreatedAtDesc(int userId, boolean oddTransaction);
    List<Payout> findTopPayoutsByUserIdOrderByCreatedAtDesc(int userId, Pageable pageable);
}

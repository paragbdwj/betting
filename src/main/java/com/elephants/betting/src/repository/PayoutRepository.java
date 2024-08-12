package com.elephants.betting.src.repository;

import com.elephants.betting.src.model.Payout;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PayoutRepository extends JpaRepository<Payout, Integer> {
    List<Payout> findAllByWinningStatus(String winningStatus, Pageable pageable);
    List<Payout> findTopPayoutsByUserIdAndOddTransactionOrderByCreatedAtDesc(int userId, boolean oddTransaction,  Pageable pageable);
}

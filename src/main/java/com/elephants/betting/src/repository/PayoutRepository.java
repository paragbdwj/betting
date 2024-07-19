package com.elephants.betting.src.repository;

import com.elephants.betting.src.model.Payout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PayoutRepository extends JpaRepository<Payout, Integer> {
    Optional<Payout> findByUserId(Integer userId);
}

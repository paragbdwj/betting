package com.elephants.betting.acquisition.repository;

import com.elephants.betting.acquisition.model.Payout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PayoutRepository extends JpaRepository<Payout, Integer> {
    Optional<Payout> findByUserId(Integer userId);
}

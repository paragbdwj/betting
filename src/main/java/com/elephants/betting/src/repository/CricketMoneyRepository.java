package com.elephants.betting.src.repository;

import com.elephants.betting.src.model.CricketMoney;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CricketMoneyRepository extends JpaRepository<CricketMoney, Integer> {
    Optional<CricketMoney> getByMatchId(int matchId);
    List<CricketMoney> findAllByMatchIdIn(List<Integer> matchIds);
}

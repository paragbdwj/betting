package com.elephants.betting.src.repository;

import com.elephants.betting.src.model.CricketMatchOddState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CricketOddStateManagementRepository extends JpaRepository<CricketMatchOddState, Integer> {
    Optional<CricketMatchOddState> getByMatchId(int matchId);
    List<CricketMatchOddState> findAllByMatchIdIn(List<Integer> matchIds);
}

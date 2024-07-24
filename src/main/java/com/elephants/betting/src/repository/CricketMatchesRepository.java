package com.elephants.betting.src.repository;

import com.elephants.betting.src.model.CricketMatches;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CricketMatchesRepository extends JpaRepository<CricketMatches, Integer> {
    List<CricketMatches> findAllByUrlIn(List<String> urls);
}

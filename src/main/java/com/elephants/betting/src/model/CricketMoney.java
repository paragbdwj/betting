package com.elephants.betting.src.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.elephants.betting.common.constants.SQLConstants.TableNames.CRICKET_MONEY;

/*
This will give money instead of odds, from this we can calculate odds for respective states
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = CRICKET_MONEY)
public class CricketMoney {
    @Id
    private Integer id;

    @Column(name = "match_id")
    private int matchId;

    @Column(name = "run_zero_money")
    private double runZeroMoney;

    @Column(name = "run_one_money")
    private double runOneMoney;

    @Column(name = "run_two_money")
    private double runTwoMoney;

    @Column(name = "run_three_money")
    private double runThreeMoney;

    @Column(name = "run_four_money")
    private double runFourMoney;

    @Column(name = "run_five_money")
    private double runFiveMoney;

    @Column(name = "run_six_money")
    private double runSixMoney;

    @Column(name = "wicket_money")
    private double wicketMoney;
}

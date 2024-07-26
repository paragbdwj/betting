package com.elephants.betting.src.enums;

import lombok.Getter;

@Getter
public enum StateName {
    BALL_SCORE_ZERO("run_zero_odds"),
    BALL_SCORE_ONE("run_one_odds"),
    BALL_SCORE_TWO("run_two_odds"),
    BALL_SCORE_THREE("run_three_odds"),
    BALL_SCORE_FOUR("run_four_odds"),
    BALL_SCORE_FIVE("run_five_odds"),
    BALL_SCORE_SIX("run_six_odds"),
    BALL_WICKET("wicket_odds");


    private final String stateName;
    StateName(String stateName) {
        this.stateName = stateName;
    }
}

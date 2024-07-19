package com.elephants.betting.src.enums;

import lombok.Getter;

@Getter
public enum StateName {
    BALL_SCORE_ZERO("ball_score_zero"),
    BALL_SCORE_ONE("ball_score_one"),
    BALL_SCORE_TWO("ball_score_two"),
    BALL_SCORE_THREE("ball_score_three"),
    BALL_SCORE_FOUR("ball_score_four"),
    BALL_SCORE_FIVE("ball_score_five"),
    BALL_SCORE_SIX("ball_score_six"),
    BALL_WICKET("ball_wicket");


    private final String stateName;
    StateName(String stateName) {
        this.stateName = stateName;
    }
}

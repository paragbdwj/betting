package com.elephants.betting.src.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum StateName {
    BALL_SCORE_ZERO("run_zero_odds", "0"),
    BALL_SCORE_ONE("run_one_odds", "1"),
    BALL_SCORE_TWO("run_two_odds", "2"),
    BALL_SCORE_THREE("run_three_odds", "3"),
    BALL_SCORE_FOUR("run_four_odds", "4"),
    BALL_SCORE_FIVE("run_five_odds", "5"),
    BALL_SCORE_SIX("run_six_odds", "6"),
    BALL_WICKET("wicket_odds", "W"),
    NULL_STATE("null_state", "NULLIFY");

    @Getter
    private static final Map<String, StateName> runStatusToStateName;

    private final String stateName;
    private final String runStatus;
    StateName(String stateName, String runStatus) {
        this.stateName = stateName;
        this.runStatus = runStatus;
    }

    static {
        runStatusToStateName = Arrays.stream(StateName.values()).collect(Collectors.toMap(StateName::getRunStatus, Function.identity()));
    }
}

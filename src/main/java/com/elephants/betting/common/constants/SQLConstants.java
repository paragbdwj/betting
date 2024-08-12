package com.elephants.betting.common.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SQLConstants {

    @UtilityClass
    public static class TableNames {
        public static final String USERS = "users";
        public static final String PAYOUT = "payout";
        public static final String CRICKET_MATCHES = "cricket_matches";
        public static final String CRICKET_MATCH_ODD_STATE = "cricket_match_odd_state";
    }
}

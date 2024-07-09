package com.elephants.betting.common.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class APIConstants {

    @UtilityClass
    public static class LoginAPIs {
        public static final String IS_FIRST_TIME_USER_API = "/is-first-time-user";
    }
    @UtilityClass
    public static class UserAPIs {
        public static final String UPDATE_USER_DETAILS = "/user/update";

    }

    @UtilityClass
    public static class ThirdPartyAPIs {
        public static String CRIC_EXCHANGE_API_URL = "https://crex.live/";
    }

    @UtilityClass
    public static class PageAPIs {
        public static final String GET_HOME_PAGE = "/homepage/get";
        public static final String GET_MATCH_PAGE = "/match/get";
    }
}

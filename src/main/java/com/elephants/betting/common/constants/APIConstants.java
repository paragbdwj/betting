package com.elephants.betting.common.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class APIConstants {
    public static final String VERSION_V1 = "/v1";

    @UtilityClass
    public static class LoginAPIs {
        public static final String IS_VERIFIED_USER_API = "/is-verified-user";
    }
    @UtilityClass
    public static class UserAPIs {
        public static final String UPDATE_USER = "/user/update";

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

    @UtilityClass
    public static class AdminAPIs {
        public static final String ONBOARD_NEW_USER = "/user/onboard";
        public static final String UPDATE_USER_DETAILS = "/user/update-details";
    }

    @UtilityClass
    public static class OddsAPIs {
        public static final String GIVE_ODDS = "/odds/get";
        public static final String UPDATE_ODDS = "/update/odds";
    }
}

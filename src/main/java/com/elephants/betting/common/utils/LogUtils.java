package com.elephants.betting.common.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class LogUtils {
    @UtilityClass
    public static class ErrorLogUtils {
        public static String EXCEPTION_LOG = "got exception in : {} for request : {} with stack_trace : {}";
    }
}

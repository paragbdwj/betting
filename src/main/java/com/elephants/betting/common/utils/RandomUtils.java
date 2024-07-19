package com.elephants.betting.common.utils;

import lombok.experimental.UtilityClass;

import java.security.SecureRandom;

import static com.elephants.betting.common.constants.VarConstants.CHARACTERS;

@UtilityClass
public class RandomUtils {

    public String generateRandomString(int length) {
        if (length < 1) {
            throw new IllegalArgumentException("Length must be greater than 0");
        }
        SecureRandom random = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            stringBuilder.append(CHARACTERS.charAt(randomIndex));
        }
        return stringBuilder.toString();
    }
}

package com.elephants.betting.common.utils;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@UtilityClass
public class BettingOddsCalculator {
    static int[] stateAmount = {100, 100, 100, 100, 100, 100, 100, 100};
    static int sumOfAllStateAmount = 800;
    static float houseCut = 0.70f;
    static int capBettingOdds = 10;

    public static List<Float> giveAndUpdateBets(int bettingAmount, int idx) {
        sumOfAllStateAmount += bettingAmount;
        float amountToPay = sumOfAllStateAmount * (1 - houseCut);
        stateAmount[idx] += bettingAmount;
        List<Float> bettingOdds = new ArrayList<>(8);
        for (int index = 0; index < 8; index++) {
            if (amountToPay / stateAmount[index] > capBettingOdds) {
                bettingOdds.add((float) capBettingOdds);
            } else {
                bettingOdds.add(amountToPay / stateAmount[index]);
            }
            System.out.print(bettingOdds.get(index) + " ");
        }
        return bettingOdds;
    }
}


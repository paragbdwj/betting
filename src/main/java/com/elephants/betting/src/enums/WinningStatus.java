package com.elephants.betting.src.enums;

import lombok.Getter;

@Getter
public enum WinningStatus {
    WIN("win"),
    LOST("lost"),
    IN_PROGRESS("in_progress");

    private final String winningStatus;
    WinningStatus(String winningStatus) {
        this.winningStatus = winningStatus;
    }
}

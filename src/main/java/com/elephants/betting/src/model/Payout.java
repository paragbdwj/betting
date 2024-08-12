package com.elephants.betting.src.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.elephants.betting.common.constants.SQLConstants.TableNames.PAYOUT;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = PAYOUT)
public class Payout {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "total_amount")
    private Double totalAmount;  // by default to be set true

    @Column(name = "odd_state")
    private String oddState;

    @Column(name = "match-details")
    private String matchDetails;

    @Column(name = "odds")
    private double odds;

    @Column(name = "money_on_stake")
    private double moneyOnStake;

    @Column(name = "winning_status")
    private String winningStatus;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "match_id")
    private int matchId;

    @Column(name = "is_odd_transaction")
    private boolean isOddTransaction = false;
}

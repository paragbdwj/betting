package com.elephants.betting.src.model;

import com.elephants.betting.src.enums.WinningStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
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
@Table(name = PAYOUT, indexes = {
        @Index(name = "triple_composite_index", columnList = "user_id, is_odd_transaction, created_at"),
        @Index(name = "double_composite_index", columnList = "winning_status, match_id")
})
public class Payout {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "total_amount", nullable = false)
    private Double totalAmount;  // by default to be set true

    @Column(name = "odd_state", nullable = true)
    private String oddState;

    @Column(name = "match-details")
    private String matchDetails;

    @Column(name = "odds")
    private double odds;

    @Column(name = "money_on_stake")
    private double moneyOnStake;

    @Column(name = "winning_status")
    private WinningStatus winningStatus;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "match_id")
    private int matchId;

    @Column(name = "exposure", nullable = false)
    private double exposure;

    @Column(name = "chips", nullable = false)
    private double chips;

    @Column(name = "is_odd_transaction", nullable = false)
    private boolean isOddTransaction = false;
}

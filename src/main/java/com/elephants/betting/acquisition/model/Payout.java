package com.elephants.betting.acquisition.model;

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

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "total_amount", nullable = false)
    private Integer totalAmount;
}

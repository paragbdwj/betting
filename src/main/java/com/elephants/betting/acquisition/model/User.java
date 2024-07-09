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

import java.time.LocalDateTime;

import static com.elephants.betting.common.constants.SQLConstants.TableNames.USERS;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = USERS)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50)
    private String username;

    private String password;

    @Column(name = "phone_number", length = 10)
    private String phoneNumber;

    @Column(length = 100)
    private String email;

    @Column(name = "created_at", nullable = false, updatable = false, insertable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false, insertable = false)
    private LocalDateTime updatedAt;

    @Column(name= "is_first_time_user", nullable = false)
    private Boolean isFirstTimeUser;

    @Column(name = "is_valid", nullable = false)
    private Boolean isValid;
}

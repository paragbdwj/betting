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

    private String username;

    private String password;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(length = 100)
    private String email;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /*
        @Column(name= "is_first_time_user", nullable = false)
        private Boolean isFirstTimeUser;
    */

    @Column(name = "is_valid")
    private Boolean isValid;

    @Column(name = "onboarder_name")
    private String onboarderName;
}

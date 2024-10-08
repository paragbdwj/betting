package com.elephants.betting.src.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.elephants.betting.common.constants.SQLConstants.TableNames.CRICKET_MATCHES;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = CRICKET_MATCHES)
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(SnakeCaseStrategy.class)
public class CricketMatches {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "match_id")
    private int matchId;

    @Column(name = "team_one")
    private String teamOne;

    @Column(name = "team_two")
    private String teamTwo;

    @Column(name = "team_one_score")
    private String teamOneScore;

    @Column(name = "team_two_score")
    private String teamTwoScore;

    @Column(name = "team_one_overs")
    private String teamOneOvers;

    @Column(name = "team_two_overs")
    private String teamTwoOvers;

    @Column(name = "url")
    private String url;

    @Column(name = "is_live_match")
    private Boolean isLiveMatch;

    @Column(name = "last_ball_result")
    private String lastBallResult;

    @Column(name = "upcoming_time")
    private String upcomingTime;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Transient
    private boolean isUpdated = false;

    @Transient
    private String currentTeam; // te
}

package com.elephants.betting.src.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
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

import static com.elephants.betting.common.constants.SQLConstants.TableNames.CRICKET_MATCHES;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = CRICKET_MATCHES)
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CricketMatches {
    @Id
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

    @Column(name = "url")
    private String url;

    @Column(name = "is_live_match")
    private boolean isLiveMatch;
}

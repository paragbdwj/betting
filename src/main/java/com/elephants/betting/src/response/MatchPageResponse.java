package com.elephants.betting.src.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(SnakeCaseStrategy.class)
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MatchPageResponse {
    private Integer matchId;
    private List<String> lastBallsResults;  //
    private String teamOneName;
    private String teamTwoName;
    private String teamOneScore;
    private String teamTwoScore;
    private String oversByTeamOne;
    private String oversByTeamTwo;
    private String teamOneSymbol;
    private String teamTwoSymbol;
    private String previousOverScore;
}

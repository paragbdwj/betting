package com.elephants.betting.src.service;

import com.elephants.betting.common.helper.DatabaseHelper;
import com.elephants.betting.src.request.CricExchangeRequest;
import com.elephants.betting.src.request.MatchPageRequest;
import com.elephants.betting.src.response.CricExchangeResponse;
import com.elephants.betting.src.response.CricExchangeResponse.CricExchangeAttributes;
import com.elephants.betting.src.response.MatchPageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.elephants.betting.common.constants.APIConstants.ThirdPartyAPIs.CRIC_EXCHANGE_API_URL;
import static com.elephants.betting.common.constants.VarConstants.NULL_STRING;

@Service
@Slf4j
@RequiredArgsConstructor
public class CricketExchangeService {
    private final DatabaseHelper databaseHelper;
    private final Map<String, CricExchangeAttributes> cricExchangeAttributesMap = new HashMap<>();

    public CricExchangeResponse getCricExchangeResponse(CricExchangeRequest request) throws IOException {
        Document doc = Jsoup.connect(CRIC_EXCHANGE_API_URL).get();

        Elements liveCards = doc.select(".live-card.height100");
        for (Element liveCard : liveCards) {
            try {
                buildCricExchangeAttributesMap(liveCard);
            } catch (Exception e) {
                log.error("Caught error");
            }
        }

        return CricExchangeResponse.builder()
                .matches(cricExchangeAttributesMap.values().stream().toList())
                .build();

    }

    private void buildCricExchangeAttributesMap(Element liveCard) {
        Elements teamScoresForDiv = liveCard.select("div.team-score");
        String teamSymbolPlaying = "null";
        String teamOneFlagUrl = null, teamTwoFlagUrl = null;
        boolean isTestMatch = false;
        try {
            String matchType = liveCard.select("h3.match-number").text();
            if (matchType.contains("TEST")) {
                isTestMatch = true;
            }
            for (Element teamScore : teamScoresForDiv) {
                // Extract the team symbol (e.g., SDS or PDL)
                String teamSymbol = teamScore.select("span.live-c, span.live-d").text();
                Elements imgElements = teamScore.select("img");
                if (!imgElements.isEmpty()) {
                    String imgUrl = imgElements.attr("src");

                    // Assign the correct flag URL to the corresponding team
                    if (teamSymbolPlaying.equals("null")) {
                        teamOneFlagUrl = imgUrl;
                    } else {
                        teamTwoFlagUrl = imgUrl;
                    }
                }
                // Check if the score contains an asterisk (*)
                Elements scoreElements = teamScore.select("span.match-score");
                for (Element scoreElement : scoreElements) {
                    if (scoreElement.html().contains("*")) {
                        teamSymbolPlaying = getTeamSymbolFromRawString(teamSymbol);
                    }
                }
            }
        } catch (Exception e) {
            log.error("caught exception in buildCricExchangeAttributesMap");
        }

        Elements teamScores = liveCard.select(".team-score");
        Elements anchors = liveCard.select("a[href]");
        Element liveTagElement = liveCard.selectFirst("span.liveTag");
        String liveScoreHref = "";

        // this is the link which will be required to get ball by ball analysis
        for (Element anchor : anchors) {
            String href = anchor.attr("href");
            if (href.startsWith("/scoreboard")) {
                liveScoreHref = href;
            }
        }

        String teamOne = Objects.requireNonNull(teamScores.get(0).select("span").first()).text();
        String teamTwo = Objects.requireNonNull(teamScores.get(1).select("span").first()).text();
        String teamOneOvers = teamScores.get(0).select("span.match-over").text();
        String teamTwoOvers = teamScores.get(1).select("span").get(3).text();


        // key is href value as it is going to be unique
        cricExchangeAttributesMap.put(CRIC_EXCHANGE_API_URL + liveScoreHref, CricExchangeAttributes.builder()
                .url(CRIC_EXCHANGE_API_URL + liveScoreHref)
                .isLiveMatch(Objects.nonNull(liveTagElement))
                .teamOne(teamOne)
                .teamTwo(teamTwo)
                .teamOneOvers(teamOneOvers)
                .teamTwoOvers(teamTwoOvers)
                .teamOneScore(teamScores.get(0).select("span").get(2).text())
                .teamTwoScore(teamScores.get(1).select("span").get(2).text())
                .teamOneFlag(teamOneFlagUrl)
                .teamTwoFlag(teamTwoFlagUrl)
                .upcomingTime(Objects.requireNonNull(liveCard.select("span.upcomingTime")).attr("title"))
                .currentTeam(teamSymbolPlaying.equalsIgnoreCase(teamOne)?"team_one":"team_two")
                .isTestMatch(isTestMatch)
                .build());
    }

    private String getTeamSymbolFromRawString(String teamSymbol) {
        char[] chars = teamSymbol.toCharArray();
        int i = 0;
        StringBuilder teamSymbolRawString = new StringBuilder();
        while(i < Math.min(chars.length, 10) && chars[i] != ' ') {
            teamSymbolRawString.append(chars[i]);
            i++;
        }
        String rawTeamSymbol = teamSymbolRawString.toString();
        return rawTeamSymbol;
    }

    public MatchPageResponse getMatchResult(MatchPageRequest request) throws IOException{
        //from matchId extract url
        String url = databaseHelper.findByMatchId(request.getMatchId()).getUrl();
        Document document = Jsoup.connect(url).get();
        MatchPageResponse matchPageResponse = new MatchPageResponse();
        setMatchResultResponse(matchPageResponse, document, request.getMatchId());
        return matchPageResponse;
    }

    private void setMatchResultResponse(MatchPageResponse matchPageResponse, Document document, Integer matchId) {
        List<String> overResults = getOverResults(document);
        JSONObject matchData = getMatchDataJsonObject(document);
        Pair<String, String> teamSymbols = getTeamSymbols(document);
        // Extract team names and scores
        matchPageResponse.setTeamOneName(getValueOrDefault(matchData, "team1_f_n", NULL_STRING));
        matchPageResponse.setTeamOneScore(getValueOrDefault(matchData, "score1", NULL_STRING));
        matchPageResponse.setTeamTwoName(getValueOrDefault(matchData, "team2_f_n", NULL_STRING));
        matchPageResponse.setTeamTwoScore(getValueOrDefault(matchData, "score2", NULL_STRING));
        matchPageResponse.setOversByTeamOne(getValueOrDefault(matchData, "over1", NULL_STRING));
        matchPageResponse.setOversByTeamTwo(getValueOrDefault(matchData, "over2", NULL_STRING));
        matchPageResponse.setMatchId(matchId);
        matchPageResponse.setTeamOneSymbol(teamSymbols.getLeft());
        matchPageResponse.setTeamTwoSymbol(teamSymbols.getRight());
        matchPageResponse.setLastBallsResults(overResults);
    }

    private Pair<String, String> getTeamSymbols(Document document) {
        Element metaElement = document.select("meta[name=keywords]").first();
        String teamOne = null, teamTwo = null;
        if (metaElement != null) {
            String input = metaElement.attr("content");

            // Use regex to extract BOL and RMR

            int i = 0;
            int start = 0;
            int count = 0;

            while (i < input.length() && count < 2) {
                // Find the start of the team symbol (a capital letter)
                while (i < input.length() && !Character.isUpperCase(input.charAt(i))) {
                    i++;
                }

                start = i;

                // Find the end of the team symbol (either space or a non-capital letter)
                while (i < input.length() && (Character.isUpperCase(input.charAt(i)) || Character.isDigit(input.charAt(i)))) {
                    i++;
                }

                if (count == 0 && start < i) {
                    teamOne = input.substring(start, i);
                    count++;
                } else if (count == 1 && start < i) {
                    teamTwo = input.substring(start, i);
                    count++;
                }

                i++;
            }

        }
        return Pair.of(teamOne, teamTwo);
    }

    private String getValueOrDefault(JSONObject jsonObject, String key, String defaultValue) {
        return (jsonObject.has(key)) ? (StringUtils.isEmpty(jsonObject.getString(key)) ? defaultValue : jsonObject.getString(key)) : defaultValue;
    }

    private JSONObject getMatchDataJsonObject(Document document) {
        Element scriptElement = document.select("script#app-root-state").first();
        String jsonString = Objects.requireNonNull(scriptElement).html();
        jsonString = jsonString.replace("&q;", "\"").replace("&a;", "&");

        // Parsing the JSON data
        JSONObject jsonObject = new JSONObject(jsonString);
        return jsonObject.getJSONObject("https://api-v1.com/v1/sV3.php");
    }

    private List<String> getOverResults(Document document) {
        List<String> overResults = new ArrayList<>();

        // Select the div with class "overs-slide"
        Elements oversSlides = document.select("div.overs-slide");
        for (Element oversSlide : oversSlides) {
            // Select the divs with class "over-ball" inside the selected overs-slide
            Elements overBalls = oversSlide.select("div.over-ball");
            for (Element overBall : overBalls) {
                // Extract the text inside the over-ball div, which represents the runs
                String runs = overBall.text().trim();
                if (!runs.isEmpty()) {
                    overResults.add(runs);
                }
            }
        }
        Collections.reverse(overResults);
        return overResults;
    }

    public Map<String, CricExchangeAttributes> getCricExchangeAttributesMap() {
        return this.cricExchangeAttributesMap;
    }

}

package com.elephants.betting.src.service;

import com.elephants.betting.common.helper.DatabaseHelper;
import com.elephants.betting.src.model.CricketMatches;
import com.elephants.betting.src.request.CricExchangeRequest;
import com.elephants.betting.src.request.MatchPageRequest;
import com.elephants.betting.src.request.MatchResultRequest;
import com.elephants.betting.src.response.CricExchangeResponse;
import com.elephants.betting.src.response.CricExchangeResponse.CricExchangeAttributes;
import com.elephants.betting.src.response.MatchPageResponse;
import com.elephants.betting.src.response.MatchResultResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
            buildCricExchangeAttributesMap(liveCard);
        }

        return CricExchangeResponse.builder()
                .matches(cricExchangeAttributesMap.values().stream().toList())
                .build();

    }

    private void buildCricExchangeAttributesMap(Element liveCard) {
        Elements teamScores = liveCard.select(".team-score");
        Elements anchors = liveCard.select("a[href]");
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
                .isLiveMatch(!teamScores.get(0).select("sup").isEmpty())
                .teamOne(teamOne)
                .teamTwo(teamTwo)
                .teamOneOvers(teamOneOvers)
                .teamTwoOvers(teamTwoOvers)
                .teamOneScore(teamScores.get(0).select("span").get(2).text())
                .teamTwoScore(teamScores.get(1).select("span").get(2).text())
                .build());
    }

    public MatchPageResponse getMatchResult(MatchPageRequest request) throws IOException{
        //from matchId extract url
        String url = databaseHelper.findByMatchId(request.getMatchId()).getUrl();
        Document document = Jsoup.connect(url).get();
        MatchPageResponse matchPageResponse = new MatchPageResponse();
        setMatchResultResponse(matchPageResponse, document);
        return matchPageResponse;
    }

    private void setMatchResultResponse(MatchPageResponse matchPageResponse, Document document) {
        List<String> overResults = getOverResults(document);
        JSONObject matchData = getMatchDataJsonObject(document);

        // Extract team names and scores
        matchPageResponse.setTeamOneName(getValueOrDefault(matchData, "team1_f_n", NULL_STRING));
        matchPageResponse.setTeamOneScore(getValueOrDefault(matchData, "score1", NULL_STRING));
        matchPageResponse.setTeamTwoName(getValueOrDefault(matchData, "team2_f_n", NULL_STRING));
        matchPageResponse.setTeamTwoScore(getValueOrDefault(matchData, "score2", NULL_STRING));
        matchPageResponse.setOversByTeamOne(getValueOrDefault(matchData, "over1", NULL_STRING));
        matchPageResponse.setOversByTeamTwo(getValueOrDefault(matchData, "over2", NULL_STRING));

        matchPageResponse.setLastBallsResults(overResults);
    }

    private String getValueOrDefault(JSONObject jsonObject, String key, String defaultValue) {
        return (jsonObject.has(key) || StringUtils.isEmpty(jsonObject.getString(key))) ? jsonObject.getString(key) : defaultValue;
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
        return overResults;
    }

    public Map<String, CricExchangeAttributes> getCricExchangeAttributesMap() {
        return this.cricExchangeAttributesMap;
    }

}

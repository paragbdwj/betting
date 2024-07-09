package com.elephants.betting.acquisition.service;

import com.elephants.betting.acquisition.request.CricExchangeRequest;
import com.elephants.betting.acquisition.request.MatchResultRequest;
import com.elephants.betting.acquisition.response.CricExchangeResponse;
import com.elephants.betting.acquisition.response.CricExchangeResponse.CricExchangeAttributes;
import com.elephants.betting.acquisition.response.MatchResultResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Service
@Slf4j
@RequiredArgsConstructor
public class CricketExchange {
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

        // key is href value as it is going to be unique
        cricExchangeAttributesMap.put(CRIC_EXCHANGE_API_URL + liveScoreHref, CricExchangeAttributes.builder()
                .hRef(CRIC_EXCHANGE_API_URL + liveScoreHref)
                .isLiveMatch(!teamScores.get(0).select("sup").isEmpty())
                .teamOne(teamOne)
                .teamTwo(teamTwo)
                .teamOneScore(teamScores.get(0).select(".match-score").text())
                .teamTwoScore(teamScores.get(1).select(".match-score").text())
                .build());
    }

    public MatchResultResponse getMatchResult(MatchResultRequest request) throws IOException{
        Document document = Jsoup.connect(request.getUrl()).get();
        MatchResultResponse matchResultResponse = new MatchResultResponse();
        setMatchResultResponse(matchResultResponse, document);
        return matchResultResponse;
    }

    private void setMatchResultResponse(MatchResultResponse matchResultResponse, Document document) {
        List<String> overResults = getOverResults(document);
        JSONObject matchData = getMatchDataJsonObject(document);

        // Extract team names and scores
        matchResultResponse.setTeamOneName(matchData.getString("team1_f_n"));
        matchResultResponse.setTeamOneScore(matchData.getString("score1"));
        matchResultResponse.setTeamTwoName(matchData.getString("team2_f_n"));
        matchResultResponse.setTeamTwoScore(matchData.getString("score2"));
        matchResultResponse.setOversByTeamOne(matchData.getString("over1"));
        matchResultResponse.setOversByTeamTwo(matchData.getString("over2"));
        matchResultResponse.setLastBallsResults(overResults);
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

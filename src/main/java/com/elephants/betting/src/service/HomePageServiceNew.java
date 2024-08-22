package com.elephants.betting.src.service;

import com.elephants.betting.src.request.HomePageRequest;
import com.elephants.betting.src.response.HomePageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class HomePageServiceNew {
    private final Scheduler scheduler;
        public HomePageResponse getHomePageAPI(HomePageRequest request) {
            return HomePageResponse.builder()
//                    .matches(scheduler.getMatchesToShowOnHomePage().stream().sorted((a,b)-> Boolean.compare(b.getIsLiveMatch(), a.getIsLiveMatch())).toList())
                    .matches(scheduler.getMatchesToShowOnHomePage().stream().filter(cricketMatches -> Boolean.TRUE.equals(cricketMatches.getIsLiveMatch())).toList())
                    .build();

        }
}

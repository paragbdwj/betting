package com.elephants.betting.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "http.pool-properties")
public class HttpPoolProperties {
    private HttpPoolConfig cricketExchange;
}

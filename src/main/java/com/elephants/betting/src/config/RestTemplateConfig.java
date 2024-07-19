package com.elephants.betting.src.config;

import com.elephants.betting.common.config.HttpPoolConfig;
import com.elephants.betting.common.config.HttpPoolProperties;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.core5.util.Timeout;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import static com.elephants.betting.common.constants.BeanNames.CRIC_EXCHANGE_REST_TEMPLATE;

@Configuration
public class RestTemplateConfig {

    @Bean(CRIC_EXCHANGE_REST_TEMPLATE)
    public RestTemplate cricExchangeRestTemplate(HttpPoolProperties httpPoolProperties) {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(getHttpClientFactory(httpPoolProperties.getCricketExchange()));
        return new RestTemplate(requestFactory);
    }

    private CloseableHttpClient getHttpClientFactory(HttpPoolConfig httpPoolConfig) {
        PoolingHttpClientConnectionManager poolingConnManager = PoolingHttpClientConnectionManagerBuilder
                .create()
                .setMaxConnPerRoute(httpPoolConfig.getDefaultMaxPerRoute())
                .setMaxConnTotal(httpPoolConfig.getMaxTotal())
                .setDefaultConnectionConfig(ConnectionConfig
                        .custom()
                        .setSocketTimeout(Timeout.ofMilliseconds(httpPoolConfig.getSocketTimeout()))
                        .setConnectTimeout(Timeout.ofMilliseconds(httpPoolConfig.getConnectionTimeout()))
                        .build())
                .build();
        return HttpClients.custom()
                .setConnectionManager(poolingConnManager)
                .build();
    }
}

package com.elephants.betting.src.config;

import com.elephants.betting.common.config.HttpPoolConfig;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.core5.util.Timeout;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RestTemplateConfig {

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

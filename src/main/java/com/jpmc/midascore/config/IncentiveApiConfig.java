package com.jpmc.midascore.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class IncentiveApiConfig {

    @Value("${incentive.api.base-url:http://localhost:8080}")
    private String baseUrl;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public String getIncentiveEndpoint() {
        return baseUrl + "/incentive";
    }
}



package com.performance.sql.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class TwitterConfig {
    @Value("${twitter.api.consumerKey}")
    private String consumerKey;

    @Value("${twitter.api.consumerSecret}")
    private String consumerSecret;

    @Value("${twitter.api.token}")
    private String token;

    @Value("${twitter.api.secret}")
    private String secret;

    public String getConsumerKey() {
        return consumerKey;
    }

    public String getConsumerSecret() {
        return consumerSecret;
    }

    public String getToken() {
        return token;
    }

    public String getSecret() {
        return secret;
    }
}

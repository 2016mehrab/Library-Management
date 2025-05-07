package com.eshan.library.configs;

import com.postmarkapp.postmark.Postmark;
import com.postmarkapp.postmark.client.ApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PostmarkConfig {
    @Value("${postmark.api-key}")
    private String apiKey;
    @Bean
    public ApiClient postmarkApiClient() {
        return Postmark.getApiClient(apiKey);
    }
}

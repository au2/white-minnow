package com.whiteminnow.geocoding.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@Slf4j
public class AppConfig {

  @Bean
  @ConfigurationProperties(prefix = "white-minnow.geocoding")
  public AppProperties appProperties() {
    return new AppProperties();
  }

  @Bean
  public WebClient geocodingWebClient() {
    AppProperties properties = appProperties();
    String url = properties.getUrl();
    log.info("Building web client for {}", url);
    return WebClient.create(url);
  }

}

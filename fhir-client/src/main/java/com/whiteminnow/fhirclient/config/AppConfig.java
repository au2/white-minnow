package com.whiteminnow.fhirclient.config;

import ca.uhn.fhir.context.FhirContext;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Configuration
@Slf4j
@Setter
public class AppConfig {

  @Value("${nxt-crt}")
  private String nxtCrt;

  @Value("${nxt-key}")
  private String nxtKey;

  @Bean
  @ConfigurationProperties(prefix = "white-minnow.fhir-client")
  public AppProperties appPropertiesBean() {
    return new AppProperties();
  }

  @Bean
  @SneakyThrows
  public WebClient webClientBean() {
    InputStream nxtCrtStream = new ByteArrayInputStream((nxtCrt.getBytes()));
    InputStream nxtKeyStream = new ByteArrayInputStream((nxtKey.getBytes()));

    log.info("building ssl context");
    SslContext sslContext = SslContextBuilder.forClient()
        .keyManager(nxtCrtStream, nxtKeyStream)
        .build();

    HttpClient httpClient = HttpClient.create()
        .secure(sslContextSpec -> sslContextSpec.sslContext(sslContext));

    AppProperties appProperties = appPropertiesBean();
    return WebClient.builder()
        .baseUrl(appProperties.getUrl())
        .clientConnector(new ReactorClientHttpConnector(httpClient))
        .build();
  }

  @Bean
  public FhirContext fhirContextBean() {
    return FhirContext.forR4();
  }

}

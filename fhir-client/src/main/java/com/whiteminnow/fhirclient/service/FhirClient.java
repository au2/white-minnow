package com.whiteminnow.fhirclient.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class FhirClient {

  private final WebClient webClient;

  public Mono<String> getBundle() {
    final String uri = "/fhir/Patient?_count=5";

    return webClient.get()
        .uri(uri)
        .accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML)
        .exchangeToMono(response -> {
          HttpStatusCode statusCode = response.statusCode();
          log.info("status code is {}", statusCode.value());
          if (statusCode.equals(HttpStatus.OK)) {
            return response.bodyToMono(String.class);
          } else if (statusCode.is4xxClientError() || statusCode.is5xxServerError()) {
            return Mono.just("Error response");
          } else {
            return response.createException().flatMap(Mono::error);
          }
        });
  }

}

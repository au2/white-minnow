package com.whiteminnow.fhirclient.service;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import com.whiteminnow.fhirclient.model.ResourceRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.Bundle;
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

  private final FhirContext context;

  public Mono<Bundle> getBundle(ResourceRequest request) {

    return webClient.get()
        .uri(request.getUri())
        .accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML)
        .exchangeToMono(response -> {
          HttpStatusCode statusCode = response.statusCode();
          log.info("status code is {}", statusCode.value());
          if (statusCode.equals(HttpStatus.OK)) {
            return response
                .bodyToMono(String.class)
                .flatMap(body -> {
                  IParser parser = context.newJsonParser();
                  return Mono.just(parser.parseResource(Bundle.class, body));
                });
          } else {
            return response.createException().flatMap(Mono::error);
          }
        });
  }

}

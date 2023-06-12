package com.whiteminnow.fhirclient;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whiteminnow.fhirclient.model.ResourceRequest;
import com.whiteminnow.fhirclient.service.FhirClient;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.Bundle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
@Slf4j
public class FhirClientTests {

  @Autowired
  private FhirClient fhirClient;

  @Autowired
  private FhirContext context;

  @Test
  @SneakyThrows
  void testFhirClient() {
    ResourceRequest request = ResourceRequest.builder()
        .requestResource("Patient")
        .resultResource("Bundle")
        .queryParameter("_count", "5")
        .build();


    Mono<Bundle> result = fhirClient.getBundle(request);

    StepVerifier.create(result)
        .expectNextMatches(body -> {
          IParser parser = context.newJsonParser();
          parser.setPrettyPrint(true);
          log.info(parser.encodeResourceToString(body));
          return true;
        })
        .verifyComplete();
  }

}

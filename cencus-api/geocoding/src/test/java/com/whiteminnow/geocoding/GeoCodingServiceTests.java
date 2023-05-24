package com.whiteminnow.geocoding;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whiteminnow.geocoding.service.GeoCodingService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
@Slf4j
public class GeoCodingServiceTests {

  @Autowired
  private GeoCodingService service;

  @Autowired
  private ObjectMapper mapper;

  @Test
  @SneakyThrows
  void testGeoCoding() {
    Mono<String> result = service.addressToCoordinates();

    StepVerifier.create(result)
        .expectNextMatches(body -> {
          try {
            JsonNode node = mapper.readTree(body);
            log.info(node.toPrettyString());
          } catch (JsonProcessingException exception) {
            log.error("Failed: ", exception);
          }
          return true;
        })
        .verifyComplete();
  }
}

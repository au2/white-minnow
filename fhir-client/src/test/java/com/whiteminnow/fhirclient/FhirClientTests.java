package com.whiteminnow.fhirclient;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whiteminnow.fhirclient.library.ResourceMapper;
import com.whiteminnow.fhirclient.model.NdiRequestLine;
import com.whiteminnow.fhirclient.model.ResourceRequest;
import com.whiteminnow.fhirclient.service.FhirClient;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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

  @Test
  @SneakyThrows
  void testGetIdsFor50() {
    Path resourceDirectory = Paths.get("src","test", "resources", "patients.txt");
    String absolutePath = resourceDirectory.toFile().getAbsolutePath();

    System.out.println(absolutePath);

    ResourceRequest request = ResourceRequest.builder()
        .requestResource("Patient")
        .resultResource("Bundle")
        .queryParameter("_count", "50")
        .build();

    Mono<List<String>> result = fhirClient.getIds(request);

    StepVerifier.create(result)
        .expectNextMatches(idList -> {
          try {
            FileWriter fileWriter = new FileWriter(absolutePath);
            for(String id: idList) {
              fileWriter.write(id + System.lineSeparator());
              log.info(id);
            }
            fileWriter.close();
            return true;
          } catch (Exception exception) {
            return false;
          }
        })
        .verifyComplete();
  }

  @Test
  @SneakyThrows
  void testCreateNdiFile() {
    Path resourceDirectory = Paths.get("src","test", "resources", "patients.txt");
    List<String> idList = Files.readAllLines(resourceDirectory);
    String queryParameterValue = String.join(",", idList);

    ResourceRequest request = ResourceRequest.builder()
        .requestResource("Patient")
        .queryParameter("_id", queryParameterValue)
        .build();

    Mono<List<IBaseResource>> result = fhirClient.getBundlePages(request);

    Path outputPath = Paths.get("src","test", "resources", "ndi_request.txt");
    String absoluteOutputPath = outputPath.toFile().getAbsolutePath();

    StepVerifier.create(result)
        .expectNextMatches(body -> {
          try {
            FileWriter fileWriter = new FileWriter(absoluteOutputPath);
            for(IBaseResource resource: body) {
              Patient patient = (Patient) resource;
              NdiRequestLine line = ResourceMapper.patientToNdiRequestLine(patient);
              fileWriter.write(line.toFileLine() + System.lineSeparator());
            }
            fileWriter.close();
            return true;
          } catch (Exception exception) {
            return false;
          }
        })
        .verifyComplete();
  }

}

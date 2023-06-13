package com.whiteminnow.fhirclient.service;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.util.BundleUtil;
import com.whiteminnow.fhirclient.library.BundleMapper;
import com.whiteminnow.fhirclient.model.ResourceRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

  private Mono<List<IBaseResource>> getBundlePage(String uri, List<IBaseResource> previousPages) {
    return webClient.get()
        .uri(uri)
        .accept(MediaType.APPLICATION_JSON)
        .exchangeToMono(response -> {
          HttpStatusCode statusCode = response.statusCode();
          log.info("status code is {}", statusCode.value());
          if (statusCode.equals(HttpStatus.OK)) {
            return response
                .bodyToMono(String.class)
                .flatMap(body -> {
                  IParser parser = context.newJsonParser();
                  Bundle result = parser.parseResource(Bundle.class, body);
                  List<IBaseResource> resources = BundleUtil.toListOfResources(context, result);
                  String nextLink = BundleUtil.getLinkUrlOfType(context, result, "next");
                  List<IBaseResource> allResources = new ArrayList<>(previousPages);
                  allResources.addAll(resources);
                  if (nextLink == null) {
                    return Mono.just(allResources);
                  } else {
                    String nextUri = nextLink.split("fhir")[1];
                    return getBundlePage(nextUri, allResources);
                  }
                });
          } else {
            return response.createException().flatMap(Mono::error);
          }
        });
  }

  public Mono<List<IBaseResource>> getBundlePages(ResourceRequest request) {
    return getBundlePage(request.getUri(), Collections.emptyList());
  }





  public Mono<List<String>> getIds(ResourceRequest request) {
    Mono<Bundle> bundleMono = getBundle(request);

    return bundleMono
        .map(bundle -> {
          return BundleMapper.toIds(bundle);
        });
  }

  public Mono<Bundle> searchBundle(ResourceRequest request) {
    String postBody = request.getQueryParametersAsString();
    String uri = request.getSearchUri();

    return webClient.post()
        .uri(uri)
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .accept(MediaType.APPLICATION_JSON)
        .bodyValue(postBody)
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

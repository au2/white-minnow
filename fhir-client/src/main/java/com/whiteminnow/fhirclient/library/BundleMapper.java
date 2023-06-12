package com.whiteminnow.fhirclient.library;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Resource;

import java.util.List;
import java.util.stream.Collectors;

public class BundleMapper {

  public static List<String> toIds(Bundle bundle) {
    return bundle.getEntry().stream()
        .map((entry) -> {
          Resource resource = entry.getResource();
          return resource.getIdPart();
        })
        .collect(Collectors.toList());
  }

}

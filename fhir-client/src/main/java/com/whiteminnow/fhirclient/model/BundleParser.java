package com.whiteminnow.fhirclient.model;

import org.hl7.fhir.r4.model.Bundle;

public interface BundleParser<T> {
  T parse(Bundle bundle);
}

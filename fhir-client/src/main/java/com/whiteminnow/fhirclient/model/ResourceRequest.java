package com.whiteminnow.fhirclient.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;

import java.util.Map;

@Setter
@Getter
@Builder
public class ResourceRequest {

  private String requestResource;
  private String resultResource;

  @Singular
  private Map<String, String> queryParameters;

  public String getQueryParametersAsString() {
    if (queryParameters == null || queryParameters.size() < 1) {
      return null;
    }

    final StringBuilder builder = new StringBuilder();

    queryParameters.forEach((key, value) -> {
      if (builder.length() > 0) {
        builder.append('&');
      }
      builder.append(key);
      builder.append('=');
      builder.append(value);
    });

    return builder.toString();
  }

  public String getUri() {
    final StringBuilder builder = new StringBuilder('/' + requestResource);
    String parameters = getQueryParametersAsString();
    if (parameters != null) {
      builder.append('?');
      builder.append(parameters);
    }
    return builder.toString();
  }

  public String getSearchUri() {
    final StringBuilder builder = new StringBuilder('/' + requestResource);
    builder.append('/');
    builder.append("_search");
    return builder.toString();
  }

}

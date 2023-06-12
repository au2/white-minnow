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

  public String getUri() {
    final StringBuilder builder = new StringBuilder('/' + requestResource);
    final int initialLength = builder.length();

    if (queryParameters != null && queryParameters.size() > 0) {
      queryParameters.forEach((key, value) -> {
        builder.append(builder.length() > initialLength ? '&' : '?');
        builder.append(key);
        builder.append('=');
        builder.append(value);
      });
    }

    return builder.toString();
  }

}

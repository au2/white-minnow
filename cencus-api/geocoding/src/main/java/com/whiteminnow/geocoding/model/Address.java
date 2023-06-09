package com.whiteminnow.geocoding.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Address {
  private int number;
  private String streetName;
  private String streetType;
  private String city;
  private String state;
  private String zip;

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();

    builder.append(number).append(' ');
    builder.append(streetName).append(' ');
    builder.append(streetType).append(' ');
    builder.append(',');

    builder.append(city).append(',');
    builder.append(state).append(',');
    builder.append(zip);

    return builder.toString();
  }
}

package com.whiteminnow.fhirclient.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NdiRequestLine {
  private String lastName;
  private String firstName;
  private String middleInitial;
  private String monthOfBirth;
  private String dayOfBirth;
  private String yearOfBirth;
  private String sex;

  private static String padRight(String value, int n) {
    return String.format("%-" + n + "s", value == null ? " " : value);
  }

  public String toFileLine() {
    StringBuilder builder = new StringBuilder();

    builder.append(padRight(lastName, 20));
    builder.append(padRight(firstName, 15));
    builder.append(middleInitial == null ? " " : middleInitial);
    builder.append(" ".repeat(10)); // future ssn location
    builder.append(monthOfBirth == null ? "  " : monthOfBirth);
    builder.append(dayOfBirth == null ? "  " : dayOfBirth);
    builder.append(yearOfBirth == null ? "    " : yearOfBirth);
    builder.append(" ".repeat(10)); // future age at death location
    builder.append(sex == null ? " " : sex);
    builder.append(" ".repeat(100 - builder.length()));
    return builder.toString();
  }
}

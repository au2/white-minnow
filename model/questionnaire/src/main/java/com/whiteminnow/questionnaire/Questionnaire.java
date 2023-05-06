package com.whiteminnow.questionnaire;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.List;

@Getter
@Builder
public class Questionnaire {
  private String key;

  @Singular
  private List<Question> questions;
}

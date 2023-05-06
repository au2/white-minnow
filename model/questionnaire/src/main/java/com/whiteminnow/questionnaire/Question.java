package com.whiteminnow.questionnaire;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class Question {
  private String key;
  private String text;
  private QuestionType type;
}

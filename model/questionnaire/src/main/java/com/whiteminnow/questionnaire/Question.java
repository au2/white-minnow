package com.whiteminnow.questionnaire;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Question {
  private String key;
  private String text;
  private QuestionType type;
}

package com.whiteminnow.questionnaire.testutil;

import com.whiteminnow.questionnaire.Questionnaire;

import java.util.concurrent.atomic.AtomicInteger;

public class QuestionnaireGenerator {
  private final AtomicInteger counter = new AtomicInteger(0);
  private final QuestionGenerator questionGenerator = new QuestionGenerator();

  public Questionnaire newQuestionnaire() {
    return Questionnaire.builder()
        .key("key_" + counter.incrementAndGet())
        .question(questionGenerator.newQuestion())
        .question(questionGenerator.newQuestion())
        .question(questionGenerator.newQuestion())
        .build();
  }
}

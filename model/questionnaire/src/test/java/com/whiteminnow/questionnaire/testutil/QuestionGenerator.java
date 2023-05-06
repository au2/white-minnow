package com.whiteminnow.questionnaire.testutil;

import com.whiteminnow.questionnaire.Question;
import com.whiteminnow.questionnaire.QuestionType;

import java.util.concurrent.atomic.AtomicInteger;

public class QuestionGenerator {
  private final AtomicInteger counter = new AtomicInteger(0);

  public Question newQuestion() {
    Question question = new Question();
    int index = counter.incrementAndGet();

    question.setType(QuestionType.STRING);
    question.setKey("key_" + index);
    question.setText("text_" + index);

    return question;
  }
}

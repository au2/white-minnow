package com.whiteminnow.questionnaire;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class QuestionTest {
  @Test
  void testLombok() {
    final String QUESTION_KEY = "question_key";
    final String QUESTION_TEXT = "question text";

    for (QuestionType type: QuestionType.values()) {
      Question question = new Question();
      question.setKey(QUESTION_KEY);
      question.setText(QUESTION_TEXT);
      question.setType(type);

      Assertions.assertEquals(QUESTION_KEY, question.getKey());
      Assertions.assertEquals(QUESTION_TEXT, question.getText());
      Assertions.assertEquals(type, question.getType());
    }
  }
}

package com.whiteminnow.questionnaire;

import com.whiteminnow.questionnaire.testutil.QuestionGenerator;
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

  @Test
  void testEquals() {
    QuestionGenerator generator = new QuestionGenerator();

    Question question = generator.newQuestion();

    Question clone = new Question();
    clone.setKey(question.getKey());
    clone.setType(question.getType());
    clone.setText(question.getText());
    Assertions.assertEquals(question, clone);

    clone.setText("not that one");

    Assertions.assertNotEquals(question, clone);
  }
}

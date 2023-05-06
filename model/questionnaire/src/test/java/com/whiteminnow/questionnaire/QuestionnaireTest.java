package com.whiteminnow.questionnaire;

import com.whiteminnow.questionnaire.testutil.QuestionGenerator;
import com.whiteminnow.questionnaire.testutil.QuestionnaireGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class QuestionnaireTest {
  @Test
  void testLombok() {
    final String QUESTIONNAIRE_KEY = "questionnaire_key";

    QuestionGenerator qg = new QuestionGenerator();
    Question q1 = qg.newQuestion();
    Question q2 = qg.newQuestion();

    Questionnaire questionnaire = Questionnaire.builder()
        .key(QUESTIONNAIRE_KEY)
        .question(q1)
        .question(q2)
        .build();

    Assertions.assertEquals(QUESTIONNAIRE_KEY, questionnaire.getKey());
    Assertions.assertEquals(q1, questionnaire.getQuestions().get(0));
    Assertions.assertEquals(q2, questionnaire.getQuestions().get(1));
  }

  @Test
  void testGenerator() {
    QuestionnaireGenerator generator = new QuestionnaireGenerator();
    Questionnaire questionnaire = generator.newQuestionnaire();

    Assertions.assertEquals(3, questionnaire.getQuestions().size());
  }
}

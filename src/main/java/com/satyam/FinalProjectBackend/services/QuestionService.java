package com.satyam.FinalProjectBackend.services;

import com.satyam.FinalProjectBackend.db.QuestionRepo;
import com.satyam.FinalProjectBackend.models.Question;
import com.satyam.FinalProjectBackend.models.Quiz;
import com.satyam.FinalProjectBackend.models.OpenTDBQuestion;
import com.satyam.FinalProjectBackend.models.OpenTDBResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepo questionRepo;

    // ✅ Save a question manually if needed
    public Question saveQuestion(Question question) {
        return questionRepo.save(question);
    }

    // ✅ Get all questions for a specific quiz ID
    public List<Question> getQuestionBasedonQuizId(Long quizId) {
        return questionRepo.findByQuizId(quizId);
    }

    // ✅ Clears previous questions and fetches 10 new ones from OpenTDB
    public void fetchFromOpenTDBAndSave(Quiz quiz) {
        // Clear old questions for this quiz (prevent duplicates or recursive nesting)
        questionRepo.deleteByQuiz(quiz); // ❗ Requires custom method in QuestionRepo

        // OpenTDB API for 10 multiple choice questions
        String url = "https://opentdb.com/api.php?amount=10&type=multiple";

        RestTemplate restTemplate = new RestTemplate();
        OpenTDBResponse response = restTemplate.getForObject(url, OpenTDBResponse.class);

        if (response != null && response.getResults() != null) {
            for (OpenTDBQuestion item : response.getResults()) {
                Question question = new Question();
                question.setQuestionText(item.getQuestion());
                question.setCorrectAnswer(item.getCorrect_answer());

                // Safe handling of wrong options
                List<String> wrong = item.getIncorrect_answers();
                question.setOption1(wrong.size() > 0 ? wrong.get(0) : "");
                question.setOption2(wrong.size() > 1 ? wrong.get(1) : "");
                question.setOption3(wrong.size() > 2 ? wrong.get(2) : "");

                question.setQuiz(quiz);
                questionRepo.save(question);
            }
        }
    }
}

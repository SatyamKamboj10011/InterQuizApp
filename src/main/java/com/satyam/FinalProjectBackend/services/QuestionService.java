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
    public QuestionRepo questionRepo;

    // ✅ Save a question to the database
    public Question saveQuestion(Question question) {
        return questionRepo.save(question); // ❗ fixed missing ')'
    }

    // ✅ Get all questions for a specific quiz
    public List<Question> getQuestionBasedonQuizId(Long quizId) {
        return questionRepo.findByQuizId(quizId);
    }

    // ✅ Fetch 10 questions from OpenTDB and save them for a quiz
    public void fetchFromOpenTDBAndSave(Quiz quiz) {
        // OpenTDB API to get 10 questions
        String url = "https://opentdb.com/api.php?amount=10&type=multiple";

        RestTemplate restTemplate = new RestTemplate();

        // Fetch and map the JSON response to OpenTDBResponse class
        OpenTDBResponse response = restTemplate.getForObject(url, OpenTDBResponse.class);

        // If data is received successfully
        if (response != null && response.getResults() != null) {
            for (OpenTDBQuestion item : response.getResults()) {
                Question question = new Question();

                // Set question text and correct answer
                question.setQuestionText(item.getQuestion());
                question.setCorrectAnswer(item.getCorrect_answer());

                // Use incorrect answers as wrong options
                List<String> wrong = item.getIncorrect_answers();
                question.setOption1(wrong.get(0));
                question.setOption2(wrong.size() > 1 ? wrong.get(1) : "");
                question.setOption3(wrong.size() > 2 ? wrong.get(2) : "");

                // Link this question to the quiz
                question.setQuiz(quiz);

                // Save the question to DB
                questionRepo.save(question);
            }
        }
    }
}

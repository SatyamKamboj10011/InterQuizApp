package com.satyam.FinalProjectBackend.services;

import com.satyam.FinalProjectBackend.db.QuestionRepo;
import com.satyam.FinalProjectBackend.models.Question;
import com.satyam.FinalProjectBackend.models.Quiz;
import com.satyam.FinalProjectBackend.models.OpenTDBQuestion;
import com.satyam.FinalProjectBackend.models.OpenTDBResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepo questionRepo;

    public Question saveQuestion(Question question) {
        return questionRepo.save(question);
    }

    public List<Question> getQuestionBasedonQuizId(Long quizId) {
        return questionRepo.findByQuizId(quizId);
    }

    public void fetchFromOpenTDBAndSave(Quiz quiz) {
        questionRepo.deleteByQuiz(quiz); // Clear old questions

        String url = "https://opentdb.com/api.php?amount=10&type=multiple";
        RestTemplate restTemplate = new RestTemplate();
        OpenTDBResponse response = restTemplate.getForObject(url, OpenTDBResponse.class);

        if (response != null && response.getResults() != null) {
            for (OpenTDBQuestion item : response.getResults()) {
                Question question = new Question();
                question.setQuestionText(item.getQuestion());
                question.setCorrectAnswer(item.getCorrect_answer());

                // Combine all options and shuffle
                List<String> allOptions = new ArrayList<>(item.getIncorrect_answers());
                allOptions.add(item.getCorrect_answer());
                Collections.shuffle(allOptions);

                // Assign options
                question.setOption1(allOptions.get(0));
                question.setOption2(allOptions.get(1));
                question.setOption3(allOptions.get(2));
                question.setOption4(allOptions.get(3));

                question.setQuiz(quiz);
                questionRepo.save(question);
            }
        }
    }
}

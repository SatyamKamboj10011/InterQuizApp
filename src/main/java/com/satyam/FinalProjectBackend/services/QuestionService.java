package com.satyam.FinalProjectBackend.services;

import com.satyam.FinalProjectBackend.db.QuestionRepo;
import com.satyam.FinalProjectBackend.models.Question;
import com.satyam.FinalProjectBackend.models.Quiz;
import com.satyam.FinalProjectBackend.models.OpenTDBQuestion;
import com.satyam.FinalProjectBackend.models.OpenTDBResponse;
import org.apache.commons.text.StringEscapeUtils;
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

        RestTemplate restTemplate = new RestTemplate();

        try {
            // ✅ Fetch multiple choice questions (7)
            String multiUrl = "https://opentdb.com/api.php?amount=10";
            OpenTDBResponse multiResponse = restTemplate.getForObject(multiUrl, OpenTDBResponse.class);
            saveQuestionsFromResponse(multiResponse, quiz);

            // Delay to avoid rate limit
            Thread.sleep(1500);

//            // ✅ Fetch true/false questions (3)
//            String tfUrl = "https://opentdb.com/api.php?amount=3&type=boolean";
//            OpenTDBResponse tfResponse = restTemplate.getForObject(tfUrl, OpenTDBResponse.class);
//            System.out.println("✅ Fetched TF: " + (tfResponse != null ? tfResponse.getResults().size() : 0));
//            saveQuestionsFromResponse(tfResponse, quiz);
        } catch (Exception e) {
            System.err.println("❌ Error fetching questions from OpenTDB: " + e.getMessage());
        }
    }

    private void saveQuestionsFromResponse(OpenTDBResponse response, Quiz quiz) {
        if (response != null && response.getResults() != null) {
            for (OpenTDBQuestion item : response.getResults()) {
                Question question = new Question();
                // ✅ Clean HTML entities in question and correct answer
                question.setQuestionText(StringEscapeUtils.unescapeHtml4(item.getQuestion()));
                question.setCorrectAnswer(StringEscapeUtils.unescapeHtml4(item.getCorrect_answer()));

                // ✅ Mix correct + incorrect answers and shuffle
                List<String> allOptions = new ArrayList<>();
                for (String ans : item.getIncorrect_answers()) {
                    allOptions.add(StringEscapeUtils.unescapeHtml4(ans));
                }
                allOptions.add(StringEscapeUtils.unescapeHtml4(item.getCorrect_answer()));
                Collections.shuffle(allOptions);

                // ⚠️ Safely assign even if TF has only 2 options
                question.setOption1(allOptions.size() > 0 ? allOptions.get(0) : null);
                question.setOption2(allOptions.size() > 1 ? allOptions.get(1) : null);
                question.setOption3(allOptions.size() > 2 ? allOptions.get(2) : null);
                question.setOption4(allOptions.size() > 3 ? allOptions.get(3) : null);

                question.setQuiz(quiz);
                questionRepo.save(question);
            }
        }
    }
}
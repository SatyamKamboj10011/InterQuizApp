package com.satyam.FinalProjectBackend.controller;

import com.satyam.FinalProjectBackend.models.Question;
import com.satyam.FinalProjectBackend.services.QuestionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/question")
public class QuestionController {
    private QuestionService questionService;

    @PostMapping("/generate")
    public Question generateQuestion(@RequestBody Question question) {
        return questionService.saveQuestion(question);
    }

    @GetMapping("/quiz/{quizId}")
    public List<Question> getQuestionsForQuiz(@PathVariable Long quizId) {
        return questionService.getQuestionBasedonQuizId(quizId);
    }
}

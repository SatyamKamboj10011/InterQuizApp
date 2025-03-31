package com.satyam.FinalProjectBackend.controller;

import com.satyam.FinalProjectBackend.models.Quiz;
import com.satyam.FinalProjectBackend.services.QuestionService;
import com.satyam.FinalProjectBackend.services.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/quiz")
public class QuizController {

    @Autowired
    private  QuizService quizService;
    @Autowired
    private QuestionService questionService;

    @PostMapping(value = "/generateQuestions",consumes = "application/json", produces = "application/json")
    public Quiz createQuizWithOpenTDB(@RequestBody Quiz quiz) {
        Quiz savedQuiz = quizService.generateQuiz(quiz); // 1. Save quiz to DB
        questionService.fetchFromOpenTDBAndSave(savedQuiz); // 2. Fetch + save 10 questions linked to quiz
        return savedQuiz; // 3. Return the full saved quiz
    }

    //for generating empty quiz
    @PostMapping("/generate")
    public Quiz generateQuiz(@RequestBody Quiz quiz){
        return quizService.generateQuiz(quiz);
    }

    @GetMapping("/all")
    public List<Quiz> getAllQuiz() {
        return quizService.getAllQuiz();
    }



    @DeleteMapping("/{id}")
    public String deleteQuiz(@PathVariable Long id) {
        quizService.deleteQuiz(id);
        return "Quiz deleted Successfully!";
    }
    @GetMapping("/{id}")
    public ResponseEntity<Quiz> getQuiz(@PathVariable Long id) {
        return quizService.getQuizById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    //QUIZ Dates
    @GetMapping("/active")
    public List<Quiz> getOngoing() {
        return quizService.getOngoingQuizzes();
    }

    @GetMapping("/coming-soon")
    public List<Quiz> getUpcoming() {
        return quizService.getUpcomingQuizzes();
    }

    @GetMapping("/ended")
    public List<Quiz> getPast() {
        return quizService.getPastQuizzes();
    }
}

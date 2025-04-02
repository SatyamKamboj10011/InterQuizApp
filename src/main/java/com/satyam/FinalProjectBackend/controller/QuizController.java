package com.satyam.FinalProjectBackend.controller;

import com.satyam.FinalProjectBackend.db.UserRepo;
import com.satyam.FinalProjectBackend.models.Quiz;
import com.satyam.FinalProjectBackend.models.User;
import com.satyam.FinalProjectBackend.services.QuestionService;
import com.satyam.FinalProjectBackend.services.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/quiz")
public class QuizController {

    @Autowired
    private  QuizService quizService;
    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserRepo userRepo;
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


    @PostMapping("/quiz/like/{quizId}/{userId}")
    public ResponseEntity<?> likeQuiz(@PathVariable Long quizId, @PathVariable Long userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.getLikedQuizId().add(quizId);
        userRepo.save(user);
        return ResponseEntity.ok("Quiz liked!");
    }

    @DeleteMapping("/quiz/unlike/{quizId}/{userId}")
    public ResponseEntity<?> unlikeQuiz(@PathVariable Long quizId, @PathVariable Long userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.getLikedQuizId().remove(quizId);
        userRepo.save(user);
        return ResponseEntity.ok("Quiz unliked!");
    }

    @GetMapping("/quiz/liked/{userId}")
    public ResponseEntity<Set<Long>> getLikedQuizIds(@PathVariable Long userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(user.getLikedQuizId());
    }
}


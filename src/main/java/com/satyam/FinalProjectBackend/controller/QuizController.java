package com.satyam.FinalProjectBackend.controller;

import com.satyam.FinalProjectBackend.db.QuizRepo;
import com.satyam.FinalProjectBackend.db.ScoreRepo;
import com.satyam.FinalProjectBackend.db.UserRepo;
import com.satyam.FinalProjectBackend.models.Quiz;
import com.satyam.FinalProjectBackend.models.Score;
import com.satyam.FinalProjectBackend.models.User;
import com.satyam.FinalProjectBackend.services.QuestionService;
import com.satyam.FinalProjectBackend.services.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/quiz")
public class QuizController {

    @Autowired
    private  QuizService quizService;
    @Autowired
    private QuestionService questionService;

    @Autowired
    private ScoreRepo scoreRepo;

    @Autowired
    private QuizRepo quizRepo;
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

    @PutMapping("/{id}")
    public ResponseEntity<?> updateQuiz(
            @PathVariable Long id,
            @RequestBody Quiz updatedQuizData) {

        Optional<Quiz> optionalQuiz = quizRepo.findById(id);
        if (optionalQuiz.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Quiz not found");
        }

        Quiz quiz = optionalQuiz.get();

        // Only update the allowed fields
        quiz.setName(updatedQuizData.getName());
        quiz.setStartDate(updatedQuizData.getStartDate());
        quiz.setEndDate(updatedQuizData.getEndDate());

        quizRepo.save(quiz);
        return ResponseEntity.ok("Quiz updated successfully");
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


    @GetMapping("/{id}/stats")
    public ResponseEntity<?> getQuizStats(@PathVariable Long id) {
        Optional<Quiz> quiz = quizRepo.findById(id);
        if (quiz.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Quiz not found");
        }

        Map<String, Object> stats = new HashMap<>();

        // Total players & average score (use your logic if already exists)
        List<Score> scores = scoreRepo.findByQuizId(id);
        int totalPlayers = scores.size();
        double averageScore = scores.stream().mapToDouble(Score::getScore).average().orElse(0.0);

        // ✅ Likes: Count users where likedQuizId contains this quiz's id
        long likes = userRepo.countByLikedQuizIdContains(id);

        stats.put("totalPlayers", totalPlayers);
        stats.put("averageScore", averageScore);
        stats.put("likes", likes);
        stats.put("scores", scores.stream().map(s -> Map.of(
                "playerName", s.getPlayer().getUsername(),
                "score", s.getScore(),
                "date", s.getCompletedDate()
        )).toList());

        return ResponseEntity.ok(stats);
    }

}


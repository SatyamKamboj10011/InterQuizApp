package com.satyam.FinalProjectBackend.controller;

import com.satyam.FinalProjectBackend.models.Score;
import com.satyam.FinalProjectBackend.services.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/score")
@CrossOrigin(origins = "http://localhost:3000")
public class ScoreController {

    @Autowired
    private ScoreService scoreService;

    // Submit a score
    @PostMapping("/submit")
    public ResponseEntity<?> submitScore(@RequestBody Score score) {
        try {
            System.out.println("Received Answer History:");
            if (score.getAnswerHistory() != null) {
                score.getAnswerHistory().forEach(a -> {
                    System.out.println("Q: " + a.getQuestion());
                    System.out.println("Selected: " + a.getSelectedAnswer());
                    System.out.println("Correct: " + a.getCorrectAnswer());
                    System.out.println("Correct? " + a.isCorrect());
                });
            }
            Score saved = scoreService.saveScore(score);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving score.");
        }
    }


    // Get all scores for a specific quiz
    @GetMapping("/quiz/{quizId}")
    public List<Score> getScoresByQuiz(@PathVariable Long quizId) {
        return scoreService.getScoresByQuizId(quizId);
    }

    // Get all scores for a specific player
    @GetMapping("/player/{playerId}")
    public List<Score> getScoresByPlayer(@PathVariable Long playerId) {
        return scoreService.getScoresByPlayerId(playerId);
    }

    // ✅ New Endpoint - Get specific score by playerId and scoreId
    @GetMapping("/details/{playerId}/{scoreId}")
    public ResponseEntity<?> getScoreDetails(@PathVariable Long playerId, @PathVariable Long scoreId) {
        Optional<Score> scoreOptional = scoreService.getScoreByPlayerAndScoreId(playerId, scoreId);

        if (scoreOptional.isPresent()) {
            return ResponseEntity.ok(scoreOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Score not found for the given player and score ID.");
        }
    }
}

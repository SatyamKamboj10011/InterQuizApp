package com.satyam.FinalProjectBackend.controller;

import com.satyam.FinalProjectBackend.models.Score;
import com.satyam.FinalProjectBackend.services.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/score")
public class ScoreController {

    @Autowired

    private ScoreService scoreService;

    @PostMapping("/submit")
    public Score submitScore(@RequestBody Score score){
        return scoreService.saveScore(score);
    }

    @GetMapping("/quiz/{quizId}")
    public List<Score> getScoresByQuiz(@PathVariable Long quizId) {
        return scoreService.getScoresByQuizId(quizId);
    }

    @GetMapping("/player/{playerId}")
    public List<Score> getScoresByPlayer(@PathVariable Long playerId) {
        return scoreService.getScoresByPlayerId(playerId);
    }
}

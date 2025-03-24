package com.satyam.FinalProjectBackend.services;

import com.satyam.FinalProjectBackend.db.QuizRepo;
import com.satyam.FinalProjectBackend.db.ScoreRepo;
import com.satyam.FinalProjectBackend.db.UserRepo;
import com.satyam.FinalProjectBackend.models.Quiz;
import com.satyam.FinalProjectBackend.models.Score;
import com.satyam.FinalProjectBackend.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ScoreService {
    @Autowired
    private ScoreRepo scoreRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private QuizRepo quizRepo;

  public Score saveScore(Score score){
// ✅ Fetch full player and quiz data before saving
          User fullUser = userRepo.findById(score.getPlayer().getId()).orElse(null);
          Quiz fullQuiz = quizRepo.findById(score.getQuiz().getId()).orElse(null);

          score.setPlayer(fullUser);
          score.setQuiz(fullQuiz);

      return scoreRepo.save(score);
  }

    public List<Score> getScoresByQuizId(Long quizId) {
        return new ArrayList<>(scoreRepo.findByQuizId(quizId));
    }

    public List<Score> getScoresByPlayerId(Long playerId) {
        return new ArrayList<>(scoreRepo.findByPlayerId(playerId));
    }


}

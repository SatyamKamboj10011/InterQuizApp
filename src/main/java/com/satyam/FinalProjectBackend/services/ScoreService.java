package com.satyam.FinalProjectBackend.services;

import com.satyam.FinalProjectBackend.db.AnswerHistoryRepo;
import com.satyam.FinalProjectBackend.db.QuizRepo;
import com.satyam.FinalProjectBackend.db.ScoreRepo;
import com.satyam.FinalProjectBackend.db.UserRepo;
import com.satyam.FinalProjectBackend.models.AnswerHistory;
import com.satyam.FinalProjectBackend.models.Quiz;
import com.satyam.FinalProjectBackend.models.Score;
import com.satyam.FinalProjectBackend.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.ParameterResolutionDelegate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ScoreService {
    @Autowired
    private ScoreRepo scoreRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private QuizRepo quizRepo;

    @Autowired
    private AnswerHistoryRepo answerHistoryRepo;

  public Score saveScore(Score score){
// ✅ Fetch full player and quiz data before saving
          User fullUser = userRepo.findById(score.getPlayer().getId()).orElse(null);
          Quiz fullQuiz = quizRepo.findById(score.getQuiz().getId()).orElse(null);

          if (fullUser == null || fullQuiz == null){
              throw new IllegalArgumentException("Player or Quiz not Found!❌");
          }
          score.setPlayer(fullUser);
          score.setQuiz(fullQuiz);

      Score savedScore = scoreRepo.save(score);
      // Save each answer history item associated with the score
      if (score.getAnswerHistory() != null) {
          for (AnswerHistory answer : score.getAnswerHistory()) {
              answer.setScore(savedScore);
              answerHistoryRepo.save(answer);
          }
      }
      return savedScore;
  }

    public List<Score> getScoresByQuizId(Long quizId) {
        return new ArrayList<>(scoreRepo.findByQuizId(quizId));
    }

    public List<Score> getScoresByPlayerId(Long playerId) {
        return new ArrayList<>(scoreRepo.findByPlayerId(playerId));
    }
    public Optional<Score> getScoreByPlayerAndScoreId(Long playerId, Long scoreId) {
        return scoreRepo.findByIdAndPlayerId(scoreId, playerId);
    }
    // ✅ Get Answer History by Score ID
    public List<AnswerHistory> getAnswerHistoryByScoreId(Long scoreId) {
        return answerHistoryRepo.findByScoreId(scoreId);
    }

}

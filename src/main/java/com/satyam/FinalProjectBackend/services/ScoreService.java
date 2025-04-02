package com.satyam.FinalProjectBackend.services;

import com.satyam.FinalProjectBackend.db.AnswerHistoryRepo;
import com.satyam.FinalProjectBackend.db.QuizRepo;
import com.satyam.FinalProjectBackend.db.ScoreRepo;
import com.satyam.FinalProjectBackend.db.UserRepo;
import com.satyam.FinalProjectBackend.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.ParameterResolutionDelegate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.satyam.FinalProjectBackend.models.QuizSummary;
import com.satyam.FinalProjectBackend.models.PlayerScore;


import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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

              // ✅ Compare selected and correct answers
              boolean isCorrect = answer.getSelectedAnswer() != null &&
                      answer.getSelectedAnswer().trim().equalsIgnoreCase(
                              answer.getCorrectAnswer().trim()
                      );

              answer.setCorrect(isCorrect);
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


    public List<Leaderboard> getLeaderboard() {
        List<Score> topScores = scoreRepo.findTop10ByOrderByScoreDesc();
        List<Leaderboard> leaderboard = new ArrayList<>();

        for (Score score : topScores) {
            String player = score.getPlayer() != null ? score.getPlayer().getUsername() : "Unknown";

            String quiz = score.getQuiz() != null ? score.getQuiz().getName() : "Unknown";
            LocalDateTime completedDate = score.getCompletedDate();
            leaderboard.add(new Leaderboard(player, quiz, score.getScore(), completedDate));
            System.out.println("Returning leaderboard with date: " + score.getCompletedDate());

        }

        return leaderboard;
    }
    public QuizSummary getQuizScoreSummary(Long quizId) {
        List<Score> scores = scoreRepo.findByQuizIdOrderByScoreDesc(quizId);

        int totalPlayers = scores.size();
        int averageScore = totalPlayers > 0
                ? (int) Math.round(scores.stream()
                .mapToInt(Score::getScore)
                .average()
                .orElse(0.0))
                : 0;


        Set<Long> likedIds = userRepo.findAll().stream()
                .filter(u -> u.getLikedQuizId().contains(quizId))
                .map(User::getId)
                .collect(Collectors.toSet());

        List<PlayerScore> playerScores = scores.stream().map(score ->
        new PlayerScore(
                score.getPlayer().getUsername(),
                score.getCompletedDate(),
                score.getScore()
        )
        ).collect(Collectors.toList());

        return new QuizSummary(totalPlayers,averageScore, likedIds.size(), playerScores);
    }


}

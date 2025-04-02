package com.satyam.FinalProjectBackend.db;

import com.satyam.FinalProjectBackend.models.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ScoreRepo extends JpaRepository<Score, Long> {

    List<Score> findByQuizId(Long quizId);
    List<Score> findByPlayerId(Long playerId);
    // ✅ Find specific score by playerId and scoreId
    Optional<Score> findByIdAndPlayerId(Long scoreId, Long playerId);

    List<Score> findTop10ByOrderByScoreDesc();

    List<Score> findByQuizIdOrderByScoreDesc(Long quizId);
}



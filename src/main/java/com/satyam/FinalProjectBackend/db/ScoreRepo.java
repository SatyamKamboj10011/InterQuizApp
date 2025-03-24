package com.satyam.FinalProjectBackend.db;

import com.satyam.FinalProjectBackend.models.Score;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ScoreRepo extends CrudRepository<Score, Long> {

    List<Score> findByQuizId(Long quizId);
    List<Score> findByPlayerId(Long playerId);
}

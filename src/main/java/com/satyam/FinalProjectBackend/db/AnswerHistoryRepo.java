package com.satyam.FinalProjectBackend.db;

import com.satyam.FinalProjectBackend.models.AnswerHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerHistoryRepo extends JpaRepository <AnswerHistory, Long> {
    List<AnswerHistory> findByScoreId(Long scoreId);
}

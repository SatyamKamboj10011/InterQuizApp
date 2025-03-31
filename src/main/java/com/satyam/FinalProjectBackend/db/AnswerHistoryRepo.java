package com.satyam.FinalProjectBackend.db;

import com.satyam.FinalProjectBackend.models.AnswerHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerHistoryRepo extends JpaRepository <AnswerHistory, Long> {

}

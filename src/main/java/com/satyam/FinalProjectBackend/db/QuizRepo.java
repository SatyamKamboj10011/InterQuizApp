package com.satyam.FinalProjectBackend.db;

import com.satyam.FinalProjectBackend.models.Quiz;
import org.springframework.data.repository.CrudRepository;

public interface QuizRepo extends CrudRepository<Quiz, Long> {
}

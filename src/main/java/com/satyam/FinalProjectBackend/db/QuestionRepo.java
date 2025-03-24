package com.satyam.FinalProjectBackend.db;

import com.satyam.FinalProjectBackend.models.Question;
import com.satyam.FinalProjectBackend.models.Quiz;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface QuestionRepo extends CrudRepository<Question, Long> {
    List<Question> findByQuizId(Long quizId);
    void deleteByQuiz(Quiz quiz);
}

package com.satyam.FinalProjectBackend.db;

import com.satyam.FinalProjectBackend.models.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface QuizRepo extends JpaRepository<Quiz, Long> {
    List<Quiz> findByStartDateLessThanEqualAndEndDateGreaterThanEqual(LocalDate start, LocalDate end);

    List<Quiz> findByStartDateAfter(LocalDate now); // quizzes that are upcoming
    List<Quiz> findByEndDateBefore(LocalDate now); // quizzess in the past

    @Query("SELECT q FROM com.satyam.FinalProjectBackend.models.Quiz q LEFT JOIN FETCH q.questions")
    List<Quiz> findAllWithQuestions();


}

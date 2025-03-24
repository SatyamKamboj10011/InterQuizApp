package com.satyam.FinalProjectBackend.services;

import com.satyam.FinalProjectBackend.db.QuizRepo;
import com.satyam.FinalProjectBackend.models.Quiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {

    @Autowired
    public QuizRepo quizRepo;

    public Quiz generateQuiz(Quiz quiz){
        return quizRepo.save(quiz);
    }
//    public List<Quiz> getAllQuiz(){
//        List<Quiz> quizzes = new ArrayList<>();
//        quizRepo.findAll().forEach(quizzes::add);
//        return quizzes;
//    }

    public Optional<Quiz> getQuizById(Long id) {
        return quizRepo.findById(id);
    }

    public void deleteQuiz(Long id) {
        quizRepo.deleteById(id);
    }

    public List<Quiz> getOngoingQuizzes() {
        LocalDate today = LocalDate.now();
        return new ArrayList<>(quizRepo.findByStartDateBeforeAndEndDateAfter(today, today));
    }

    public List<Quiz> getUpcomingQuizzes() {
        LocalDate today = LocalDate.now();
        return new ArrayList<>(quizRepo.findByStartDateAfter(today));
    }

    public List<Quiz> getPastQuizzes() {
        LocalDate today = LocalDate.now();
        return new ArrayList<>(quizRepo.findByEndDateBefore(today));
    }

    public List<Quiz> getAllQuiz() {
        return quizRepo.findAllWithQuestions();
    }

}

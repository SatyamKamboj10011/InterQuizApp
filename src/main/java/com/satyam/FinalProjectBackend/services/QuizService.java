package com.satyam.FinalProjectBackend.services;

import com.satyam.FinalProjectBackend.db.QuizRepo;
import com.satyam.FinalProjectBackend.db.UserRepo;
import com.satyam.FinalProjectBackend.models.Quiz;
import com.satyam.FinalProjectBackend.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {

    @Autowired
    public QuizRepo quizRepo;
    @Autowired
    public UserRepo userRepo;
    @Autowired EmailService emailService;

    public Quiz generateQuiz(Quiz quiz){
        List<User> users = userRepo.findAll();
        String subject = "🎉 New Quiz Created: " + quiz.getName();
        String body = "Hello,\n\n" +
                "A new quiz titled \"" + quiz.getName() + "\" has been created!\n" +
                "Category: " + quiz.getCategory() + "\n" +
                "Difficulty: " + quiz.getDifficulty() + "\n" +
                "Start Date: " + quiz.getStartDate() + "\n" +
                "End Date: " + quiz.getEndDate() + "\n\n" +
                "Get ready to participate and showcase your knowledge!\n\n" +
                "Best Regards, \n" +" Satyam Kamboj, \n"+

                "Quiz App Team";

        for (User user : users) {
            if (user.getEmail() != null && !user.getEmail().endsWith("@example.com")) {
                emailService.sendEmail(user.getEmail(), subject, body);
            }
        }

        return  quizRepo.save(quiz);
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
        LocalDateTime now = LocalDateTime.now();
        List<Quiz> allQuizzes = quizRepo.findAll();

        List<Quiz> ongoingQuizzes = new ArrayList<>();
        for (Quiz quiz : allQuizzes) {
            if (quiz.getStartDate() != null && quiz.getEndDate() != null) {
                if (!quiz.getStartDate().isAfter(now) && !quiz.getEndDate().isBefore(now)) {
                    ongoingQuizzes.add(quiz);
                }
            }
        }
        return ongoingQuizzes;
    }


    public List<Quiz> getUpcomingQuizzes() {
        LocalDateTime now = LocalDateTime.now();
        List<Quiz> allQuizzes = quizRepo.findAll();

        List<Quiz> upcoming = new ArrayList<>();
        for (Quiz quiz : allQuizzes) {
            if (quiz.getStartDate() != null && quiz.getStartDate().isAfter(now)) {
                upcoming.add(quiz);
            }
        }
        return upcoming;
    }

    public List<Quiz> getPastQuizzes() {
        LocalDateTime now = LocalDateTime.now();
        List<Quiz> allQuizzes = quizRepo.findAll();

        List<Quiz> past = new ArrayList<>();
        for (Quiz quiz : allQuizzes) {
            if (quiz.getEndDate() != null && quiz.getEndDate().isBefore(now)) {
                past.add(quiz);
            }
        }
        return past;
    }

    public List<Quiz> getAllQuiz() {
        return quizRepo.findAllWithQuestions();
    }

}

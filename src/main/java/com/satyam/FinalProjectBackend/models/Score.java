package com.satyam.FinalProjectBackend.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "score")
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User player;

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz  quiz;

    private int score;
    private LocalDateTime completedDate;

    private int totalQuestions;

    @OneToMany(mappedBy = "score", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<AnswerHistory> answerHistory =new ArrayList<>();

    public Score(){

    }

    public Score(int totalQuestions, List<AnswerHistory> answerHistory) {
        this.totalQuestions = totalQuestions;
        this.answerHistory = answerHistory;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public List<AnswerHistory> getAnswerHistory() {
        return answerHistory;
    }

    public void setAnswerHistory(List<AnswerHistory> answerHistory) {
        this.answerHistory = answerHistory;
    }

    public Score(Long id, User player, Quiz quiz, int score, LocalDateTime completedDate) {
        this.id = id;
        this.player = player;
        this.quiz = quiz;
        this.score = score;
        this.completedDate = completedDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getPlayer() {
        return player;
    }

    public void setPlayer(User player) {
        this.player = player;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public LocalDateTime getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(LocalDateTime completedDate) {
        this.completedDate = completedDate;
    }
}

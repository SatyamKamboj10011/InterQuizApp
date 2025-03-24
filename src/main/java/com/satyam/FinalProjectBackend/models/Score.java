package com.satyam.FinalProjectBackend.models;

import jakarta.persistence.*;

import java.time.LocalDate;

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
    private LocalDate completedDate;

    public Score(){

    }

    public Score(Long id, User player, Quiz quiz, int score, LocalDate completedDate) {
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

    public LocalDate getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(LocalDate completedDate) {
        this.completedDate = completedDate;
    }
}

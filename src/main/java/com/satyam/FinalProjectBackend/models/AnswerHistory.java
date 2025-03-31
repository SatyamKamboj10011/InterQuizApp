package com.satyam.FinalProjectBackend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class AnswerHistory
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "score_id")
    @JsonBackReference
    private Score score;

    private String question;
    private String selectedAnswer;
    private String correctAnswer;
    private boolean isCorrect;

    public AnswerHistory(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public  AnswerHistory(){

    }
    public AnswerHistory(Long id, Score score, String question, String selecteAnswer, boolean isCorrect) {
        this.id = id;
        this.score = score;
        this.question = question;
        this.selectedAnswer = selecteAnswer;
        this.isCorrect = isCorrect;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Score getScore() {
        return score;
    }

    public void setScore(Score score) {
        this.score = score;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getSelecteAnswer() {
        return selectedAnswer;
    }

    public void setSelecteAnswer(String selecteAnswer) {
        this.selectedAnswer = selecteAnswer;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }
}


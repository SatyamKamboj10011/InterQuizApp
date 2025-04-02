package com.satyam.FinalProjectBackend.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class OpenTDBQuestion {
    private String question;
    private String correct_answer;
    private List<String> incorrect_answers;

    @JsonProperty("type")
    private String type;
    public String getQuestion() {
        return question;
    }

    public OpenTDBQuestion(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCorrect_answer() {
        return correct_answer;
    }

    public void setCorrect_answer(String correct_answer) {
        this.correct_answer = correct_answer;
    }

    public List<String> getIncorrect_answers() {
        return incorrect_answers;
    }

    public void setIncorrect_answers(List<String> incorrect_answers) {
        this.incorrect_answers = incorrect_answers;
    }
}

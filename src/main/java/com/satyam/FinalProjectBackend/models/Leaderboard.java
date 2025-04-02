package com.satyam.FinalProjectBackend.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class Leaderboard {
    public String playerName;
    public String quizName;
    public int score;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    public LocalDateTime completedDate; // ✅ use this name exactly

    public Leaderboard(String playerName, String quizName, int score, LocalDateTime completedDate) {
        this.playerName = playerName;
        this.quizName = quizName;
        this.score = score;
        this.completedDate = completedDate; // ✅ fixed assignment
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getQuizName() {
        return quizName;
    }

    public int getScore() {
        return score;
    }

    public LocalDateTime getCompletedDate() { // ✅ exact getter
        return completedDate;
    }

    public void setCompletedDate(LocalDateTime completedDate) {
        this.completedDate = completedDate;
    }
}

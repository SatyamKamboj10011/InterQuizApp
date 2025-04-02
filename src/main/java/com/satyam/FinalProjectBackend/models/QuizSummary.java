package com.satyam.FinalProjectBackend.models;

import java.util.List;

public class QuizSummary {

    private int totalPlayers;
    private double averageScore;
    private int numberOfLikes;
    private List<PlayerScore> playerScores;

    public QuizSummary(double averageScore, int totalPlayers, int numberOfLikes, List<PlayerScore> playerScores) {
        this.averageScore = averageScore;
        this.totalPlayers = totalPlayers;
        this.numberOfLikes = numberOfLikes;
        this.playerScores = playerScores;
    }

    public int getTotalPlayers() {
        return totalPlayers;
    }

    public void setTotalPlayers(int totalPlayers) {
        this.totalPlayers = totalPlayers;
    }

    public double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(double averageScore) {
        this.averageScore = averageScore;
    }

    public int getNumberOfLikes() {
        return numberOfLikes;
    }

    public void setNumberOfLikes(int numberOfLikes) {
        this.numberOfLikes = numberOfLikes;
    }

    public List<PlayerScore> getPlayerScores() {
        return playerScores;
    }

    public void setPlayerScores(List<PlayerScore> playerScores) {
        this.playerScores = playerScores;
    }
}


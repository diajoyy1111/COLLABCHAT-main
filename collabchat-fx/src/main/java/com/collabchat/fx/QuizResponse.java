package com.collabchat.fx;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * QuizResponse represents a student's response to a quiz
 */
public class QuizResponse {
    
    private String quizId;
    private int userId;
    private int selectedAnswer;
    private LocalDateTime responseTime;
    private boolean isCorrect;
    
    public QuizResponse(String quizId, int userId, int selectedAnswer, LocalDateTime responseTime) {
        this.quizId = quizId;
        this.userId = userId;
        this.selectedAnswer = selectedAnswer;
        this.responseTime = responseTime;
        this.isCorrect = false; // Will be set when quiz is graded
    }
    
    /**
     * Set whether the response is correct
     * 
     * @param correctAnswer The correct answer index
     */
    public void gradeResponse(int correctAnswer) {
        this.isCorrect = (selectedAnswer == correctAnswer);
    }
    
    // Getters and Setters
    public String getQuizId() {
        return quizId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public int getSelectedAnswer() {
        return selectedAnswer;
    }
    
    public LocalDateTime getResponseTime() {
        return responseTime;
    }
    
    public boolean isCorrect() {
        return isCorrect;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        QuizResponse that = (QuizResponse) obj;
        return userId == that.userId && Objects.equals(quizId, that.quizId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(quizId, userId);
    }
    
    @Override
    public String toString() {
        return String.format("QuizResponse{quiz='%s', user=%d, answer=%d, correct=%s}",
            quizId, userId, selectedAnswer, isCorrect);
    }
}

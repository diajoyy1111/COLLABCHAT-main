package com.collabchat.fx;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Quiz represents a single quiz question
 */
public class Quiz {
    
    private String quizId;
    private int groupId;
    private int teacherId;
    private String question;
    private List<String> options;
    private int correctAnswer;
    private int timeLimit; // in seconds
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean isActive;
    
    public Quiz(String quizId, int groupId, int teacherId, String question, 
                List<String> options, int correctAnswer, int timeLimit) {
        this.quizId = quizId;
        this.groupId = groupId;
        this.teacherId = teacherId;
        this.question = question;
        this.options = options;
        this.correctAnswer = correctAnswer;
        this.timeLimit = timeLimit;
        this.startTime = LocalDateTime.now();
        this.isActive = true;
    }
    
    /**
     * End the quiz
     */
    public void endQuiz() {
        this.isActive = false;
        this.endTime = LocalDateTime.now();
    }
    
    /**
     * Check if quiz is still active (within time limit)
     * 
     * @return true if quiz is active
     */
    public boolean isActive() {
        if (!isActive) {
            return false;
        }
        
        // Check if time limit has been exceeded
        if (timeLimit > 0) {
            LocalDateTime expirationTime = startTime.plusSeconds(timeLimit);
            if (LocalDateTime.now().isAfter(expirationTime)) {
                endQuiz();
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Get remaining time in seconds
     * 
     * @return Remaining time or -1 if no time limit
     */
    public long getRemainingTime() {
        if (timeLimit <= 0) {
            return -1;
        }
        
        LocalDateTime expirationTime = startTime.plusSeconds(timeLimit);
        LocalDateTime now = LocalDateTime.now();
        
        if (now.isAfter(expirationTime)) {
            return 0;
        }
        
        return java.time.Duration.between(now, expirationTime).getSeconds();
    }
    
    // Getters and Setters
    public String getQuizId() {
        return quizId;
    }
    
    public int getGroupId() {
        return groupId;
    }
    
    public int getTeacherId() {
        return teacherId;
    }
    
    public String getQuestion() {
        return question;
    }
    
    public List<String> getOptions() {
        return options;
    }
    
    public int getCorrectAnswer() {
        return correctAnswer;
    }
    
    public int getTimeLimit() {
        return timeLimit;
    }
    
    public LocalDateTime getStartTime() {
        return startTime;
    }
    
    public LocalDateTime getEndTime() {
        return endTime;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Quiz quiz = (Quiz) obj;
        return Objects.equals(quizId, quiz.quizId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(quizId);
    }
    
    @Override
    public String toString() {
        return String.format("Quiz{id='%s', group=%d, question='%s', active=%s}",
            quizId, groupId, question, isActive);
    }
}

package com.collabchat.fx;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * QuizResults contains the results and statistics for a completed quiz
 */
public class QuizResults {
    
    private String quizId;
    private int totalResponses;
    private int correctResponses;
    private List<QuizResponse> responses;
    private double averageScore;
    private Map<Integer, Long> answerDistribution;
    
    public QuizResults(String quizId, int totalResponses, int correctResponses, List<QuizResponse> responses) {
        this.quizId = quizId;
        this.totalResponses = totalResponses;
        this.correctResponses = correctResponses;
        this.responses = responses;
        this.averageScore = totalResponses > 0 ? (double) correctResponses / totalResponses * 100 : 0;
        this.answerDistribution = calculateAnswerDistribution();
    }
    
    /**
     * Calculate the distribution of answers
     * 
     * @return Map of answer index to count
     */
    private Map<Integer, Long> calculateAnswerDistribution() {
        return responses.stream()
            .collect(Collectors.groupingBy(
                QuizResponse::getSelectedAnswer,
                Collectors.counting()
            ));
    }
    
    /**
     * Get the percentage of correct responses
     * 
     * @return Percentage as a double (0-100)
     */
    public double getCorrectPercentage() {
        return averageScore;
    }
    
    /**
     * Get the most popular answer
     * 
     * @return The answer index that was selected most often
     */
    public int getMostPopularAnswer() {
        return answerDistribution.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(-1);
    }
    
    // Getters
    public String getQuizId() {
        return quizId;
    }
    
    public int getTotalResponses() {
        return totalResponses;
    }
    
    public int getCorrectResponses() {
        return correctResponses;
    }
    
    public List<QuizResponse> getResponses() {
        return responses;
    }
    
    public double getAverageScore() {
        return averageScore;
    }
    
    public Map<Integer, Long> getAnswerDistribution() {
        return answerDistribution;
    }
    
    @Override
    public String toString() {
        return String.format("QuizResults{quiz='%s', total=%d, correct=%d, percentage=%.1f%%}",
            quizId, totalResponses, correctResponses, averageScore);
    }
}

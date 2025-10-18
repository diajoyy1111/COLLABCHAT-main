package com.collabchat.fx;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * QuizManager handles live quiz functionality
 * Teachers can create and send quizzes to students in real-time
 */
public class QuizManager {
    
    private Map<Integer, List<Quiz>> groupQuizzes;
    private Map<String, Quiz> activeQuizzes;
    private Map<String, List<QuizResponse>> quizResponses;
    
    public QuizManager() {
        this.groupQuizzes = new ConcurrentHashMap<>();
        this.activeQuizzes = new ConcurrentHashMap<>();
        this.quizResponses = new ConcurrentHashMap<>();
        
        System.out.println("📝 QuizManager initialized");
    }
    
    /**
     * Create and send a quiz to a group
     * 
     * @param groupId The group ID
     * @param teacherId The teacher's user ID
     * @param question The quiz question
     * @param options The answer options
     * @param correctAnswer The index of the correct answer (0-based)
     * @param timeLimit Time limit in seconds
     * @return The created Quiz
     */
    public Quiz createQuiz(int groupId, int teacherId, String question, 
                          List<String> options, int correctAnswer, int timeLimit) {
        String quizId = generateQuizId();
        Quiz quiz = new Quiz(quizId, groupId, teacherId, question, options, correctAnswer, timeLimit);
        
        groupQuizzes.computeIfAbsent(groupId, k -> new ArrayList<>()).add(quiz);
        activeQuizzes.put(quizId, quiz);
        quizResponses.put(quizId, new ArrayList<>());
        
        System.out.println("📝 Created quiz for group " + groupId + " (Quiz ID: " + quizId + ")");
        return quiz;
    }
    
    /**
     * Submit a quiz response
     * 
     * @param quizId The quiz ID
     * @param userId The user ID
     * @param selectedAnswer The selected answer index
     * @return The QuizResponse
     */
    public QuizResponse submitResponse(String quizId, int userId, int selectedAnswer) {
        Quiz quiz = activeQuizzes.get(quizId);
        if (quiz != null && quiz.isActive()) {
            QuizResponse response = new QuizResponse(quizId, userId, selectedAnswer, LocalDateTime.now());
            quizResponses.get(quizId).add(response);
            
            System.out.println("📝 User " + userId + " submitted response for quiz " + quizId);
            return response;
        }
        return null;
    }
    
    /**
     * Get active quiz for a group
     * 
     * @param groupId The group ID
     * @return The active Quiz or null if none
     */
    public Quiz getActiveQuiz(int groupId) {
        return groupQuizzes.getOrDefault(groupId, new ArrayList<>())
            .stream()
            .filter(Quiz::isActive)
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Get quiz responses
     * 
     * @param quizId The quiz ID
     * @return List of responses
     */
    public List<QuizResponse> getQuizResponses(String quizId) {
        return quizResponses.getOrDefault(quizId, new ArrayList<>());
    }
    
    /**
     * End a quiz
     * 
     * @param quizId The quiz ID
     * @return true if successfully ended
     */
    public boolean endQuiz(String quizId) {
        Quiz quiz = activeQuizzes.get(quizId);
        if (quiz != null) {
            quiz.endQuiz();
            System.out.println("🏁 Ended quiz " + quizId);
            return true;
        }
        return false;
    }
    
    /**
     * Get quiz results
     * 
     * @param quizId The quiz ID
     * @return QuizResults object
     */
    public QuizResults getQuizResults(String quizId) {
        Quiz quiz = activeQuizzes.get(quizId);
        List<QuizResponse> responses = quizResponses.getOrDefault(quizId, new ArrayList<>());
        
        if (quiz != null) {
            int totalResponses = responses.size();
            int correctResponses = (int) responses.stream()
                .filter(r -> r.getSelectedAnswer() == quiz.getCorrectAnswer())
                .count();
            
            return new QuizResults(quizId, totalResponses, correctResponses, responses);
        }
        
        return null;
    }
    
    private String generateQuizId() {
        return "QUIZ_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000);
    }
    
    /**
     * Get quiz statistics
     * 
     * @return String containing statistics
     */
    public String getStatistics() {
        int totalQuizzes = groupQuizzes.values().stream()
            .mapToInt(List::size)
            .sum();
        
        int activeQuizCount = activeQuizzes.size();
        
        return String.format("Total Quizzes: %d | Active Quizzes: %d | Groups with Quizzes: %d",
            totalQuizzes, activeQuizCount, groupQuizzes.size());
    }
}

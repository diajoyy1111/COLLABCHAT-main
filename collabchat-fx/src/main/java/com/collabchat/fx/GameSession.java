package com.collabchat.fx;

import java.time.LocalDateTime;
import java.util.*;

/**
 * GameSession represents a single game session for a group
 */
public class GameSession {
    
    private String gameId;
    private int groupId;
    private GameManager.GameType gameType;
    private int hostUserId;
    private Set<Integer> players;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean isActive;
    private Map<String, Object> gameState;
    private Map<Integer, Integer> scores;
    
    public GameSession(String gameId, int groupId, GameManager.GameType gameType, int hostUserId) {
        this.gameId = gameId;
        this.groupId = groupId;
        this.gameType = gameType;
        this.hostUserId = hostUserId;
        this.players = new HashSet<>();
        this.startTime = LocalDateTime.now();
        this.isActive = true;
        this.gameState = new HashMap<>();
        this.scores = new HashMap<>();
        
        // Add host as first player
        this.players.add(hostUserId);
    }
    
    /**
     * Add a player to the game
     * 
     * @param userId The user ID
     * @return true if successfully added
     */
    public boolean addPlayer(int userId) {
        if (canJoin()) {
            players.add(userId);
            scores.put(userId, 0);
            return true;
        }
        return false;
    }
    
    /**
     * Check if a user can join this game
     * 
     * @return true if game can accept more players
     */
    public boolean canJoin() {
        return isActive && players.size() < gameType.getMaxPlayers();
    }
    
    /**
     * End the game
     */
    public void endGame() {
        this.isActive = false;
        this.endTime = LocalDateTime.now();
    }
    
    /**
     * Update a player's score
     * 
     * @param userId The user ID
     * @param score The new score
     */
    public void updateScore(int userId, int score) {
        if (players.contains(userId)) {
            scores.put(userId, score);
        }
    }
    
    /**
     * Get the winner of the game
     * 
     * @return The user ID of the winner, or -1 if no winner
     */
    public int getWinner() {
        return scores.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(-1);
    }
    
    // Getters and Setters
    public String getGameId() {
        return gameId;
    }
    
    public int getGroupId() {
        return groupId;
    }
    
    public GameManager.GameType getGameType() {
        return gameType;
    }
    
    public int getHostUserId() {
        return hostUserId;
    }
    
    public Set<Integer> getPlayers() {
        return new HashSet<>(players);
    }
    
    public int getPlayerCount() {
        return players.size();
    }
    
    public LocalDateTime getStartTime() {
        return startTime;
    }
    
    public LocalDateTime getEndTime() {
        return endTime;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public Map<String, Object> getGameState() {
        return new HashMap<>(gameState);
    }
    
    public void setGameState(String key, Object value) {
        gameState.put(key, value);
    }
    
    public Map<Integer, Integer> getScores() {
        return new HashMap<>(scores);
    }
    
    @Override
    public String toString() {
        return String.format("GameSession{id='%s', type=%s, group=%d, players=%d, active=%s}",
            gameId, gameType, groupId, players.size(), isActive);
    }
}

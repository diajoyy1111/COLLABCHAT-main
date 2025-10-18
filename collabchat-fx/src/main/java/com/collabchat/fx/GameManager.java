package com.collabchat.fx;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * GameManager handles educational games for groups
 * Supports multiple game types and group-specific game sessions
 */
public class GameManager {
    
    private Map<Integer, List<GameSession>> groupGames;
    private Map<String, GameSession> activeGames;
    
    public GameManager() {
        this.groupGames = new ConcurrentHashMap<>();
        this.activeGames = new ConcurrentHashMap<>();
        
        System.out.println("🎮 GameManager initialized");
    }
    
    /**
     * Start a new game session for a group
     * 
     * @param groupId The group ID
     * @param gameType The type of game
     * @param hostUserId The user hosting the game
     * @return The created GameSession
     */
    public GameSession startGame(int groupId, GameType gameType, int hostUserId) {
        String gameId = generateGameId();
        GameSession gameSession = new GameSession(gameId, groupId, gameType, hostUserId);
        
        groupGames.computeIfAbsent(groupId, k -> new ArrayList<>()).add(gameSession);
        activeGames.put(gameId, gameSession);
        
        System.out.println("🎮 Started " + gameType + " game for group " + groupId + " (Game ID: " + gameId + ")");
        return gameSession;
    }
    
    /**
     * Join an existing game
     * 
     * @param gameId The game ID
     * @param userId The user joining
     * @return true if successfully joined
     */
    public boolean joinGame(String gameId, int userId) {
        GameSession game = activeGames.get(gameId);
        if (game != null && game.canJoin()) {
            game.addPlayer(userId);
            System.out.println("👤 User " + userId + " joined game " + gameId);
            return true;
        }
        return false;
    }
    
    /**
     * Get active games for a group
     * 
     * @param groupId The group ID
     * @return List of active games
     */
    public List<GameSession> getActiveGames(int groupId) {
        return groupGames.getOrDefault(groupId, new ArrayList<>())
            .stream()
            .filter(GameSession::isActive)
            .toList();
    }
    
    /**
     * Get a game session by ID
     * 
     * @param gameId The game ID
     * @return The GameSession or null if not found
     */
    public GameSession getGame(String gameId) {
        return activeGames.get(gameId);
    }
    
    /**
     * End a game session
     * 
     * @param gameId The game ID
     * @return true if successfully ended
     */
    public boolean endGame(String gameId) {
        GameSession game = activeGames.remove(gameId);
        if (game != null) {
            game.endGame();
            System.out.println("🏁 Ended game " + gameId);
            return true;
        }
        return false;
    }
    
    private String generateGameId() {
        return "GAME_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000);
    }
    
    /**
     * Get game statistics
     * 
     * @return String containing statistics
     */
    public String getStatistics() {
        int totalGames = groupGames.values().stream()
            .mapToInt(List::size)
            .sum();
        
        int activeGameCount = activeGames.size();
        
        return String.format("Total Games: %d | Active Games: %d | Groups with Games: %d",
            totalGames, activeGameCount, groupGames.size());
    }
    
    /**
     * Enum for supported game types
     */
    public enum GameType {
        TIC_TAC_TOE("Tic-Tac-Toe", 2, 2),
        MEMORY_MATCH("Memory Match", 2, 4),
        MATH_QUIZ("Math Quiz", 1, 10),
        WORD_BUILDER("Word Builder", 1, 6);
        
        private final String displayName;
        private final int minPlayers;
        private final int maxPlayers;
        
        GameType(String displayName, int minPlayers, int maxPlayers) {
            this.displayName = displayName;
            this.minPlayers = minPlayers;
            this.maxPlayers = maxPlayers;
        }
        
        public String getDisplayName() {
            return displayName;
        }
        
        public int getMinPlayers() {
            return minPlayers;
        }
        
        public int getMaxPlayers() {
            return maxPlayers;
        }
    }
}

package com.collabchat.fx;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ChatManager handles real-time messaging for groups
 * Each group has its own chat channel
 */
public class ChatManager {
    
    private Map<Integer, List<ChatMessage>> groupChats;
    private Map<Integer, Set<Integer>> onlineUsers;
    
    public ChatManager() {
        this.groupChats = new ConcurrentHashMap<>();
        this.onlineUsers = new ConcurrentHashMap<>();
        
        System.out.println("💬 ChatManager initialized");
    }
    
    /**
     * Send a message to a group chat
     * 
     * @param groupId The group ID
     * @param userId The user ID
     * @param username The username
     * @param message The message content
     * @return The created ChatMessage
     */
    public ChatMessage sendMessage(int groupId, int userId, String username, String message) {
        ChatMessage chatMessage = new ChatMessage(
            generateMessageId(),
            groupId,
            userId,
            username,
            message,
            LocalDateTime.now()
        );
        
        groupChats.computeIfAbsent(groupId, k -> new ArrayList<>()).add(chatMessage);
        
        System.out.println("💬 Message sent to group " + groupId + " by " + username + ": " + message);
        return chatMessage;
    }
    
    /**
     * Get messages for a group
     * 
     * @param groupId The group ID
     * @return List of messages for the group
     */
    public List<ChatMessage> getGroupMessages(int groupId) {
        return groupChats.getOrDefault(groupId, new ArrayList<>());
    }
    
    /**
     * Get recent messages for a group
     * 
     * @param groupId The group ID
     * @param limit Maximum number of messages to return
     * @return List of recent messages
     */
    public List<ChatMessage> getRecentMessages(int groupId, int limit) {
        List<ChatMessage> messages = groupChats.getOrDefault(groupId, new ArrayList<>());
        int startIndex = Math.max(0, messages.size() - limit);
        return new ArrayList<>(messages.subList(startIndex, messages.size()));
    }
    
    /**
     * Mark user as online in a group
     * 
     * @param groupId The group ID
     * @param userId The user ID
     */
    public void setUserOnline(int groupId, int userId) {
        onlineUsers.computeIfAbsent(groupId, k -> new HashSet<>()).add(userId);
        System.out.println("🟢 User " + userId + " is online in group " + groupId);
    }
    
    /**
     * Mark user as offline in a group
     * 
     * @param groupId The group ID
     * @param userId The user ID
     */
    public void setUserOffline(int groupId, int userId) {
        Set<Integer> users = onlineUsers.get(groupId);
        if (users != null) {
            users.remove(userId);
            System.out.println("🔴 User " + userId + " is offline in group " + groupId);
        }
    }
    
    /**
     * Get online users for a group
     * 
     * @param groupId The group ID
     * @return Set of online user IDs
     */
    public Set<Integer> getOnlineUsers(int groupId) {
        return onlineUsers.getOrDefault(groupId, new HashSet<>());
    }
    
    /**
     * Get online user count for a group
     * 
     * @param groupId The group ID
     * @return Number of online users
     */
    public int getOnlineUserCount(int groupId) {
        return onlineUsers.getOrDefault(groupId, new HashSet<>()).size();
    }
    
    private int generateMessageId() {
        return (int) (System.currentTimeMillis() % Integer.MAX_VALUE);
    }
    
    /**
     * Get chat statistics
     * 
     * @return String containing statistics
     */
    public String getStatistics() {
        int totalMessages = groupChats.values().stream()
            .mapToInt(List::size)
            .sum();
        
        int totalOnlineUsers = onlineUsers.values().stream()
            .mapToInt(Set::size)
            .sum();
        
        return String.format("Group Chats: %d | Total Messages: %d | Online Users: %d",
            groupChats.size(), totalMessages, totalOnlineUsers);
    }
}

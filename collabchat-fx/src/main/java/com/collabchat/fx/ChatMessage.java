package com.collabchat.fx;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * ChatMessage represents a single message in a group chat
 */
public class ChatMessage {
    
    private int messageId;
    private int groupId;
    private int userId;
    private String username;
    private String content;
    private LocalDateTime timestamp;
    private boolean isEdited;
    private LocalDateTime editedAt;
    
    public ChatMessage(int messageId, int groupId, int userId, String username, 
                      String content, LocalDateTime timestamp) {
        this.messageId = messageId;
        this.groupId = groupId;
        this.userId = userId;
        this.username = username;
        this.content = content;
        this.timestamp = timestamp;
        this.isEdited = false;
    }
    
    // Getters and Setters
    public int getMessageId() {
        return messageId;
    }
    
    public int getGroupId() {
        return groupId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
        this.isEdited = true;
        this.editedAt = LocalDateTime.now();
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public boolean isEdited() {
        return isEdited;
    }
    
    public LocalDateTime getEditedAt() {
        return editedAt;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ChatMessage that = (ChatMessage) obj;
        return messageId == that.messageId;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(messageId);
    }
    
    @Override
    public String toString() {
        return String.format("ChatMessage{id=%d, group=%d, user='%s', content='%s', time=%s}",
            messageId, groupId, username, content, timestamp);
    }
}

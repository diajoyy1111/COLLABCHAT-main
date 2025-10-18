package com.collabchat.fx;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Group model representing a classroom group
 * Each group has its own chat channel and whiteboard
 */
public class Group {
    
    private int groupId;
    private String name;
    private int creatorId;
    private Set<Integer> members;
    private LocalDateTime createdAt;
    private String description;
    private boolean isActive;
    
    public Group(int groupId, String name, int creatorId) {
        this.groupId = groupId;
        this.name = name;
        this.creatorId = creatorId;
        this.members = new HashSet<>();
        this.createdAt = LocalDateTime.now();
        this.description = "";
        this.isActive = true;
        
        // Add creator as first member
        this.members.add(creatorId);
    }
    
    // Getters and Setters
    public int getGroupId() {
        return groupId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getCreatorId() {
        return creatorId;
    }
    
    public Set<Integer> getMembers() {
        return new HashSet<>(members);
    }
    
    public int getMemberCount() {
        return members.size();
    }
    
    public boolean isMember(int userId) {
        return members.contains(userId);
    }
    
    public void addMember(int userId) {
        members.add(userId);
    }
    
    public boolean removeMember(int userId) {
        // Don't allow removing the creator
        if (userId == creatorId) {
            return false;
        }
        return members.remove(userId);
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        this.isActive = active;
    }
    
    @Override
    public String toString() {
        return String.format("Group{id=%d, name='%s', members=%d, created=%s}",
            groupId, name, members.size(), createdAt);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Group group = (Group) obj;
        return groupId == group.groupId;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(groupId);
    }
}

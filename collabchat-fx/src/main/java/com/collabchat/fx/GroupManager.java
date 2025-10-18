package com.collabchat.fx;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * GroupManager handles the creation and management of groups
 * Each group gets its own dedicated whiteboard instance
 * 
 * Key Features:
 * - Group creation and management
 * - Automatic whiteboard instance creation per group
 * - Group member management
 * - Group-specific chat channels
 */
public class GroupManager {
    
    private Map<Integer, Group> groups;
    private Map<Integer, WhiteboardInstance> groupWhiteboards;
    private int nextGroupId;
    
    public GroupManager() {
        this.groups = new ConcurrentHashMap<>();
        this.groupWhiteboards = new ConcurrentHashMap<>();
        this.nextGroupId = 1;
        
        // Initialize with sample groups
        initializeSampleGroups();
    }
    
    /**
     * Create a new group with automatic whiteboard instance
     * 
     * @param groupName The name of the group
     * @param creatorId The ID of the user creating the group
     * @return The created Group object
     */
    public Group createGroup(String groupName, int creatorId) {
        int groupId = nextGroupId++;
        
        // Create the group
        Group group = new Group(groupId, groupName, creatorId);
        groups.put(groupId, group);
        
        // Create a dedicated whiteboard instance for this group
        WhiteboardInstance whiteboard = new WhiteboardInstance(groupId, groupName + " Whiteboard");
        groupWhiteboards.put(groupId, whiteboard);
        
        System.out.println("✅ Created group: " + groupName + " (ID: " + groupId + ")");
        System.out.println("🎨 Created dedicated whiteboard for group: " + groupName);
        
        return group;
    }
    
    /**
     * Join an existing group
     * 
     * @param groupId The ID of the group to join
     * @param userId The ID of the user joining
     * @return true if successfully joined, false otherwise
     */
    public boolean joinGroup(int groupId, int userId) {
        Group group = groups.get(groupId);
        if (group != null) {
            group.addMember(userId);
            System.out.println("👤 User " + userId + " joined group: " + group.getName());
            return true;
        }
        return false;
    }
    
    /**
     * Get a group by its ID
     * 
     * @param groupId The group ID
     * @return The Group object or null if not found
     */
    public Group getGroup(int groupId) {
        return groups.get(groupId);
    }
    
    /**
     * Get the whiteboard instance for a specific group
     * 
     * @param groupId The group ID
     * @return The WhiteboardInstance for the group
     */
    public WhiteboardInstance getGroupWhiteboard(int groupId) {
        return groupWhiteboards.get(groupId);
    }
    
    /**
     * Get all groups
     * 
     * @return Collection of all groups
     */
    public Collection<Group> getAllGroups() {
        return groups.values();
    }
    
    /**
     * Get groups that a user is a member of
     * 
     * @param userId The user ID
     * @return List of groups the user is a member of
     */
    public List<Group> getUserGroups(int userId) {
        List<Group> userGroups = new ArrayList<>();
        for (Group group : groups.values()) {
            if (group.isMember(userId)) {
                userGroups.add(group);
            }
        }
        return userGroups;
    }
    
    /**
     * Delete a group and its associated whiteboard
     * 
     * @param groupId The group ID to delete
     * @return true if successfully deleted
     */
    public boolean deleteGroup(int groupId) {
        Group group = groups.remove(groupId);
        WhiteboardInstance whiteboard = groupWhiteboards.remove(groupId);
        
        if (group != null) {
            System.out.println("🗑️ Deleted group: " + group.getName() + " (ID: " + groupId + ")");
            if (whiteboard != null) {
                System.out.println("🎨 Removed associated whiteboard for group: " + group.getName());
            }
            return true;
        }
        return false;
    }
    
    /**
     * Initialize with sample groups for demonstration
     */
    private void initializeSampleGroups() {
        // Create sample groups with their whiteboards
        createGroup("Math Study Group", 1);
        createGroup("Science Lab Team", 2);
        createGroup("English Literature", 3);
        createGroup("Art & Design", 4);
        
        System.out.println("📚 Initialized " + groups.size() + " sample groups with dedicated whiteboards");
    }
    
    /**
     * Get statistics about groups and whiteboards
     * 
     * @return String containing statistics
     */
    public String getStatistics() {
        return String.format("Groups: %d | Whiteboards: %d | Total Members: %d",
            groups.size(),
            groupWhiteboards.size(),
            groups.values().stream().mapToInt(Group::getMemberCount).sum()
        );
    }
}

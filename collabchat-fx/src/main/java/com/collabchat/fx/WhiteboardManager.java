package com.collabchat.fx;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WhiteboardManager handles all whiteboard operations
 * Manages group-specific whiteboard instances and their states
 * 
 * Key Features:
 * - Group-specific whiteboard management
 * - Drawing state synchronization
 * - Undo/redo functionality
 * - Real-time collaboration support
 */
public class WhiteboardManager {
    
    private Map<Integer, WhiteboardInstance> whiteboards;
    private Map<Integer, List<DrawingAction>> undoStacks;
    private Map<Integer, List<DrawingAction>> redoStacks;
    
    public WhiteboardManager() {
        this.whiteboards = new ConcurrentHashMap<>();
        this.undoStacks = new ConcurrentHashMap<>();
        this.redoStacks = new ConcurrentHashMap<>();
        
        System.out.println("🎨 WhiteboardManager initialized");
    }
    
    /**
     * Create a new whiteboard instance for a group
     * 
     * @param groupId The group ID
     * @param whiteboardName The name of the whiteboard
     * @return The created WhiteboardInstance
     */
    public WhiteboardInstance createWhiteboard(int groupId, String whiteboardName) {
        WhiteboardInstance whiteboard = new WhiteboardInstance(groupId, whiteboardName);
        whiteboards.put(groupId, whiteboard);
        
        // Initialize undo/redo stacks for this whiteboard
        undoStacks.put(groupId, new ArrayList<>());
        redoStacks.put(groupId, new ArrayList<>());
        
        System.out.println("🎨 Created whiteboard: " + whiteboardName + " for group " + groupId);
        return whiteboard;
    }
    
    /**
     * Get a whiteboard instance for a specific group
     * 
     * @param groupId The group ID
     * @return The WhiteboardInstance or null if not found
     */
    public WhiteboardInstance getWhiteboard(int groupId) {
        return whiteboards.get(groupId);
    }
    
    /**
     * Add a drawing action to a group's whiteboard
     * 
     * @param groupId The group ID
     * @param action The drawing action
     */
    public void addDrawingAction(int groupId, DrawingAction action) {
        WhiteboardInstance whiteboard = whiteboards.get(groupId);
        if (whiteboard != null) {
            whiteboard.addDrawingAction(action);
            
            // Add to undo stack and clear redo stack
            List<DrawingAction> undoStack = undoStacks.get(groupId);
            List<DrawingAction> redoStack = redoStacks.get(groupId);
            
            if (undoStack != null) {
                undoStack.add(action);
                // Limit undo stack size
                if (undoStack.size() > 100) {
                    undoStack.remove(0);
                }
            }
            
            if (redoStack != null) {
                redoStack.clear();
            }
            
            System.out.println("✏️ Added drawing action to group " + groupId + " whiteboard");
        }
    }
    
    /**
     * Undo the last drawing action for a group
     * 
     * @param groupId The group ID
     * @return The undone action or null if nothing to undo
     */
    public DrawingAction undo(int groupId) {
        List<DrawingAction> undoStack = undoStacks.get(groupId);
        List<DrawingAction> redoStack = redoStacks.get(groupId);
        
        if (undoStack != null && !undoStack.isEmpty()) {
            DrawingAction action = undoStack.remove(undoStack.size() - 1);
            
            if (redoStack != null) {
                redoStack.add(action);
            }
            
            System.out.println("↶ Undid action for group " + groupId + " whiteboard");
            return action;
        }
        
        return null;
    }
    
    /**
     * Redo the last undone action for a group
     * 
     * @param groupId The group ID
     * @return The redone action or null if nothing to redo
     */
    public DrawingAction redo(int groupId) {
        List<DrawingAction> undoStack = undoStacks.get(groupId);
        List<DrawingAction> redoStack = redoStacks.get(groupId);
        
        if (redoStack != null && !redoStack.isEmpty()) {
            DrawingAction action = redoStack.remove(redoStack.size() - 1);
            
            if (undoStack != null) {
                undoStack.add(action);
            }
            
            System.out.println("↷ Redid action for group " + groupId + " whiteboard");
            return action;
        }
        
        return null;
    }
    
    /**
     * Clear all drawings from a group's whiteboard
     * 
     * @param groupId The group ID
     */
    public void clearWhiteboard(int groupId) {
        WhiteboardInstance whiteboard = whiteboards.get(groupId);
        if (whiteboard != null) {
            whiteboard.clear();
            
            // Clear undo/redo stacks
            List<DrawingAction> undoStack = undoStacks.get(groupId);
            List<DrawingAction> redoStack = redoStacks.get(groupId);
            
            if (undoStack != null) {
                undoStack.clear();
            }
            if (redoStack != null) {
                redoStack.clear();
            }
            
            System.out.println("🗑️ Cleared whiteboard for group " + groupId);
        }
    }
    
    /**
     * Get all drawing actions for a group's whiteboard
     * 
     * @param groupId The group ID
     * @return List of drawing actions
     */
    public List<DrawingAction> getDrawingActions(int groupId) {
        WhiteboardInstance whiteboard = whiteboards.get(groupId);
        if (whiteboard != null) {
            return whiteboard.getDrawingActions();
        }
        return new ArrayList<>();
    }
    
    /**
     * Get statistics about whiteboards
     * 
     * @return String containing statistics
     */
    public String getStatistics() {
        int totalActions = whiteboards.values().stream()
            .mapToInt(wb -> wb.getDrawingActions().size())
            .sum();
        
        return String.format("Whiteboards: %d | Total Drawing Actions: %d",
            whiteboards.size(), totalActions);
    }
    
    /**
     * Remove a whiteboard for a group
     * 
     * @param groupId The group ID
     * @return true if successfully removed
     */
    public boolean removeWhiteboard(int groupId) {
        WhiteboardInstance removed = whiteboards.remove(groupId);
        undoStacks.remove(groupId);
        redoStacks.remove(groupId);
        
        if (removed != null) {
            System.out.println("🗑️ Removed whiteboard for group " + groupId);
            return true;
        }
        return false;
    }
}

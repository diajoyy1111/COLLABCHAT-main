package com.collabchat.fx;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * WhiteboardInstance represents a single whiteboard for a specific group
 * Each group gets its own dedicated whiteboard instance
 * 
 * Features:
 * - Group-specific drawing canvas
 * - Drawing action history
 * - Real-time collaboration support
 * - Drawing tool management
 */
public class WhiteboardInstance {
    
    private int groupId;
    private String name;
    private List<DrawingAction> drawingActions;
    private LocalDateTime createdAt;
    private LocalDateTime lastModified;
    private boolean isActive;
    private int width;
    private int height;
    
    // Drawing settings
    private String currentColor;
    private int currentBrushSize;
    private String currentTool;
    
    public WhiteboardInstance(int groupId, String name) {
        this.groupId = groupId;
        this.name = name;
        this.drawingActions = new CopyOnWriteArrayList<>();
        this.createdAt = LocalDateTime.now();
        this.lastModified = LocalDateTime.now();
        this.isActive = true;
        this.width = 800;
        this.height = 600;
        
        // Default drawing settings
        this.currentColor = "#000000";
        this.currentBrushSize = 3;
        this.currentTool = "pen";
        
        System.out.println("🎨 Created WhiteboardInstance: " + name + " for group " + groupId);
    }
    
    /**
     * Add a drawing action to this whiteboard
     * 
     * @param action The drawing action to add
     */
    public void addDrawingAction(DrawingAction action) {
        drawingActions.add(action);
        lastModified = LocalDateTime.now();
        
        System.out.println("✏️ Added drawing action to " + name + " (Group " + groupId + ")");
    }
    
    /**
     * Get all drawing actions for this whiteboard
     * 
     * @return List of drawing actions
     */
    public List<DrawingAction> getDrawingActions() {
        return new ArrayList<>(drawingActions);
    }
    
    /**
     * Clear all drawings from this whiteboard
     */
    public void clear() {
        drawingActions.clear();
        lastModified = LocalDateTime.now();
        
        System.out.println("🗑️ Cleared whiteboard: " + name + " (Group " + groupId + ")");
    }
    
    /**
     * Get the number of drawing actions
     * 
     * @return Number of drawing actions
     */
    public int getActionCount() {
        return drawingActions.size();
    }
    
    /**
     * Set drawing color
     * 
     * @param color The color in hex format (e.g., "#FF0000")
     */
    public void setCurrentColor(String color) {
        this.currentColor = color;
    }
    
    /**
     * Set brush size
     * 
     * @param size The brush size in pixels
     */
    public void setCurrentBrushSize(int size) {
        this.currentBrushSize = size;
    }
    
    /**
     * Set current drawing tool
     * 
     * @param tool The tool name (pen, eraser, line, rectangle, circle)
     */
    public void setCurrentTool(String tool) {
        this.currentTool = tool;
    }
    
    /**
     * Get whiteboard dimensions
     * 
     * @return Array with [width, height]
     */
    public int[] getDimensions() {
        return new int[]{width, height};
    }
    
    /**
     * Set whiteboard dimensions
     * 
     * @param width The width
     * @param height The height
     */
    public void setDimensions(int width, int height) {
        this.width = width;
        this.height = height;
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
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public LocalDateTime getLastModified() {
        return lastModified;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        this.isActive = active;
    }
    
    public String getCurrentColor() {
        return currentColor;
    }
    
    public int getCurrentBrushSize() {
        return currentBrushSize;
    }
    
    public String getCurrentTool() {
        return currentTool;
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    @Override
    public String toString() {
        return String.format("WhiteboardInstance{groupId=%d, name='%s', actions=%d, modified=%s}",
            groupId, name, drawingActions.size(), lastModified);
    }
}

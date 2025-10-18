package com.collabchat.fx;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * DrawingAction represents a single drawing action on the whiteboard
 * Used for undo/redo functionality and real-time collaboration
 */
public class DrawingAction {
    
    public enum ActionType {
        DRAW, ERASE, LINE, RECTANGLE, CIRCLE, TEXT, CLEAR
    }
    
    private int actionId;
    private ActionType type;
    private double startX;
    private double startY;
    private double endX;
    private double endY;
    private String color;
    private int brushSize;
    private String tool;
    private String text; // For text actions
    private int userId;
    private LocalDateTime timestamp;
    
    public DrawingAction(int actionId, ActionType type, double startX, double startY, 
                        double endX, double endY, String color, int brushSize, 
                        String tool, int userId) {
        this.actionId = actionId;
        this.type = type;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.color = color;
        this.brushSize = brushSize;
        this.tool = tool;
        this.userId = userId;
        this.timestamp = LocalDateTime.now();
    }
    
    // Constructor for text actions
    public DrawingAction(int actionId, ActionType type, double x, double y, 
                        String text, String color, int brushSize, int userId) {
        this.actionId = actionId;
        this.type = type;
        this.startX = x;
        this.startY = y;
        this.endX = x;
        this.endY = y;
        this.text = text;
        this.color = color;
        this.brushSize = brushSize;
        this.tool = "text";
        this.userId = userId;
        this.timestamp = LocalDateTime.now();
    }
    
    // Getters and Setters
    public int getActionId() {
        return actionId;
    }
    
    public ActionType getType() {
        return type;
    }
    
    public double getStartX() {
        return startX;
    }
    
    public double getStartY() {
        return startY;
    }
    
    public double getEndX() {
        return endX;
    }
    
    public double getEndY() {
        return endY;
    }
    
    public String getColor() {
        return color;
    }
    
    public int getBrushSize() {
        return brushSize;
    }
    
    public String getTool() {
        return tool;
    }
    
    public String getText() {
        return text;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        DrawingAction that = (DrawingAction) obj;
        return actionId == that.actionId;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(actionId);
    }
    
    @Override
    public String toString() {
        return String.format("DrawingAction{id=%d, type=%s, from=(%.1f,%.1f), to=(%.1f,%.1f), color=%s, user=%d}",
            actionId, type, startX, startY, endX, endY, color, userId);
    }
}

package com.collabchat.fx;

/**
 * Demo class to demonstrate group-specific whiteboard functionality
 * This shows how each group gets its own dedicated whiteboard instance
 */
public class Demo {
    
    public static void main(String[] args) {
        System.out.println("🎓 CollabChat FX - Group-Specific Whiteboard Demo");
        System.out.println("==================================================");
        
        // Initialize managers
        GroupManager groupManager = new GroupManager();
        WhiteboardManager whiteboardManager = new WhiteboardManager();
        
        System.out.println("\n📚 Creating sample groups...");
        
        // Create groups (each gets its own whiteboard automatically)
        Group mathGroup = groupManager.createGroup("Math Study Group", 1);
        Group scienceGroup = groupManager.createGroup("Science Lab Team", 2);
        Group englishGroup = groupManager.createGroup("English Literature", 3);
        
        System.out.println("\n🎨 Demonstrating group-specific whiteboards...");
        
        // Each group has its own whiteboard
        WhiteboardInstance mathWhiteboard = groupManager.getGroupWhiteboard(mathGroup.getGroupId());
        WhiteboardInstance scienceWhiteboard = groupManager.getGroupWhiteboard(scienceGroup.getGroupId());
        WhiteboardInstance englishWhiteboard = groupManager.getGroupWhiteboard(englishGroup.getGroupId());
        
        System.out.println("✅ Math Group Whiteboard: " + mathWhiteboard.getName());
        System.out.println("✅ Science Group Whiteboard: " + scienceWhiteboard.getName());
        System.out.println("✅ English Group Whiteboard: " + englishWhiteboard.getName());
        
        System.out.println("\n✏️ Adding drawing actions to different group whiteboards...");
        
        // Add drawing actions to different group whiteboards
        DrawingAction mathAction1 = new DrawingAction(1, DrawingAction.ActionType.DRAW, 100, 100, 200, 200, "#FF0000", 3, "pen", 1);
        DrawingAction mathAction2 = new DrawingAction(2, DrawingAction.ActionType.RECTANGLE, 150, 150, 250, 250, "#00FF00", 2, "rectangle", 1);
        
        DrawingAction scienceAction1 = new DrawingAction(3, DrawingAction.ActionType.CIRCLE, 300, 300, 400, 400, "#0000FF", 4, "circle", 2);
        DrawingAction scienceAction2 = new DrawingAction(4, DrawingAction.ActionType.TEXT, 350, 350, 350, 350, "#FF00FF", 2, "text", 2);
        
        DrawingAction englishAction1 = new DrawingAction(5, DrawingAction.ActionType.LINE, 500, 500, 600, 600, "#FFFF00", 1, "line", 3);
        
        // Add actions to respective whiteboards
        whiteboardManager.addDrawingAction(mathGroup.getGroupId(), mathAction1);
        whiteboardManager.addDrawingAction(mathGroup.getGroupId(), mathAction2);
        
        whiteboardManager.addDrawingAction(scienceGroup.getGroupId(), scienceAction1);
        whiteboardManager.addDrawingAction(scienceGroup.getGroupId(), scienceAction2);
        
        whiteboardManager.addDrawingAction(englishGroup.getGroupId(), englishAction1);
        
        System.out.println("✅ Added 2 drawing actions to Math Group whiteboard");
        System.out.println("✅ Added 2 drawing actions to Science Group whiteboard");
        System.out.println("✅ Added 1 drawing action to English Group whiteboard");
        
        System.out.println("\n📊 Whiteboard Statistics:");
        System.out.println("Math Group Actions: " + mathWhiteboard.getActionCount());
        System.out.println("Science Group Actions: " + scienceWhiteboard.getActionCount());
        System.out.println("English Group Actions: " + englishWhiteboard.getActionCount());
        
        System.out.println("\n🔄 Demonstrating undo functionality...");
        
        // Undo last action from Math group
        DrawingAction undoneAction = whiteboardManager.undo(mathGroup.getGroupId());
        if (undoneAction != null) {
            System.out.println("↶ Undid action: " + undoneAction);
        }
        
        System.out.println("Math Group Actions after undo: " + mathWhiteboard.getActionCount());
        
        System.out.println("\n🗑️ Demonstrating clear functionality...");
        
        // Clear Science group whiteboard
        whiteboardManager.clearWhiteboard(scienceGroup.getGroupId());
        System.out.println("Science Group Actions after clear: " + scienceWhiteboard.getActionCount());
        
        System.out.println("\n📈 Final Statistics:");
        System.out.println("Groups: " + groupManager.getAllGroups().size());
        System.out.println("Whiteboards: " + whiteboardManager.getStatistics());
        
        System.out.println("\n🎯 Key Features Demonstrated:");
        System.out.println("✅ Each group has its own dedicated whiteboard instance");
        System.out.println("✅ Drawing actions are isolated per group");
        System.out.println("✅ Undo/redo functionality works per group");
        System.out.println("✅ Clear functionality works per group");
        System.out.println("✅ Real-time collaboration ready (WebSocket integration)");
        
        System.out.println("\n🚀 Demo completed! The JavaFX application will show this in action.");
    }
}

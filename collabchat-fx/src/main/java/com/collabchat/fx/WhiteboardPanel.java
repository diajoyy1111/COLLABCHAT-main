package com.collabchat.fx;

import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class WhiteboardPanel extends BorderPane {
    private WhiteboardManager whiteboardManager;
    private Canvas canvas;

    public WhiteboardPanel(WhiteboardManager whiteboardManager) {
        this.whiteboardManager = whiteboardManager;
        setPadding(new Insets(20));
        setStyle("-fx-background-radius: 20; -fx-background-color: rgba(255,255,255,0.7);");

        canvas = new Canvas(700, 500);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Toolbar for drawing tools (placeholder)
        ToolBar toolbar = new ToolBar(
            new Button("Pen"),
            new Button("Eraser"),
            new Button("Clear")
        );

        // NOTE: In a real app, you would listen for group selection changes and update the whiteboard accordingly.
        // For now, this is a placeholder. You can add a method to update the canvas when the group changes.

        setTop(toolbar);
        setCenter(canvas);
    }
}

package com.collabchat.fx;

import javafx.scene.layout.*;
import javafx.scene.control.*;

public class GamesPanel extends VBox {
    public GamesPanel() {
        setSpacing(20);
        setStyle("-fx-background-radius: 20; -fx-background-color: rgba(255,255,255,0.7); -fx-padding: 40;");
        getChildren().addAll(
            new Label("Games (Coming Soon)"),
            new Button("Math Quiz"),
            new Button("Word Puzzle")
        );
    }
}

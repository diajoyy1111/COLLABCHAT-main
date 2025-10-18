package com.collabchat.fx;

import javafx.scene.layout.*;
import javafx.scene.control.*;

public class QuizPanel extends VBox {
    public QuizPanel() {
        setSpacing(20);
        setStyle("-fx-background-radius: 20; -fx-background-color: rgba(255,255,255,0.7); -fx-padding: 40;");
        getChildren().addAll(
            new Label("Quizzes (Coming Soon)"),
            new Button("Start Quiz"),
            new Button("View Results")
        );
    }
}

package com.collabchat.fx;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class GroupChatPanel extends BorderPane {
    private GroupManager groupManager;
    private WhiteboardManager whiteboardManager;
    private ListView<Group> groupList;
    private TextArea chatArea;
    private TextField inputField;
    private int currentUserId = 1; // Placeholder for demo

    public GroupChatPanel(GroupManager groupManager, WhiteboardManager whiteboardManager) {
        this.groupManager = groupManager;
        this.whiteboardManager = whiteboardManager;

        setPadding(new Insets(20));
        setStyle("-fx-background-radius: 20; -fx-background-color: rgba(255,255,255,0.7);");

        groupList = new ListView<>();
        groupList.getItems().addAll(groupManager.getAllGroups());
        groupList.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Group group, boolean empty) {
                super.updateItem(group, empty);
                setText((empty || group == null) ? null : group.getName());
            }
        });
        groupList.setPrefWidth(180);
        groupList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                chatArea.setText("Chat for group: " + newVal.getName());
                // Optionally, load chat history here
            }
        });

        chatArea = new TextArea();
        chatArea.setEditable(false);
        chatArea.setStyle("-fx-background-radius: 15; -fx-background-color: #f7fafd;");

        inputField = new TextField();
        inputField.setPromptText("Type a message...");
        inputField.setOnAction(e -> {
            Group selected = groupList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                chatArea.appendText("\nYou: " + inputField.getText());
                // Placeholder: send message to server for selected.getGroupId()
            }
            inputField.clear();
        });

        Button newGroupBtn = new Button("New Group");
        newGroupBtn.setOnAction(e -> {
            String groupName = "Group " + (groupList.getItems().size() + 1);
            Group newGroup = groupManager.createGroup(groupName, currentUserId);
            groupList.getItems().add(newGroup);
            // Whiteboard is auto-created in GroupManager
        });

        VBox leftPanel = new VBox(10, new Label("Groups"), groupList, newGroupBtn);
        leftPanel.setPadding(new Insets(0, 20, 0, 0));
        leftPanel.setStyle("-fx-background-color: transparent;");

        VBox chatPanel = new VBox(10, chatArea, inputField);
        chatPanel.setVgrow(chatArea, Priority.ALWAYS);

        setLeft(leftPanel);
        setCenter(chatPanel);
    }
}

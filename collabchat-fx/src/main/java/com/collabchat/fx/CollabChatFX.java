package com.collabchat.fx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Main JavaFX Application for CollabChat Classroom Collaboration Platform
 * 
 * Features:
 * - Group-specific whiteboards (each group gets its own drawing canvas)
 * - Real-time chat per group
 * - Educational games and quizzes
 * - Modern, futuristic UI design
 * - Modular architecture for easy expansion
 */
public class CollabChatFX extends Application {
    
    private Stage primaryStage;
    private BorderPane mainLayout;
    private TabPane mainTabPane;
    private GroupManager groupManager;
    private WhiteboardManager whiteboardManager;
    private ChatManager chatManager;
    private GameManager gameManager;
    private QuizManager quizManager;
    
    // Modern color palette - light pastels with futuristic accents
    private static final Color PRIMARY_COLOR = Color.rgb(102, 126, 234); // Soft blue
    private static final Color SECONDARY_COLOR = Color.rgb(118, 75, 162); // Soft purple
    private static final Color ACCENT_COLOR = Color.rgb(52, 152, 219); // Bright blue
    private static final Color BACKGROUND_COLOR = Color.rgb(248, 249, 250); // Light gray
    private static final Color CARD_COLOR = Color.rgb(255, 255, 255); // White
    private static final Color TEXT_PRIMARY = Color.rgb(44, 62, 80); // Dark blue-gray
    private static final Color TEXT_SECONDARY = Color.rgb(108, 117, 125); // Medium gray
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        initializeApplication();
        setupMainLayout();
        setupTabs();
        applyModernStyling();
        
        primaryStage.setTitle("CollabChat - Classroom Collaboration Platform");
        primaryStage.setMinWidth(1200);
        primaryStage.setMinHeight(800);
        primaryStage.setScene(new Scene(mainLayout, 1400, 900));
        primaryStage.show();
        
        // Initialize managers
        initializeManagers();
    }
    
    /**
     * Initialize the application with modern styling and fonts
     */
    private void initializeApplication() {
        // Set modern font
        Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-Regular.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-Bold.ttf"), 12);
    }
    
    /**
     * Setup the main application layout with modern design
     */
    private void setupMainLayout() {
        mainLayout = new BorderPane();
        mainLayout.setBackground(createGradientBackground());
        
        // Create header
        VBox header = createHeader();
        mainLayout.setTop(header);
        
        // Create main content area
        mainTabPane = new TabPane();
        mainTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        mainLayout.setCenter(mainTabPane);
        
        // Create sidebar for group management
        VBox sidebar = createSidebar();
        mainLayout.setLeft(sidebar);
    }
    
    /**
     * Create the modern header with app branding
     */
    private VBox createHeader() {
        VBox header = new VBox(10);
        header.setPadding(new Insets(20, 30, 20, 30));
        header.setBackground(createCardBackground());
        
        HBox titleBar = new HBox();
        titleBar.setAlignment(Pos.CENTER_LEFT);
        titleBar.setSpacing(15);
        
        // App logo/icon
        Label logo = new Label("🎓");
        logo.setFont(Font.font("System", FontWeight.BOLD, 32));
        
        // App title
        Label title = new Label("CollabChat");
        title.setFont(Font.font("System", FontWeight.BOLD, 28));
        title.setTextFill(PRIMARY_COLOR);
        
        // Subtitle
        Label subtitle = new Label("Classroom Collaboration Platform");
        subtitle.setFont(Font.font("System", 14));
        subtitle.setTextFill(TEXT_SECONDARY);
        
        VBox titleSection = new VBox(5);
        titleSection.getChildren().addAll(title, subtitle);
        
        titleBar.getChildren().addAll(logo, titleSection);
        
        // User info section
        HBox userInfo = new HBox(15);
        userInfo.setAlignment(Pos.CENTER_RIGHT);
        
        Label userLabel = new Label("Welcome, Student");
        userLabel.setFont(Font.font("System", FontWeight.MEDIUM, 14));
        userLabel.setTextFill(TEXT_PRIMARY);
        
        Button logoutBtn = createModernButton("Logout", ACCENT_COLOR);
        logoutBtn.setPrefWidth(80);
        
        userInfo.getChildren().addAll(userLabel, logoutBtn);
        
        header.getChildren().addAll(titleBar, userInfo);
        return header;
    }
    
    /**
     * Create the sidebar for group management
     */
    private VBox createSidebar() {
        VBox sidebar = new VBox(20);
        sidebar.setPrefWidth(280);
        sidebar.setPadding(new Insets(20));
        sidebar.setBackground(createCardBackground());
        
        // Group management section
        Label groupTitle = new Label("Groups");
        groupTitle.setFont(Font.font("System", FontWeight.BOLD, 18));
        groupTitle.setTextFill(PRIMARY_COLOR);
        
        // Create group button
        Button createGroupBtn = createModernButton("+ Create New Group", PRIMARY_COLOR);
        createGroupBtn.setPrefWidth(240);
        createGroupBtn.setPrefHeight(45);
        
        // Join group section
        VBox joinSection = new VBox(10);
        Label joinLabel = new Label("Join Group");
        joinLabel.setFont(Font.font("System", FontWeight.MEDIUM, 14));
        joinLabel.setTextFill(TEXT_PRIMARY);
        
        TextField groupIdField = new TextField();
        groupIdField.setPromptText("Enter Group ID");
        groupIdField.setPrefHeight(35);
        styleTextField(groupIdField);
        
        Button joinBtn = createModernButton("Join", SECONDARY_COLOR);
        joinBtn.setPrefWidth(240);
        joinBtn.setPrefHeight(35);
        
        joinSection.getChildren().addAll(joinLabel, groupIdField, joinBtn);
        
        // Current groups list
        VBox groupsList = new VBox(10);
        Label currentGroupsLabel = new Label("Current Groups");
        currentGroupsLabel.setFont(Font.font("System", FontWeight.MEDIUM, 14));
        currentGroupsLabel.setTextFill(TEXT_PRIMARY);
        
        ListView<String> groupsListView = new ListView<>();
        groupsListView.setPrefHeight(200);
        groupsListView.setBackground(createCardBackground());
        
        // Add sample groups
        groupsListView.getItems().addAll(
            "📚 Math Study Group",
            "🔬 Science Lab Team",
            "📝 English Literature",
            "🎨 Art & Design"
        );
        
        groupsList.getChildren().addAll(currentGroupsLabel, groupsListView);
        
        sidebar.getChildren().addAll(groupTitle, createGroupBtn, joinSection, groupsList);
        return sidebar;
    }
    
    /**
     * Setup the main tabs for different features
     */
    private void setupTabs() {
        // Chat Tab
        Tab chatTab = new Tab("💬 Chat");
        chatTab.setContent(createChatTab());
        
        // Whiteboard Tab
        Tab whiteboardTab = new Tab("🎨 Whiteboard");
        whiteboardTab.setContent(createWhiteboardTab());
        
        // Games Tab
        Tab gamesTab = new Tab("🎮 Games");
        gamesTab.setContent(createGamesTab());
        
        // Quiz Tab
        Tab quizTab = new Tab("📝 Quiz");
        quizTab.setContent(createQuizTab());
        
        mainTabPane.getTabs().addAll(chatTab, whiteboardTab, gamesTab, quizTab);
    }
    
    /**
     * Create the chat tab with modern design
     */
    private VBox createChatTab() {
        VBox chatTab = new VBox(20);
        chatTab.setPadding(new Insets(20));
        chatTab.setBackground(createCardBackground());
        
        // Chat header
        HBox chatHeader = new HBox(15);
        chatHeader.setAlignment(Pos.CENTER_LEFT);
        
        Label chatTitle = new Label("Group Chat");
        chatTitle.setFont(Font.font("System", FontWeight.BOLD, 20));
        chatTitle.setTextFill(PRIMARY_COLOR);
        
        Label groupInfo = new Label("Math Study Group • 5 members online");
        groupInfo.setFont(Font.font("System", 12));
        groupInfo.setTextFill(TEXT_SECONDARY);
        
        chatHeader.getChildren().addAll(chatTitle, groupInfo);
        
        // Chat messages area
        VBox messagesArea = new VBox(10);
        messagesArea.setPrefHeight(500);
        messagesArea.setBackground(createCardBackground());
        messagesArea.setPadding(new Insets(15));
        messagesArea.setStyle("-fx-border-color: #e9ecef; -fx-border-width: 1; -fx-border-radius: 10;");
        
        // Add sample messages
        addSampleMessages(messagesArea);
        
        // Message input area
        HBox inputArea = new HBox(10);
        inputArea.setAlignment(Pos.CENTER_LEFT);
        
        TextField messageInput = new TextField();
        messageInput.setPromptText("Type your message...");
        messageInput.setPrefHeight(40);
        messageInput.setPrefWidth(800);
        styleTextField(messageInput);
        
        Button sendBtn = createModernButton("Send", ACCENT_COLOR);
        sendBtn.setPrefWidth(80);
        sendBtn.setPrefHeight(40);
        
        inputArea.getChildren().addAll(messageInput, sendBtn);
        
        chatTab.getChildren().addAll(chatHeader, messagesArea, inputArea);
        return chatTab;
    }
    
    /**
     * Create the whiteboard tab with group-specific whiteboards
     */
    private VBox createWhiteboardTab() {
        VBox whiteboardTab = new VBox(20);
        whiteboardTab.setPadding(new Insets(20));
        whiteboardTab.setBackground(createCardBackground());
        
        // Whiteboard header
        HBox whiteboardHeader = new HBox(15);
        whiteboardHeader.setAlignment(Pos.CENTER_LEFT);
        
        Label whiteboardTitle = new Label("Collaborative Whiteboard");
        whiteboardTitle.setFont(Font.font("System", FontWeight.BOLD, 20));
        whiteboardTitle.setTextFill(PRIMARY_COLOR);
        
        Label groupWhiteboardInfo = new Label("Math Study Group Whiteboard");
        groupWhiteboardInfo.setFont(Font.font("System", 12));
        groupWhiteboardInfo.setTextFill(TEXT_SECONDARY);
        
        whiteboardHeader.getChildren().addAll(whiteboardTitle, groupWhiteboardInfo);
        
        // Drawing toolbar
        HBox toolbar = createDrawingToolbar();
        
        // Whiteboard canvas area
        VBox canvasArea = new VBox();
        canvasArea.setPrefHeight(500);
        canvasArea.setBackground(createCardBackground());
        canvasArea.setStyle("-fx-border-color: #e9ecef; -fx-border-width: 1; -fx-border-radius: 10;");
        canvasArea.setPadding(new Insets(10));
        
        // Create whiteboard canvas (placeholder for actual canvas implementation)
        Label canvasPlaceholder = new Label("🎨 Whiteboard Canvas\n\nThis is where the collaborative drawing will happen.\nEach group has its own dedicated whiteboard instance.");
        canvasPlaceholder.setFont(Font.font("System", 16));
        canvasPlaceholder.setTextFill(TEXT_SECONDARY);
        canvasPlaceholder.setAlignment(Pos.CENTER);
        canvasPlaceholder.setPrefHeight(480);
        
        canvasArea.getChildren().add(canvasPlaceholder);
        
        whiteboardTab.getChildren().addAll(whiteboardHeader, toolbar, canvasArea);
        return whiteboardTab;
    }
    
    /**
     * Create the drawing toolbar for whiteboard
     */
    private HBox createDrawingToolbar() {
        HBox toolbar = new HBox(15);
        toolbar.setAlignment(Pos.CENTER_LEFT);
        toolbar.setPadding(new Insets(10));
        toolbar.setBackground(createCardBackground());
        toolbar.setStyle("-fx-border-color: #e9ecef; -fx-border-width: 1; -fx-border-radius: 10;");
        
        // Color selection
        VBox colorSection = new VBox(5);
        Label colorLabel = new Label("Colors");
        colorLabel.setFont(Font.font("System", FontWeight.MEDIUM, 12));
        colorLabel.setTextFill(TEXT_PRIMARY);
        
        HBox colorButtons = new HBox(8);
        Color[] colors = {Color.BLACK, Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE, Color.PURPLE};
        for (Color color : colors) {
            Button colorBtn = new Button();
            colorBtn.setPrefSize(30, 30);
            colorBtn.setStyle("-fx-background-color: " + toHexString(color) + "; -fx-background-radius: 15; -fx-border-color: #ddd; -fx-border-radius: 15;");
        }
        colorSection.getChildren().addAll(colorLabel, colorButtons);
        
        // Brush size
        VBox brushSection = new VBox(5);
        Label brushLabel = new Label("Brush Size");
        brushLabel.setFont(Font.font("System", FontWeight.MEDIUM, 12));
        brushLabel.setTextFill(TEXT_PRIMARY);
        
        ComboBox<String> brushSize = new ComboBox<>();
        brushSize.getItems().addAll("Thin", "Medium", "Thick", "Extra Thick");
        brushSize.setValue("Medium");
        brushSize.setPrefWidth(100);
        
        brushSection.getChildren().addAll(brushLabel, brushSize);
        
        // Tools
        VBox toolsSection = new VBox(5);
        Label toolsLabel = new Label("Tools");
        toolsLabel.setFont(Font.font("System", FontWeight.MEDIUM, 12));
        toolsLabel.setTextFill(TEXT_PRIMARY);
        
        HBox toolButtons = new HBox(8);
        Button undoBtn = createModernButton("↶ Undo", TEXT_SECONDARY);
        Button clearBtn = createModernButton("🗑️ Clear", Color.rgb(231, 76, 60));
        undoBtn.setPrefWidth(80);
        clearBtn.setPrefWidth(80);
        
        toolButtons.getChildren().addAll(undoBtn, clearBtn);
        toolsSection.getChildren().addAll(toolsLabel, toolButtons);
        
        toolbar.getChildren().addAll(colorSection, brushSection, toolsSection);
        return toolbar;
    }
    
    /**
     * Create the games tab
     */
    private VBox createGamesTab() {
        VBox gamesTab = new VBox(20);
        gamesTab.setPadding(new Insets(20));
        gamesTab.setBackground(createCardBackground());
        
        Label gamesTitle = new Label("Educational Games");
        gamesTitle.setFont(Font.font("System", FontWeight.BOLD, 20));
        gamesTitle.setTextFill(PRIMARY_COLOR);
        
        // Game selection grid
        GridPane gameGrid = new GridPane();
        gameGrid.setHgap(20);
        gameGrid.setVgap(20);
        gameGrid.setAlignment(Pos.CENTER);
        
        // Tic-Tac-Toe game card
        VBox ticTacToeCard = createGameCard("🎯 Tic-Tac-Toe", "Classic 3x3 grid game for strategy practice");
        GridPane.setConstraints(ticTacToeCard, 0, 0);
        
        // Memory game card
        VBox memoryCard = createGameCard("🧠 Memory Match", "Find matching pairs to improve memory");
        GridPane.setConstraints(memoryCard, 1, 0);
        
        // Math quiz card
        VBox mathCard = createGameCard("🔢 Math Quiz", "Quick arithmetic challenges");
        GridPane.setConstraints(mathCard, 0, 1);
        
        // Word game card
        VBox wordCard = createGameCard("📝 Word Builder", "Create words from given letters");
        GridPane.setConstraints(wordCard, 1, 1);
        
        gameGrid.getChildren().addAll(ticTacToeCard, memoryCard, mathCard, wordCard);
        
        gamesTab.getChildren().addAll(gamesTitle, gameGrid);
        return gamesTab;
    }
    
    /**
     * Create the quiz tab
     */
    private VBox createQuizTab() {
        VBox quizTab = new VBox(20);
        quizTab.setPadding(new Insets(20));
        quizTab.setBackground(createCardBackground());
        
        Label quizTitle = new Label("Live Quiz System");
        quizTitle.setFont(Font.font("System", FontWeight.BOLD, 20));
        quizTitle.setTextFill(PRIMARY_COLOR);
        
        // Teacher controls (if user is teacher)
        VBox teacherControls = new VBox(15);
        teacherControls.setPadding(new Insets(15));
        teacherControls.setBackground(createCardBackground());
        teacherControls.setStyle("-fx-border-color: #e9ecef; -fx-border-width: 1; -fx-border-radius: 10;");
        
        Label teacherLabel = new Label("Teacher Controls");
        teacherLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        teacherLabel.setTextFill(SECONDARY_COLOR);
        
        TextField questionField = new TextField();
        questionField.setPromptText("Enter your question...");
        questionField.setPrefHeight(35);
        styleTextField(questionField);
        
        HBox optionsRow1 = new HBox(10);
        TextField option1 = new TextField();
        option1.setPromptText("Option 1");
        option1.setPrefHeight(35);
        styleTextField(option1);
        
        TextField option2 = new TextField();
        option2.setPromptText("Option 2");
        option2.setPrefHeight(35);
        styleTextField(option2);
        
        optionsRow1.getChildren().addAll(option1, option2);
        
        HBox optionsRow2 = new HBox(10);
        TextField option3 = new TextField();
        option3.setPromptText("Option 3");
        option3.setPrefHeight(35);
        styleTextField(option3);
        
        TextField option4 = new TextField();
        option4.setPromptText("Option 4");
        option4.setPrefHeight(35);
        styleTextField(option4);
        
        optionsRow2.getChildren().addAll(option3, option4);
        
        Button sendQuizBtn = createModernButton("📤 Send Quiz to Students", ACCENT_COLOR);
        sendQuizBtn.setPrefWidth(250);
        sendQuizBtn.setPrefHeight(40);
        
        teacherControls.getChildren().addAll(teacherLabel, questionField, optionsRow1, optionsRow2, sendQuizBtn);
        
        // Student quiz area
        VBox studentQuiz = new VBox(15);
        studentQuiz.setPadding(new Insets(15));
        studentQuiz.setBackground(createCardBackground());
        studentQuiz.setStyle("-fx-border-color: #e9ecef; -fx-border-width: 1; -fx-border-radius: 10;");
        
        Label studentLabel = new Label("Student Quiz Area");
        studentLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        studentLabel.setTextFill(PRIMARY_COLOR);
        
        Label waitingLabel = new Label("Waiting for quiz from teacher...");
        waitingLabel.setFont(Font.font("System", 14));
        waitingLabel.setTextFill(TEXT_SECONDARY);
        waitingLabel.setAlignment(Pos.CENTER);
        waitingLabel.setPrefHeight(200);
        
        studentQuiz.getChildren().addAll(studentLabel, waitingLabel);
        
        quizTab.getChildren().addAll(quizTitle, teacherControls, studentQuiz);
        return quizTab;
    }
    
    /**
     * Create a game card with modern styling
     */
    private VBox createGameCard(String title, String description) {
        VBox card = new VBox(15);
        card.setPadding(new Insets(20));
        card.setPrefWidth(200);
        card.setPrefHeight(150);
        card.setBackground(createCardBackground());
        card.setStyle("-fx-border-color: #e9ecef; -fx-border-width: 1; -fx-border-radius: 15;");
        card.setAlignment(Pos.CENTER);
        
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        titleLabel.setTextFill(PRIMARY_COLOR);
        titleLabel.setAlignment(Pos.CENTER);
        
        Label descLabel = new Label(description);
        descLabel.setFont(Font.font("System", 12));
        descLabel.setTextFill(TEXT_SECONDARY);
        descLabel.setAlignment(Pos.CENTER);
        descLabel.setWrapText(true);
        
        Button playBtn = createModernButton("Play", ACCENT_COLOR);
        playBtn.setPrefWidth(120);
        playBtn.setPrefHeight(35);
        
        card.getChildren().addAll(titleLabel, descLabel, playBtn);
        
        // Add hover effect
        card.setOnMouseEntered(e -> {
            card.setStyle("-fx-border-color: " + toHexString(PRIMARY_COLOR) + "; -fx-border-width: 2; -fx-border-radius: 15; -fx-background-color: #f8f9fa;");
        });
        
        card.setOnMouseExited(e -> {
            card.setStyle("-fx-border-color: #e9ecef; -fx-border-width: 1; -fx-border-radius: 15; -fx-background-color: white;");
        });
        
        return card;
    }
    
    /**
     * Add sample messages to chat area
     */
    private void addSampleMessages(VBox messagesArea) {
        String[] sampleMessages = {
            "👋 Welcome to the Math Study Group!",
            "📚 Let's work on calculus problems together",
            "❓ Does anyone understand derivatives?",
            "💡 I can help explain the chain rule",
            "📝 Great! Let's practice some examples"
        };
        
        String[] users = {"Teacher", "Student1", "Student2", "Student3", "Student1"};
        
        for (int i = 0; i < sampleMessages.length; i++) {
            HBox messageBox = new HBox(10);
            messageBox.setAlignment(Pos.CENTER_LEFT);
            messageBox.setPadding(new Insets(8));
            messageBox.setBackground(createCardBackground());
            messageBox.setStyle("-fx-border-color: #e9ecef; -fx-border-width: 1; -fx-border-radius: 10;");
            
            Label userLabel = new Label(users[i] + ":");
            userLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
            userLabel.setTextFill(PRIMARY_COLOR);
            userLabel.setPrefWidth(80);
            
            Label messageLabel = new Label(sampleMessages[i]);
            messageLabel.setFont(Font.font("System", 12));
            messageLabel.setTextFill(TEXT_PRIMARY);
            messageLabel.setWrapText(true);
            
            messageBox.getChildren().addAll(userLabel, messageLabel);
            messagesArea.getChildren().add(messageBox);
        }
    }
    
    /**
     * Initialize all managers for different features
     */
    private void initializeManagers() {
        groupManager = new GroupManager();
        whiteboardManager = new WhiteboardManager();
        chatManager = new ChatManager();
        groupManager = new GroupManager();
        whiteboardManager = new WhiteboardManager();

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab groupChatTab = new Tab("Group Chat", new GroupChatPanel(groupManager, whiteboardManager));
        Tab whiteboardTab = new Tab("Whiteboard", new WhiteboardPanel(whiteboardManager));
        Tab gamesTab = new Tab("Games", new GamesPanel());
        Tab quizTab = new Tab("Quizzes", new QuizPanel());

        tabPane.getTabs().addAll(groupChatTab, whiteboardTab, gamesTab, quizTab);

        StackPane root = new StackPane(tabPane);
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #e0eafc, #cfdef3);");

        Scene scene = new Scene(root, 1000, 700);
        scene.getStylesheets().add(getClass().getResource("/futuristic.css").toExternalForm());
        Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-Regular.ttf"), 12);

        primaryStage.setTitle("CollabChat - Classroom Collaboration");
        primaryStage.setScene(scene);
        primaryStage.show();
        button.setBackground(new Background(new BackgroundFill(color, new CornerRadii(8), new Insets(0))));
        button.setPrefHeight(35);
        button.setCursor(javafx.scene.Cursor.HAND);
        
        // Hover effect
        button.setOnMouseEntered(e -> {
            button.setBackground(new Background(new BackgroundFill(color.deriveColor(0, 1, 0.9, 1), new CornerRadii(8), new Insets(0))));
        });
        
        button.setOnMouseExited(e -> {
            button.setBackground(new Background(new BackgroundFill(color, new CornerRadii(8), new Insets(0))));
        });
        
        return button;
    }
    
    private void styleTextField(TextField textField) {
        textField.setFont(Font.font("System", 12));
        textField.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(8), new Insets(0))));
        textField.setStyle("-fx-border-color: #e9ecef; -fx-border-width: 1; -fx-border-radius: 8; -fx-padding: 8;");
    }
    
    private String toHexString(Color color) {
        return String.format("#%02X%02X%02X",
            (int) (color.getRed() * 255),
            (int) (color.getGreen() * 255),
            (int) (color.getBlue() * 255));
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}

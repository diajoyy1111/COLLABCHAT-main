# CollabChat FX - JavaFX Classroom Collaboration Platform

A modern, futuristic JavaFX application for classroom collaboration with group-specific whiteboards, real-time chat, educational games, and live quizzes.

## 🎯 Features

### ✅ **Group-Specific Whiteboards**
- Each group gets its own dedicated whiteboard instance
- Automatic whiteboard creation when groups are created
- Real-time collaborative drawing
- Multiple drawing tools (pen, eraser, shapes, text)
- Undo/redo functionality
- Color and brush size selection

### ✅ **Real-Time Chat**
- Group-specific chat channels
- Online user indicators
- Message history
- Typing indicators (placeholder for future implementation)

### ✅ **Educational Games**
- Tic-Tac-Toe
- Memory Match
- Math Quiz
- Word Builder
- Group-based game sessions

### ✅ **Live Quiz System**
- Teachers can create and send quizzes
- Students respond in real-time
- Automatic grading and results
- Time-limited quizzes
- Answer distribution analytics

### ✅ **Modern UI/UX**
- Futuristic and minimalistic design
- Light pastel colors with subtle gradients
- Rounded panels and modern fonts
- Responsive layout
- Smooth animations and transitions

## 🏗️ Architecture

### **Modular Design**
```
CollabChatFX (Main Application)
├── GroupManager (Group creation and management)
├── WhiteboardManager (Whiteboard operations)
├── ChatManager (Real-time messaging)
├── GameManager (Educational games)
└── QuizManager (Live quiz system)
```

### **Group-Specific Whiteboards**
- **GroupManager**: Creates groups and automatically generates whiteboard instances
- **WhiteboardManager**: Manages all whiteboard operations and drawing actions
- **WhiteboardInstance**: Represents a single whiteboard for a specific group
- **DrawingAction**: Represents individual drawing operations for undo/redo

### **Key Classes**

#### **Core Application**
- `CollabChatFX.java` - Main JavaFX application with modern UI
- `GroupManager.java` - Handles group creation and whiteboard instantiation
- `WhiteboardManager.java` - Manages group-specific whiteboards

#### **Models**
- `Group.java` - Group model with member management
- `WhiteboardInstance.java` - Individual whiteboard for each group
- `DrawingAction.java` - Drawing operations for collaboration
- `ChatMessage.java` - Chat message model
- `Quiz.java` - Quiz question model
- `GameSession.java` - Game session model

#### **Managers**
- `ChatManager.java` - Real-time messaging
- `GameManager.java` - Educational games
- `QuizManager.java` - Live quiz system

## 🚀 Getting Started

### **Prerequisites**
- Java 17 or higher
- Maven 3.6 or higher
- JavaFX 17 (included in dependencies)

### **Installation**

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd collabchat-fx
   ```

2. **Build the project**
   ```bash
   mvn clean compile
   ```

3. **Run the application**
   ```bash
   mvn javafx:run
   ```

### **Alternative: Run with JAR**
```bash
mvn clean package
java -jar target/collabchat-fx-1.0.0.jar
```

## 🎨 UI Design

### **Color Palette**
- **Primary**: Soft Blue (#667eea)
- **Secondary**: Soft Purple (#764ba2)
- **Accent**: Bright Blue (#3498db)
- **Background**: Light Gray (#f8f9fa)
- **Cards**: White (#ffffff)
- **Text Primary**: Dark Blue-Gray (#2c3e50)
- **Text Secondary**: Medium Gray (#6c757d)

### **Design Principles**
- **Futuristic**: Modern gradients and rounded corners
- **Minimalistic**: Clean layouts with plenty of white space
- **Responsive**: Adapts to different screen sizes
- **Accessible**: High contrast and readable fonts

## 🔧 Group-Specific Whiteboard Implementation

### **How It Works**

1. **Group Creation**:
   ```java
   // When a group is created, a whiteboard is automatically generated
   Group group = groupManager.createGroup("Math Study Group", creatorId);
   // This automatically creates a WhiteboardInstance for the group
   ```

2. **Whiteboard Access**:
   ```java
   // Each group has its own whiteboard
   WhiteboardInstance whiteboard = whiteboardManager.getGroupWhiteboard(groupId);
   ```

3. **Drawing Actions**:
   ```java
   // All drawing actions are stored per group
   DrawingAction action = new DrawingAction(actionId, type, x1, y1, x2, y2, color, brushSize, tool, userId);
   whiteboardManager.addDrawingAction(groupId, action);
   ```

### **Key Features**
- **Isolation**: Each group's drawings are completely separate
- **Collaboration**: Multiple users can draw on the same group whiteboard
- **Persistence**: Drawing actions are stored and can be replayed
- **Undo/Redo**: Full undo/redo functionality per group
- **Real-time**: Ready for WebSocket integration

## 🌐 Server Communication (Placeholder)

The application includes placeholder logic for server communication:

```java
// Placeholder for WebSocket connection
// Future implementation will connect to server for real-time collaboration
// - Group synchronization
// - Real-time drawing updates
// - Chat message broadcasting
// - Quiz distribution
// - Game state synchronization
```

## 📱 Responsive Design

The application is designed to work on:
- **Desktop**: Full-featured experience
- **Tablet**: Touch-optimized interface
- **Large Screens**: Multi-panel layouts

## 🎮 Educational Games

### **Supported Games**
1. **Tic-Tac-Toe**: 2-player strategy game
2. **Memory Match**: 2-4 player memory game
3. **Math Quiz**: 1-10 player arithmetic challenges
4. **Word Builder**: 1-6 player word creation game

### **Game Features**
- Group-based game sessions
- Real-time multiplayer support
- Score tracking
- Game state management

## 📝 Quiz System

### **Teacher Features**
- Create multiple-choice questions
- Set time limits
- Send quizzes to groups
- View real-time results

### **Student Features**
- Receive quiz notifications
- Answer questions in real-time
- See immediate feedback
- View results after completion

### **Analytics**
- Response time tracking
- Answer distribution
- Correctness percentage
- Individual and group statistics

## 🔮 Future Enhancements

### **Planned Features**
- **WebSocket Integration**: Real-time collaboration
- **File Sharing**: Document and image sharing
- **Voice Chat**: Audio communication
- **Screen Sharing**: Presentation mode
- **Mobile App**: Companion mobile application
- **Cloud Storage**: Persistent data storage
- **Advanced Drawing**: More drawing tools and shapes
- **Game Extensions**: More educational games

### **Technical Improvements**
- **Performance Optimization**: Better rendering and memory management
- **Security**: End-to-end encryption
- **Scalability**: Support for larger groups
- **Offline Mode**: Work without internet connection
- **Accessibility**: Screen reader support

## 🛠️ Development

### **Project Structure**
```
collabchat-fx/
├── src/main/java/com/collabchat/fx/
│   ├── CollabChatFX.java          # Main application
│   ├── GroupManager.java          # Group management
│   ├── WhiteboardManager.java     # Whiteboard operations
│   ├── ChatManager.java           # Chat functionality
│   ├── GameManager.java           # Game management
│   ├── QuizManager.java           # Quiz system
│   └── models/                    # Data models
├── pom.xml                        # Maven configuration
└── README.md                      # This file
```

### **Building and Testing**
```bash
# Compile
mvn compile

# Run tests
mvn test

# Package
mvn package

# Run application
mvn javafx:run
```

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## 📞 Support

For support and questions:
- Create an issue in the repository
- Contact the development team
- Check the documentation

---

**CollabChat FX** - Bringing classrooms together through modern technology! 🎓✨

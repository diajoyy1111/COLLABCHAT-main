CollabChat Swing + WebSocket (example)

Overview
- Minimal example of a classroom collaboration desktop app using Java Swing for the UI and a simple WebSocket server for real-time messaging.
- Contains a basic WebSocket server and a Swing client with modular panels: Chat, Whiteboard, TicTacToe, Quiz.

Requirements
- Java 11+ (Java 17 recommended)
- java-websocket library (org.java-websocket:Java-WebSocket). Download jar from: https://github.com/TooTallNate/Java-WebSocket/releases

How to run
1) Start the server:
   javac -cp java-websocket-1.5.3.jar server/*.java
   java -cp .;java-websocket-1.5.3.jar server.CollabServer

2) Start clients (multiple):
   javac -cp java-websocket-1.5.3.jar client/*.java
   java -cp .;java-websocket-1.5.3.jar client.CollabClient

Notes
- This is a demo scaffold: it focuses on the GUI, message formats and network wiring. It is not production-ready (no authentication persistence, limited security).
- To extend: add persistence, authentication, better message validation, and richer whiteboard sync protocols.

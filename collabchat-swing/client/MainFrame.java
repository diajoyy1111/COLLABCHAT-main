package client;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.util.function.Consumer;

// Main application frame with CardLayout for modular pages
public class MainFrame extends JFrame {
    private CardLayout cardLayout = new CardLayout();
    private JPanel cards = new JPanel(cardLayout);

    private ChatPanel chatPanel;
    private JPanel homePanel;
    private WhiteboardPanel whiteboardPanel;
    private TicTacToePanel gamePanel;
    private QuizPanel quizPanel;

    private WSClient wsClient;
    private String username;
    private String role;

    public MainFrame(String username, String role) {
        super("CollabChat - " + username + " (" + role + ")");
        this.username = username; this.role = role;

        chatPanel = new ChatPanel(this::sendMessage);
        homePanel = createHomeSplash();
        whiteboardPanel = new WhiteboardPanel(this::sendMessage);
        gamePanel = new TicTacToePanel(this::sendMessage);
        quizPanel = new QuizPanel(this::sendMessage, role);

        cards.add(wrapWithPadding(homePanel), "home");
        cards.add(wrapWithPadding(chatPanel), "chat");
        cards.add(wrapWithPadding(whiteboardPanel), "whiteboard");
        cards.add(wrapWithPadding(gamePanel), "game");
        cards.add(wrapWithPadding(quizPanel), "quiz");

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(createToolbar(), BorderLayout.NORTH);
        getContentPane().add(cards, BorderLayout.CENTER);
    }

    private JPanel createToolbar() {
        JPanel p = new JPanel();
        p.setBackground(new Color(236,240,241));
        p.setBorder(BorderFactory.createMatteBorder(0,0,1,0,new Color(210,214,218)));
        JButton bHome = new JButton("Home"); bHome.addActionListener(e -> animateTo("home")); styleButton(bHome);
        JButton bChat = new JButton("Chat"); bChat.addActionListener(e -> animateTo("chat")); styleButton(bChat);
        JButton bBoard = new JButton("Whiteboard"); bBoard.addActionListener(e -> animateTo("whiteboard")); styleButton(bBoard);
        JButton bGame = new JButton("Games"); bGame.addActionListener(e -> animateTo("game")); styleButton(bGame);
        JButton bQuiz = new JButton("Quiz"); bQuiz.addActionListener(e -> animateTo("quiz")); styleButton(bQuiz);
        p.add(bHome); p.add(bChat); p.add(bBoard); p.add(bGame); p.add(bQuiz);
        return p;
    }

    // Create WS connection and wire incoming messages to panels
    public void connectToServer(String uri) {
        try {
            wsClient = new WSClient(new URI(uri), msg -> handleServerMessage(msg));
            wsClient.connect();
        } catch (Exception e) { e.printStackTrace(); }
    }

    // Send message via WS
    public void sendMessage(String msg) {
        if (wsClient != null && wsClient.isOpen()) {
            wsClient.send(msg);
        } else {
            System.out.println("WS not connected: " + msg);
        }
    }

    // Dispatch incoming JSON messages to panels (simple type dispatch)
    private void handleServerMessage(String msg) {
        // In a real app, use a JSON library. For brevity, do simple contains checks.
        SwingUtilities.invokeLater(() -> {
            if (msg.contains("\"type\":\"chat\"")) {
                chatPanel.onMessage(msg);
            } else if (msg.contains("\"type\":\"draw\"")) {
                whiteboardPanel.onDrawMessage(msg);
            } else if (msg.contains("\"type\":\"quiz\"")) {
                quizPanel.onQuizMessage(msg);
            } else if (msg.contains("\"type\":\"game\"")) {
                gamePanel.onGameMessage(msg);
            }
        });
    }

    private JPanel wrapWithPadding(JComponent inner) {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        outer.add(inner, BorderLayout.CENTER);
        return outer;
    }

    // Simple animated card switch (fade/slide feel)
    private void animateTo(String name) {
        cardLayout.show(cards, name);
    }

    private JPanel createHomeSplash() {
        JPanel p = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Welcome to CollabChat", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 26f));
        title.setForeground(new Color(41,128,185));
        JTextArea body = new JTextArea("Use the tabs above to chat, draw on the whiteboard, play games, and take quizzes.\nOpen this app on two machines to see real-time collaboration.");
        body.setEditable(false); body.setWrapStyleWord(true); body.setLineWrap(true);
        body.setBackground(p.getBackground());
        p.add(title, BorderLayout.NORTH);
        p.add(body, BorderLayout.CENTER);
        JButton go = new JButton("Go to Chat"); go.addActionListener(e -> animateTo("chat"));
        JPanel bottom = new JPanel(); bottom.add(go); p.add(bottom, BorderLayout.SOUTH);
        return p;
    }

    private void styleButton(JButton b) {
        b.setFocusPainted(false);
        b.setFont(b.getFont().deriveFont(Font.BOLD));
        b.setMargin(new Insets(6,12,6,12));
    }

    // Small WebSocket client wrapper
    private static class WSClient extends WebSocketClient {
        private Consumer<String> onMessage;
        public WSClient(URI serverUri, Consumer<String> onMessage) { super(serverUri); this.onMessage = onMessage; }
        @Override public void onOpen(ServerHandshake handshakedata) { System.out.println("WS connected"); }
        @Override public void onMessage(String message) { onMessage.accept(message); }
        @Override public void onClose(int code, String reason, boolean remote) { System.out.println("WS closed: "+reason); }
        @Override public void onError(Exception ex) { ex.printStackTrace(); }
    }
}

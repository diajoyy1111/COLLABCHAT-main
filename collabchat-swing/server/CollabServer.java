package server;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

// Simple WebSocket server that broadcasts messages to all connected clients.
// Message format: JSON with a top-level `type` field: chat, draw, quiz, game
public class CollabServer extends WebSocketServer {
    private final Set<WebSocket> conns = Collections.synchronizedSet(new HashSet<>());

    public CollabServer(int port) {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        conns.add(conn);
        System.out.println("New connection from " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        conns.remove(conn);
        System.out.println("Connection closed: " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        // Basic broadcast: incoming JSON is forwarded to all clients
        System.out.println("Received: " + message);
        synchronized (conns) {
            for (WebSocket c : conns) {
                if (c.isOpen()) c.send(message);
            }
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.err.println("Server error: " + ex.getMessage());
    }

    @Override
    public void onStart() {
        System.out.println("CollabServer started on port " + getPort());
        setConnectionLostTimeout(0);
        setConnectionLostTimeout(100);
    }

    public static void main(String[] args) {
        int port = 8887;
        CollabServer server = new CollabServer(port);
        server.start();
        System.out.println("Server started on port: " + port);
    }
}

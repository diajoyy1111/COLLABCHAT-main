package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Instant;
import java.util.function.Consumer;

// Simple chat UI panel. Messages are sent as JSON string: {"type":"chat","user":"...","msg":"...","ts":123}
public class ChatPanel extends JPanel {
    private JTextArea taMessages;
    private JTextField tfInput;
    private JLabel typing;
    private Consumer<String> sender;

    public ChatPanel(Consumer<String> sender) {
        this.sender = sender;
        setLayout(new BorderLayout());
        taMessages = new JTextArea(); taMessages.setEditable(false);
        add(new JScrollPane(taMessages), BorderLayout.CENTER);
        JPanel bottom = new JPanel(new BorderLayout());
        tfInput = new JTextField();
        JButton bSend = new JButton("Send");
        bSend.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { send(); } });
        bottom.add(tfInput, BorderLayout.CENTER); bottom.add(bSend, BorderLayout.EAST);
        typing = new JLabel(" ");
        bottom.add(typing, BorderLayout.NORTH);
        add(bottom, BorderLayout.SOUTH);
    }

    private void send() {
        String text = tfInput.getText().trim(); if (text.isEmpty()) return;
        String json = String.format("{\"type\":\"chat\",\"user\":\"%s\",\"msg\":\"%s\",\"ts\":%d}", "local", escape(text), Instant.now().toEpochMilli());
        sender.accept(json);
        tfInput.setText("");
    }

    public void onMessage(String json) {
        // crude parsing
        String user = extract(json, "user");
        String msg = extract(json, "msg");
        taMessages.append("→ " + user + ": " + msg + "\n");
        typing.setText(" ");
    }

    private String extract(String json, String key) {
        int idx = json.indexOf("\""+key+"\""); if (idx<0) return "";
        int col = json.indexOf(':', idx); int start = json.indexOf('"', col+1); int end = json.indexOf('"', start+1);
        if (start<0||end<0) return ""; return json.substring(start+1,end);
    }

    private String escape(String s) { return s.replace("\\","\\\\").replace("\"","\\\"").replace("\n","\\n"); }
}

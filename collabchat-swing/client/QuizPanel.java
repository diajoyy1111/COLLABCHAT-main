package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

// Quiz panel: teacher can send a multiple-choice question; students receive and answer.
// Quiz message example (teacher): {"type":"quiz","action":"ask","id":123,"q":"Q?","opts":["A","B"]}
// Answer message (student): {"type":"quiz","action":"answer","id":123,"ans":1,"user":"u"}
public class QuizPanel extends JPanel {
    private Consumer<String> sender;
    private String role;

    private JPanel questionArea = new JPanel(new BorderLayout());
    private JProgressBar countdown = new JProgressBar(0, 100);
    private Timer timer;

    public QuizPanel(Consumer<String> sender, String role) {
        this.sender = sender; this.role = role;
        setLayout(new BorderLayout());
        add(questionArea, BorderLayout.CENTER);
        countdown.setStringPainted(true);
        add(countdown, BorderLayout.NORTH);
        if ("teacher".equals(role)) add(createTeacherControls(), BorderLayout.SOUTH);
    }

    private JPanel createTeacherControls() {
        JPanel p = new JPanel();
        JButton bAsk = new JButton("Send Sample Question");
        bAsk.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { sendSampleQuestion(); } });
        p.add(bAsk); return p;
    }

    private void sendSampleQuestion() {
        // Hard-coded sample question
        String json = "{\"type\":\"quiz\",\"action\":\"ask\",\"id\":123,\"q\":\"What is 2+2?\",\"opts\":[\"3\",\"4\",\"5\"]}";
        sender.accept(json);
    }

    // Students will receive quiz messages and display choices
    public void onQuizMessage(String json) {
        if (json.contains("\"action\":\"ask\"")) {
            SwingUtilities.invokeLater(() -> showQuestion(json));
        }
    }

    private void showQuestion(String json) {
        questionArea.removeAll();
        String q = extractStr(json, "q");
        JPanel opts = new JPanel(new GridLayout(0,1));
        // crude extract options between [ and ]
        int start = json.indexOf("[", json.indexOf("\"opts\"")); int end = json.indexOf("]", start+1);
        String inner = json.substring(start+1,end);
        String[] parts = inner.split(",");
        ButtonGroup bg = new ButtonGroup();
        for (int i=0;i<parts.length;i++) {
            String opt = parts[i].trim(); if (opt.startsWith("\"") && opt.endsWith("\"")) opt = opt.substring(1,opt.length()-1);
            JRadioButton r = new JRadioButton(opt);
            int idx=i;
            r.addActionListener(e -> {
                // send answer message
                String ans = String.format("{\"type\":\"quiz\",\"action\":\"answer\",\"id\":%s,\"ans\":%d,\"user\":\"local\"}", extract(json, "id"), idx);
                sender.accept(ans);
            });
            bg.add(r); opts.add(r);
        }
        questionArea.add(new JLabel(q), BorderLayout.NORTH);
        questionArea.add(opts, BorderLayout.CENTER);
        startCountdown(20); // 20 seconds
        revalidate(); repaint();
    }

    private String extract(String json, String key) { int idx = json.indexOf('"'+key+'"'); int col=json.indexOf(':',idx); int comma=json.indexOf(',',col+1); if (comma<0) comma=json.indexOf('}',col+1); return json.substring(col+1,comma).trim(); }
    private String extractStr(String json, String key) { int idx = json.indexOf('"'+key+'"'); int col=json.indexOf(':',idx); int s=json.indexOf('"',col+1); int e=json.indexOf('"',s+1); return json.substring(s+1,e); }

    private void startCountdown(int seconds) {
        if (timer != null) timer.stop();
        countdown.setValue(100);
        final long end = System.currentTimeMillis() + seconds*1000L;
        timer = new Timer(100, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                long remain = Math.max(0, end - System.currentTimeMillis());
                int pct = (int)(remain * 100 / (seconds*1000L));
                countdown.setValue(pct);
                countdown.setString(pct + "% time left");
                if (remain == 0) timer.stop();
            }
        });
        timer.start();
    }
}

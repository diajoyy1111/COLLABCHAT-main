package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

// Very simple local tic-tac-toe UI. For demo, moves are sent as game messages broadcast to others.
// Message example: {"type":"game","game":"tictac","x":0,"y":1,"player":"p1"}
public class TicTacToePanel extends JPanel {
    private JButton[][] btns = new JButton[3][3];
    private char[][] board = new char[3][3];
    private Consumer<String> sender;

    public TicTacToePanel(Consumer<String> sender) {
        this.sender = sender;
        setLayout(new GridLayout(3,3));
        for (int r=0;r<3;r++) for (int c=0;c<3;c++) {
            JButton b = new JButton(""); final int rr=r, cc=c;
            b.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 60));
            b.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { move(rr,cc); } });
            btns[r][c]=b; add(b);
        }
        JButton reset = new JButton("Reset"); reset.addActionListener(e -> doReset());
        add(reset);
    }

    public void move(int r,int c) {
        if (board[r][c]!=0) return;
        board[r][c] = 'X'; btns[r][c].setText("X");
        String json = String.format("{\"type\":\"game\",\"game\":\"tictac\",\"x\":%d,\"y\":%d,\"player\":\"local\"}", r, c);
        sender.accept(json);
    }

    public void onGameMessage(String json) {
        if (!json.contains("\"game\":\"tictac\"")) return;
        int x = Integer.parseInt(extract(json, "x"));
        int y = Integer.parseInt(extract(json, "y"));
        String player = extractStr(json, "player");
        board[x][y] = 'O'; // show remote moves as O
        btns[x][y].setText("O");
    }

    private void doReset() {
        for (int r=0;r<3;r++) for (int c=0;c<3;c++) { board[r][c]=0; btns[r][c].setText(""); }
    }

    private String extract(String json, String key) { int idx = json.indexOf('"'+key+'"'); int col=json.indexOf(':',idx); int comma=json.indexOf(',',col+1); if (comma<0) comma=json.indexOf('}',col+1); return json.substring(col+1,comma).trim(); }
    private String extractStr(String json, String key) { int idx = json.indexOf('"'+key+'"'); int col=json.indexOf(':',idx); int s=json.indexOf('"',col+1); int e=json.indexOf('"',s+1); return json.substring(s+1,e); }
}

package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

// Very small collaborative whiteboard: sends draw messages containing line segments.
// Message example: {"type":"draw","user":"u","x1":10,"y1":10,"x2":20,"y2":20}
public class WhiteboardPanel extends JPanel {
    private List<Stroke> strokes = new ArrayList<>();
    private int lastX, lastY;
    private Consumer<String> sender;
    private Color currentColor = Color.BLACK;
    private int brushSize = 3;
    private JToolBar toolbar;

    public WhiteboardPanel(Consumer<String> sender) {
        this.sender = sender;
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        add(buildToolbar(), BorderLayout.NORTH);
        addMouseListener(new MouseAdapter() { public void mousePressed(MouseEvent e) { lastX=e.getX(); lastY=e.getY(); } });
        addMouseMotionListener(new MouseAdapter() { public void mouseDragged(MouseEvent e) { int x=e.getX(), y=e.getY(); strokes.add(new Stroke(new Line2D.Float(lastX,lastY,x,y), currentColor, brushSize)); repaint(); String json = String.format("{\"type\":\"draw\",\"user\":\"local\",\"x1\":%d,\"y1\":%d,\"x2\":%d,\"y2\":%d,\"color\":%d,\"size\":%d}", lastX,lastY,x,y,currentColor.getRGB(),brushSize); sender.accept(json); lastX=x; lastY=y; } });
    }

    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2=(Graphics2D)g;
        // draw light grid
        g2.setColor(new Color(240,240,240));
        for (int i=0;i<getWidth();i+=20) g2.drawLine(i,0,i,getHeight());
        for (int j=0;j<getHeight();j+=20) g2.drawLine(0,j,getWidth(),j);
        // strokes
        for (Stroke s: strokes) { g2.setColor(s.color); g2.setStroke(new BasicStroke(s.size, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)); g2.draw(s.line); }
    }

    // When a draw message arrives from the server, update local whiteboard
    public void onDrawMessage(String json) {
        try {
            int x1 = Integer.parseInt(extract(json, "x1"));
            int y1 = Integer.parseInt(extract(json, "y1"));
            int x2 = Integer.parseInt(extract(json, "x2"));
            int y2 = Integer.parseInt(extract(json, "y2"));
            int rgb = Integer.parseInt(extract(json, "color"));
            int size = Integer.parseInt(extract(json, "size"));
            strokes.add(new Stroke(new Line2D.Float(x1,y1,x2,y2), new Color(rgb, true), size));
            repaint();
        } catch (Exception e) { /* ignore parse errors in demo */ }
    }

    private String extract(String json, String key) {
        int idx = json.indexOf('"'+key+'"'); if (idx<0) return "0";
        int col = json.indexOf(':', idx); int comma = json.indexOf(',', col+1); if (comma<0) comma = json.indexOf('}', col+1);
        return json.substring(col+1, comma).trim();
    }

    private static class Stroke {
        Line2D line; Color color; int size;
        Stroke(Line2D l, Color c, int s) { this.line=l; this.color=c; this.size=s; }
    }

    private JToolBar buildToolbar() {
        JToolBar tb = new JToolBar(); tb.setFloatable(false);
        JButton clear = new JButton("Clear"); clear.addActionListener(e -> { strokes.clear(); repaint(); });
        JButton undo = new JButton("Undo"); undo.addActionListener(e -> { if (!strokes.isEmpty()) { strokes.remove(strokes.size()-1); repaint(); } });
        JComboBox<String> size = new JComboBox<>(new String[]{"2","3","4","6","8","12"}); size.setSelectedItem("3");
        size.addActionListener(e -> { brushSize = Integer.parseInt((String) size.getSelectedItem()); });
        JButton black = new JButton(); black.setBackground(Color.BLACK); black.setOpaque(true); black.addActionListener(e -> currentColor=Color.BLACK);
        JButton red = new JButton(); red.setBackground(Color.RED); red.setOpaque(true); red.addActionListener(e -> currentColor=Color.RED);
        JButton blue = new JButton(); blue.setBackground(Color.BLUE); blue.setOpaque(true); blue.addActionListener(e -> currentColor=Color.BLUE);
        JButton green = new JButton(); green.setBackground(Color.GREEN.darker()); green.setOpaque(true); green.addActionListener(e -> currentColor=Color.GREEN.darker());
        tb.add(new JLabel("Brush:")); tb.add(size); tb.addSeparator();
        tb.add(new JLabel("Color:")); tb.add(black); tb.add(red); tb.add(blue); tb.add(green); tb.addSeparator();
        tb.add(undo); tb.add(clear);
        return tb;
    }
}

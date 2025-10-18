package client;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

// Main entry for the Swing client. Creates a Login dialog and then the main frame.
public class CollabClient {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Modern look and feel
            try { for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) { if ("Nimbus".equals(info.getName())) { UIManager.setLookAndFeel(info.getClassName()); break; } } } catch (Exception ignore) {}

            LoginDialog login = new LoginDialog(null);
            login.setVisible(true);
            if (!login.isSucceeded()) System.exit(0);

            // Role and username captured from login dialog
            String username = login.getUsername();
            String role = login.getRole();

            MainFrame main = new MainFrame(username, role);
            main.setIconImage(createAppIcon());
            main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            main.setSize(1000, 700);
            main.setLocationRelativeTo(null);
            main.setVisible(true);

            // Connect to server and start receiving messages
            main.connectToServer("ws://localhost:8887");
        });
    }

    private static Image createAppIcon() {
        int s=64; BufferedImage img = new BufferedImage(s,s,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(new Color(41, 128, 185)); g.fillRoundRect(0,0,s,s,18,18);
        g.setColor(Color.WHITE);
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 28));
        g.drawString("CC", 14, 40);
        g.dispose();
        return img;
    }
}

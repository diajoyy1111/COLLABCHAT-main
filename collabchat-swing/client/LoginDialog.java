package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Simple login dialog that collects username and role (teacher/student). No server auth in this demo.
public class LoginDialog extends JDialog {
    private JTextField tfUsername;
    private JComboBox<String> cbRole;
    private JPasswordField pfPassword;
    private boolean succeeded = false;

    public LoginDialog(Frame parent) {
        super(parent, "Login", true);
        // Header
        JPanel header = new JPanel(); header.setBackground(new Color(41,128,185));
        JLabel title = new JLabel("  CollabChat"); title.setForeground(Color.WHITE); title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        header.add(title);

        // Form
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints(); gc.insets = new Insets(8,8,8,8); gc.fill = GridBagConstraints.HORIZONTAL;
        gc.gridx=0; gc.gridy=0; panel.add(new JLabel("Username:"), gc);
        tfUsername = new JTextField(20);
        gc.gridx=1; panel.add(tfUsername, gc);
        gc.gridx=0; gc.gridy=1; panel.add(new JLabel("Role:"), gc);
        cbRole = new JComboBox<>(new String[]{"student","teacher"});
        gc.gridx=1; panel.add(cbRole, gc);
        gc.gridx=0; gc.gridy=2; panel.add(new JLabel("Password:"), gc);
        pfPassword = new JPasswordField(); gc.gridx=1; panel.add(pfPassword, gc);

        JButton btnLogin = new JButton("Login");
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (getUsername().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(LoginDialog.this, "Enter username");
                    return;
                }
                // cosmetic only; password not used for auth in demo
                succeeded = true;
                setVisible(false);
            }
        });

        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(e -> System.exit(0));
        JButton btnSkip = new JButton("Skip");
        btnSkip.setToolTipText("Enter as guest");
        btnSkip.addActionListener(e -> { tfUsername.setText("Guest-" + (System.currentTimeMillis()%1000)); cbRole.setSelectedItem("student"); pfPassword.setText(""); succeeded=true; setVisible(false); });

        JPanel bp = new JPanel();
        bp.add(btnSkip);
        bp.add(btnLogin);
        bp.add(btnCancel);

        getRootPane().setDefaultButton(btnLogin);
        getContentPane().add(header, BorderLayout.NORTH);
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(bp, BorderLayout.PAGE_END);
        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
    }

    public String getUsername() { return tfUsername.getText().trim(); }
    public String getRole() { return (String) cbRole.getSelectedItem(); }
    public boolean isSucceeded() { return succeeded; }
}

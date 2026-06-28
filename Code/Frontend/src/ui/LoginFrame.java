package ui;

import com.mycompany.DatabaseManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginFrame extends JFrame {

    private JTextField usernameInput;
    private JPasswordField passwordInput;
    private JButton loginBtn;
    private JLabel errorMessageLabel;

    public LoginFrame() {
        Color primaryColor = new Color(10, 25, 47);
        Color textPrimary = new Color(204, 214, 246);
        Color accentColor = new Color(100, 255, 218);
        final Color glassColor = new Color(17, 34, 64, 180);

        setTitle("Đăng Nhập - Go Game");
        setSize(420, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        getContentPane().setBackground(primaryColor);

        JPanel authBox = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(new Color(10, 25, 47));
                g.fillRect(0, 0, getWidth(), getHeight());

                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(glassColor);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();

                super.paintComponent(g);
            }
        };
        authBox.setOpaque(false);
        authBox.setLayout(new BoxLayout(authBox, BoxLayout.Y_AXIS));
        authBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 255, 218, 51), 1),
                BorderFactory.createEmptyBorder(35, 40, 35, 40)
        ));

        JLabel titleLabel = new JLabel("Đăng Nhập");
        titleLabel.setFont(new Font("Inter", Font.BOLD, 32));
        titleLabel.setForeground(textPrimary);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel usernameLabel = new JLabel("Tên đăng nhập:");
        usernameLabel.setFont(new Font("Inter", Font.PLAIN, 14));
        usernameLabel.setForeground(textPrimary);
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        usernameInput = new JTextField();
        usernameInput.setMaximumSize(new Dimension(340, 45));
        usernameInput.setBackground(new Color(10, 25, 47));
        usernameInput.setForeground(textPrimary);
        usernameInput.setCaretColor(accentColor);
        usernameInput.setFont(new Font("Inter", Font.PLAIN, 14));
        usernameInput.setBorder(BorderFactory.createLineBorder(new Color(100, 255, 218, 51), 2));

        JLabel passwordLabel = new JLabel("Mật khẩu:");
        passwordLabel.setFont(new Font("Inter", Font.PLAIN, 14));
        passwordLabel.setForeground(textPrimary);
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        passwordInput = new JPasswordField();
        passwordInput.setMaximumSize(new Dimension(340, 45));
        passwordInput.setBackground(new Color(10, 25, 47));
        passwordInput.setForeground(textPrimary);
        passwordInput.setCaretColor(accentColor);
        passwordInput.setFont(new Font("Inter", Font.PLAIN, 14));
        passwordInput.setBorder(BorderFactory.createLineBorder(new Color(100, 255, 218, 51), 2));

        loginBtn = new JButton("ĐĂNG NHẬP");
        loginBtn.setBackground(accentColor);
        loginBtn.setForeground(primaryColor);
        loginBtn.setFont(new Font("Inter", Font.BOLD, 16));
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.setMaximumSize(new Dimension(340, 50));
        loginBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginBtn.setFocusPainted(false);
        loginBtn.setBorderPainted(false);

        errorMessageLabel = new JLabel("");
        errorMessageLabel.setFont(new Font("Inter", Font.PLAIN, 14));
        errorMessageLabel.setForeground(new Color(255, 107, 107));
        errorMessageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel registerLinkLabel = new JLabel("Chưa có tài khoản? Đăng ký ngay");
        registerLinkLabel.setFont(new Font("Inter", Font.PLAIN, 14));
        registerLinkLabel.setForeground(accentColor);
        registerLinkLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        registerLinkLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        authBox.add(titleLabel);
        authBox.add(Box.createVerticalStrut(25));
        authBox.add(usernameLabel);
        authBox.add(Box.createVerticalStrut(8));
        authBox.add(usernameInput);
        authBox.add(Box.createVerticalStrut(20));
        authBox.add(passwordLabel);
        authBox.add(Box.createVerticalStrut(8));
        authBox.add(passwordInput);
        authBox.add(Box.createVerticalStrut(25));
        authBox.add(loginBtn);
        authBox.add(Box.createVerticalStrut(15));
        authBox.add(errorMessageLabel);
        authBox.add(Box.createVerticalStrut(15));
        authBox.add(registerLinkLabel);

        add(authBox);

        setupActions(registerLinkLabel);
    }

    private void setupActions(JLabel registerLinkLabel) {
        DatabaseManager dbManager = DatabaseManager.getInstance();

        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameInput.getText().trim();
                String password = new String(passwordInput.getPassword()).trim();

                if (username.isEmpty() || password.isEmpty()) {
                    errorMessageLabel.setText("Vui lòng điền đầy đủ thông tin!");
                } else {
                    if (dbManager.checkLogin(username, password)) {
                        errorMessageLabel.setText("");
                        JOptionPane.showMessageDialog(LoginFrame.this, "Đăng nhập thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                        LoginFrame.this.dispose();

                        SwingUtilities.invokeLater(() -> {
                            LobbyRoom lobby = new LobbyRoom(usernameInput.getText().trim());
                            lobby.setVisible(true);
                        });
                    } else {
                        errorMessageLabel.setText("Tên đăng nhập hoặc mật khẩu không đúng.");
                    }
                }
            }
        });

        registerLinkLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                LoginFrame.this.setVisible(false);
                SwingUtilities.invokeLater(() -> {
                    RegisterFrame regFrame = new RegisterFrame(LoginFrame.this);
                    regFrame.setVisible(true);
                });
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
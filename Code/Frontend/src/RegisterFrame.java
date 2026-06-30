package src;
import backend.src.main.java.com.company.Packets;
import src.network.SocketClient;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterFrame extends JFrame {

    private JTextField usernameInput;
    private JTextField emailInput;
    private JPasswordField passwordInput;
    private JPasswordField confirmPasswordInput;
    private JButton registerBtn;
    private JLabel errorMessageLabel;

    public RegisterFrame() {
        Color primaryColor = new Color(10, 25, 47);
        Color textPrimary = new Color(204, 214, 246);
        Color textSecondary = new Color(136, 146, 176);
        Color accentColor = new Color(100, 255, 218);
        final Color glassColor = new Color(17, 34, 64, 180);
        Color errorBgColor = new Color(255, 107, 107, 25);

        setTitle("Đăng Ký - Go Game");
        setSize(450, 560);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        getContentPane().setBackground(primaryColor);

        JPanel authBox = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
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
                BorderFactory.createLineBorder(new Color(100, 255, 218, 25), 1),
                BorderFactory.createEmptyBorder(40, 40, 40, 40)
        ));

        JLabel titleLabel = new JLabel("Đăng Ký");
        titleLabel.setFont(new Font("Inter", Font.BOLD, 32));
        titleLabel.setForeground(textPrimary);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel usernameLabel = new JLabel("Tên đăng nhập:");
        usernameLabel.setFont(new Font("Inter", Font.PLAIN, 14));
        usernameLabel.setForeground(textPrimary);
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        usernameInput = new JTextField();
        usernameInput.setMaximumSize(new Dimension(370, 45));
        usernameInput.setBackground(new Color(17, 34, 64, 127));
        usernameInput.setForeground(textPrimary);
        usernameInput.setCaretColor(accentColor);
        usernameInput.setBorder(BorderFactory.createLineBorder(new Color(100, 255, 218, 51), 2));

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Inter", Font.PLAIN, 14));
        emailLabel.setForeground(textPrimary);
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        emailInput = new JTextField();
        emailInput.setMaximumSize(new Dimension(370, 45));
        emailInput.setBackground(new Color(17, 34, 64, 127));
        emailInput.setForeground(textPrimary);
        emailInput.setCaretColor(accentColor);
        emailInput.setBorder(BorderFactory.createLineBorder(new Color(100, 255, 218, 51), 2));

        JLabel passwordLabel = new JLabel("Mật khẩu:");
        passwordLabel.setFont(new Font("Inter", Font.PLAIN, 14));
        passwordLabel.setForeground(textPrimary);
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        passwordInput = new JPasswordField();
        passwordInput.setMaximumSize(new Dimension(370, 45));
        passwordInput.setBackground(new Color(17, 34, 64, 127));
        passwordInput.setForeground(textPrimary);
        passwordInput.setCaretColor(accentColor);
        passwordInput.setBorder(BorderFactory.createLineBorder(new Color(100, 255, 218, 51), 2));

        JLabel confirmPasswordLabel = new JLabel("Xác nhận mật khẩu:");
        confirmPasswordLabel.setFont(new Font("Inter", Font.PLAIN, 14));
        confirmPasswordLabel.setForeground(textPrimary);
        confirmPasswordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        confirmPasswordInput = new JPasswordField();
        confirmPasswordInput.setMaximumSize(new Dimension(370, 45));
        confirmPasswordInput.setBackground(new Color(17, 34, 64, 127));
        confirmPasswordInput.setForeground(textPrimary);
        confirmPasswordInput.setCaretColor(accentColor);
        confirmPasswordInput.setBorder(BorderFactory.createLineBorder(new Color(100, 255, 218, 51), 2));

        registerBtn = new JButton("ĐĂNG KÝ");
        registerBtn.setBackground(accentColor);
        registerBtn.setForeground(primaryColor);
        registerBtn.setFont(new Font("Inter", Font.BOLD, 16));
        registerBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerBtn.setMaximumSize(new Dimension(370, 50));
        registerBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        registerBtn.setFocusPainted(false);
        registerBtn.setBorderPainted(false);

        errorMessageLabel = new JLabel("");
        errorMessageLabel.setFont(new Font("Inter", Font.PLAIN, 14));
        errorMessageLabel.setForeground(new Color(255, 107, 107));
        errorMessageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        errorMessageLabel.setOpaque(false);

        JLabel loginLinkLabel = new JLabel("Đã có tài khoản? Đăng nhập");
        loginLinkLabel.setFont(new Font("Inter", Font.PLAIN, 14));
        loginLinkLabel.setForeground(accentColor);
        loginLinkLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginLinkLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        authBox.add(titleLabel);
        authBox.add(Box.createVerticalStrut(30));
        authBox.add(usernameLabel);
        authBox.add(Box.createVerticalStrut(8));
        authBox.add(usernameInput);
        authBox.add(Box.createVerticalStrut(20));
        authBox.add(emailLabel);
        authBox.add(Box.createVerticalStrut(8));
        authBox.add(emailInput);
        authBox.add(Box.createVerticalStrut(20));
        authBox.add(passwordLabel);
        authBox.add(Box.createVerticalStrut(8));
        authBox.add(passwordInput);
        authBox.add(Box.createVerticalStrut(20));
        authBox.add(confirmPasswordLabel);
        authBox.add(Box.createVerticalStrut(8));
        authBox.add(confirmPasswordInput);
        authBox.add(Box.createVerticalStrut(25));
        authBox.add(registerBtn);
        authBox.add(Box.createVerticalStrut(15));
        authBox.add(errorMessageLabel);
        authBox.add(Box.createVerticalStrut(15));
        authBox.add(loginLinkLabel);

        add(authBox);

        setupActions(errorBgColor);
    }

    private void setupActions(final Color errBg) {
        final JTextField uInput = this.usernameInput;
        final JTextField eInput = this.emailInput;
        final JPasswordField pInput = this.passwordInput;
        final JPasswordField cpInput = this.confirmPasswordInput;
        final JLabel errLabel = this.errorMessageLabel;

        registerBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = uInput.getText().trim();
                String email = eInput.getText().trim();
                String password = new String(pInput.getPassword()).trim();
                String confirmPassword = new String(cpInput.getPassword()).trim();

                if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    errLabel.setOpaque(true);
                    errLabel.setBackground(errBg);
                    errLabel.setText(" Vui lòng điền đầy đủ tất cả thông tin! ");
                } else if (!password.equals(confirmPassword)) {
                    errLabel.setOpaque(true);
                    errLabel.setBackground(errBg);
                    errLabel.setText(" Mật khẩu xác nhận không trùng khớp! ");
                } else {
                    errLabel.setOpaque(false);
                    errLabel.setText(" Đang gửi yêu cầu đăng ký...");
                    registerBtn.setEnabled(false);

                    SocketClient client = SocketClient.getInstance();
                    client.setPacketListener(packet -> {
                        registerBtn.setEnabled(true);
                        switch (packet.getAction()) {
                            case Packets.REGISTER_OK:
                                errLabel.setOpaque(false);
                                errLabel.setText("");
                                JOptionPane.showMessageDialog(RegisterFrame.this, "Đăng ký tài khoản thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                                new Login().setVisible(true);
                                RegisterFrame.this.dispose();
                                break;
                            case Packets.REGISTER_FAIL:
                                errLabel.setOpaque(true);
                                errLabel.setBackground(errBg);
                                errLabel.setText(" " + (packet.get("message").isEmpty()
                                        ? "Đăng ký thất bại." : packet.get("message")) + " ");
                                break;
                            default:
                                // bỏ qua gói tin không liên quan màn hình Register
                        }
                    });

                    client.connectAsync(
                            () -> client.sendRegister(username, email, password),
                            () -> {
                                registerBtn.setEnabled(true);
                                errLabel.setOpaque(true);
                                errLabel.setBackground(errBg);
                                errLabel.setText(" Không thể kết nối Server (cổng 9000). ");
                            }
                    );
                }
                errLabel.revalidate();
                errLabel.repaint();
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new RegisterFrame().setVisible(true);
            }
        });
    }
}



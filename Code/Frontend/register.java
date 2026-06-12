import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Lớp chính RegisterFrame
public class RegisterFrame extends JFrame {

//  KHAI BÁO CÁC THÀNH PHẦN GIAO DIỆN
private JTextField usernameInput;
private JTextField emailInput;
private JPasswordField passwordInput;
private JPasswordField confirmPasswordInput;
private JButton registerBtn;
private JLabel errorMessageLabel;

public RegisterFrame() {
// 1. CẤU HÌNH CỬA SỔ CHÍNH
setTitle("Đăng Ký - Go Game");
setSize(420, 480);
setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
setLocationRelativeTo(null);
setLayout(new GridBagLayout());
getContentPane().setBackground(new Color(245, 246, 250));

// 2. KHUNG ĐĂNG KÝ
JPanel authBox = new JPanel();
authBox.setLayout(new BoxLayout(authBox, BoxLayout.Y_AXIS));
authBox.setBorder(BorderFactory.createCompoundBorder(
BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
BorderFactory.createEmptyBorder(25, 30, 25, 30)
));
authBox.setBackground(Color.WHITE);

// Tiêu đề
JLabel titleLabel = new JLabel("Đăng Ký");
titleLabel.setFont(new Font("Inter", Font.BOLD, 22));
titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

// Tên đăng nhập
JLabel usernameLabel = new JLabel("Tên đăng nhập:");
usernameLabel.setFont(new Font("Inter", Font.PLAIN, 13));
usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

usernameInput = new JTextField();
usernameInput.setMaximumSize(new Dimension(300, 35));

// Email
JLabel emailLabel = new JLabel("Email:");
emailLabel.setFont(new Font("Inter", Font.PLAIN, 13));
emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

emailInput = new JTextField();
emailInput.setMaximumSize(new Dimension(300, 35));

// Mật khẩu
JLabel passwordLabel = new JLabel("Mật khẩu:");
passwordLabel.setFont(new Font("Inter", Font.PLAIN, 13));
passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

passwordInput = new JPasswordField();
passwordInput.setMaximumSize(new Dimension(300, 35));

// Xác nhận mật khẩu
JLabel confirmPasswordLabel = new JLabel("Xác nhận mật khẩu:");
confirmPasswordLabel.setFont(new Font("Inter", Font.PLAIN, 13));
confirmPasswordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

confirmPasswordInput = new JPasswordField();
confirmPasswordInput.setMaximumSize(new Dimension(300, 35));

// Nút Đăng Ký
registerBtn = new JButton("Đăng Ký");
registerBtn.setBackground(new Color(0, 123, 255));
registerBtn.setForeground(Color.WHITE);
registerBtn.setFont(new Font("Inter", Font.BOLD, 14));
registerBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
registerBtn.setMaximumSize(new Dimension(300, 40));

// Thông báo lỗi
errorMessageLabel = new JLabel("");
errorMessageLabel.setFont(new Font("Inter", Font.ITALIC, 12));
errorMessageLabel.setForeground(Color.RED);
errorMessageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

// Đường dẫn đăng nhập
JLabel loginLinkLabel = new JLabel("Đã có tài khoản? Đăng nhập");
loginLinkLabel.setFont(new Font("Inter", Font.PLAIN, 12));
loginLinkLabel.setForeground(new Color(0, 123, 255));
loginLinkLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
loginLinkLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

// 3. THÊM CÁC THÀNH PHẦN VÀO KHUNG CHỨA
authBox.add(titleLabel);
authBox.add(Box.createVerticalStrut(15));
authBox.add(usernameLabel);
authBox.add(Box.createVerticalStrut(3));
authBox.add(usernameInput);
authBox.add(Box.createVerticalStrut(12));
authBox.add(emailLabel);
authBox.add(Box.createVerticalStrut(3));
authBox.add(emailInput);
authBox.add(Box.createVerticalStrut(12));
authBox.add(passwordLabel);
authBox.add(Box.createVerticalStrut(3));
authBox.add(passwordInput);
authBox.add(Box.createVerticalStrut(12));
authBox.add(confirmPasswordLabel);
authBox.add(Box.createVerticalStrut(3));
authBox.add(confirmPasswordInput);
authBox.add(Box.createVerticalStrut(15));
authBox.add(registerBtn);
authBox.add(Box.createVerticalStrut(8));
authBox.add(errorMessageLabel);
authBox.add(Box.createVerticalStrut(8));
authBox.add(loginLinkLabel);

add(authBox);

// Kích hoạt lắng nghe sự kiện
setupActions();
}

// 4. XỬ LÝ SỰ KIỆN ĐĂNG KÝ
private void setupActions() {
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
errLabel.setText("Vui lòng điền đầy đủ tất cả thông tin!");
} else {
if (!password.equals(confirmPassword)) {
errLabel.setText("Mật khẩu xác nhận không trùng khớp!");
} else {
errLabel.setText("");
JOptionPane.showMessageDialog(RegisterFrame.this, "Đăng ký tài khoản thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
}
}
}
});
}

// Hàm khởi chạy ứng dụng
public static void main(String[] args) {
SwingUtilities.invokeLater(new Runnable() {
@Override
public void run() {
new RegisterFrame().setVisible(true);
}
});
}
}

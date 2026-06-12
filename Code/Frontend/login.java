import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Lớp chính LoginFrame
public class LoginFrame extends JFrame {

// --- KHAI BÁO CÁC THÀNH PHẦN GIAO DIỆN ---
private JTextField usernameInput;
private JPasswordField passwordInput;
private JButton loginBtn;
private JLabel errorMessageLabel;

public LoginFrame() {
// 1. CẤU HÌNH CỬA SỔ CHÍNH
setTitle("Đăng Nhập - Go Game");
setSize(400, 350);
setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
setLocationRelativeTo(null);
setLayout(new GridBagLayout());
getContentPane().setBackground(new Color(245, 246, 250));

// 2. KHUNG ĐĂNG NHẬP
JPanel authBox = new JPanel();
authBox.setLayout(new BoxLayout(authBox, BoxLayout.Y_AXIS));
authBox.setBorder(BorderFactory.createCompoundBorder(
BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
BorderFactory.createEmptyBorder(25, 30, 25, 30)
));
authBox.setBackground(Color.WHITE);

// Tiêu đề
JLabel titleLabel = new JLabel("Đăng Nhập");
titleLabel.setFont(new Font("Inter", Font.BOLD, 22));
titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

// Nhãn Tên đăng nhập
JLabel usernameLabel = new JLabel("Tên đăng nhập:");
usernameLabel.setFont(new Font("Inter", Font.PLAIN, 13));
usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

usernameInput = new JTextField();
usernameInput.setMaximumSize(new Dimension(300, 35));

// Nhãn Mật khẩu
JLabel passwordLabel = new JLabel("Mật khẩu:");
passwordLabel.setFont(new Font("Inter", Font.PLAIN, 13));
passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

passwordInput = new JPasswordField();
passwordInput.setMaximumSize(new Dimension(300, 35));

// Nút Đăng Nhập
loginBtn = new JButton("Đăng Nhập");
loginBtn.setBackground(new Color(0, 123, 255));
loginBtn.setForeground(Color.WHITE);
loginBtn.setFont(new Font("Inter", Font.BOLD, 14));
loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
loginBtn.setMaximumSize(new Dimension(300, 40));

// Nhãn thông báo lỗi
errorMessageLabel = new JLabel("");
errorMessageLabel.setFont(new Font("Inter", Font.ITALIC, 12));
errorMessageLabel.setForeground(Color.RED);
errorMessageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

// Liên kết chuyển đổi đăng ký
JLabel registerLinkLabel = new JLabel("Chưa có tài khoản? Đăng ký ngay");
registerLinkLabel.setFont(new Font("Inter", Font.PLAIN, 12));
registerLinkLabel.setForeground(new Color(0, 123, 255));
registerLinkLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
registerLinkLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

// 3. THÊM CÁC THÀNH PHẦN VÀO KHUNG CHỨA
authBox.add(titleLabel);
authBox.add(Box.createVerticalStrut(20));
authBox.add(usernameLabel);
authBox.add(Box.createVerticalStrut(5));
authBox.add(usernameInput);
authBox.add(Box.createVerticalStrut(15));
authBox.add(passwordLabel);
authBox.add(Box.createVerticalStrut(5));
authBox.add(passwordInput);
authBox.add(Box.createVerticalStrut(20));
authBox.add(loginBtn);
authBox.add(Box.createVerticalStrut(10));
authBox.add(errorMessageLabel);
authBox.add(Box.createVerticalStrut(10));
authBox.add(registerLinkLabel);

add(authBox);

// Kích hoạt lắng nghe sự kiện
setupActions();
}

// 4. XỬ LÝ SỰ KIỆN
private void setupActions() {
final JTextField uInput = this.usernameInput;
final JPasswordField pInput = this.passwordInput;
final JLabel errLabel = this.errorMessageLabel;

loginBtn.addActionListener(new ActionListener() {
@Override
public void actionPerformed(ActionEvent e) {
String username = uInput.getText().trim();
String password = new String(pInput.getPassword()).trim();

if (username.isEmpty() || password.isEmpty()) {
errLabel.setText("Vui lòng điền đầy đủ thông tin!");
} else {
if (username.equals("admin") && password.equals("123456")) {
errLabel.setText("");
JOptionPane.showMessageDialog(LoginFrame.this, "Đăng nhập thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
} else {
errLabel.setText("Tên đăng nhập hoặc mật khẩu không đúng.");
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
new LoginFrame().setVisible(true);
}
});
}
}

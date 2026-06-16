import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Lớp chính LobbyRoom tạo sảnh chờ game
public class LobbyRoom extends JFrame {

// --- KHAI BÁO CÁC THÀNH PHẦN GIAO DIỆN ---
private JLabel currentUserLabel;
private JButton logoutBtn;
private JButton createRoomBtn;
private JButton refreshBtn;
private JPanel roomsListPanel;
private JLabel emptyMessageLabel;

public LobbyRoom() {
// 1. CẤU HÌNH CỬA SỔ CHÍNH
setTitle("Lobby - Go Game");
setSize(850, 600);
setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
setLocationRelativeTo(null);
setLayout(new BorderLayout(15, 15));

// 2. PHẦN HEADER
JPanel headerPanel = new JPanel(new BorderLayout());
headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
headerPanel.setBackground(new Color(40, 44, 52));

JLabel titleLabel = new JLabel("🎮 Go Game Lobby");
titleLabel.setFont(new Font("Inter", Font.BOLD, 22));
titleLabel.setForeground(Color.WHITE);

JPanel userInfoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
userInfoPanel.setOpaque(false);

currentUserLabel = new JLabel("Người chơi: KySuCoDoc");
currentUserLabel.setFont(new Font("Inter", Font.PLAIN, 14));
currentUserLabel.setForeground(Color.LIGHT_GRAY);

logoutBtn = new JButton("Đăng Xuất");
logoutBtn.setBackground(new Color(220, 53, 69));
logoutBtn.setForeground(Color.WHITE);

userInfoPanel.add(currentUserLabel);
userInfoPanel.add(logoutBtn);

headerPanel.add(titleLabel, BorderLayout.WEST);
headerPanel.add(userInfoPanel, BorderLayout.EAST);
add(headerPanel, BorderLayout.NORTH);

// 3. VÙNG NỘI DUNG CHÍNH
JPanel contentPanel = new JPanel(new BorderLayout(10, 15));
contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

// Thanh hành động điều khiển
JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));

createRoomBtn = new JButton("+ Tạo Phòng Mới");
createRoomBtn.setBackground(new Color(40, 167, 69));
createRoomBtn.setForeground(Color.WHITE);
createRoomBtn.setFont(new Font("Inter", Font.BOLD, 14));

refreshBtn = new JButton("🔄 Làm Mới");
refreshBtn.setBackground(new Color(0, 123, 255));
refreshBtn.setForeground(Color.WHITE);

actionsPanel.add(createRoomBtn);
actionsPanel.add(refreshBtn);
contentPanel.add(actionsPanel, BorderLayout.NORTH);

// Danh sách các phòng chơi
JPanel roomsSection = new JPanel(new BorderLayout(0, 10));

JLabel sectionTitle = new JLabel("Danh Sách Phòng");
sectionTitle.setFont(new Font("Inter", Font.BOLD, 16));
roomsSection.add(sectionTitle, BorderLayout.NORTH);

// Grid chứa các phòng chơi
roomsListPanel = new JPanel(new GridLayout(0, 2, 15, 15));

emptyMessageLabel = new JLabel("Chưa có phòng nào. Hãy tạo phòng mới!", SwingConstants.CENTER);
emptyMessageLabel.setFont(new Font("Inter", Font.ITALIC, 14));

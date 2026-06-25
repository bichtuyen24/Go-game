package src;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Lớp chính LobbyRoom tạo sảnh chờ game
public class lobby extends JFrame {

// --- KHAI BÁO CÁC THÀNH PHẦN GIAO DIỆN ---
private JLabel currentUserLabel;
private JButton logoutBtn;
private JButton createRoomBtn;
private JButton refreshBtn;
private JPanel roomsListPanel;
private JLabel emptyMessageLabel;

public lobby() {
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
emptyMessageLabel.setForeground(Color.GRAY);

JScrollPane scrollPane = new JScrollPane(roomsListPanel);
scrollPane.setBorder(null);

roomsSection.add(scrollPane, BorderLayout.CENTER);
contentPanel.add(roomsSection, BorderLayout.CENTER);

add(contentPanel, BorderLayout.CENTER);

// Kiểm tra phòng trống ban đầu
checkRoomsEmpty();

// Kích hoạt các sự kiện click nút
setupActions();
}

// 4. XỬ LÝ SỰ KIỆN NÚT BẤM
private void setupActions() {
// Sự kiện bấm nút "+ Tạo Phòng Mới"
    createRoomBtn.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
    showCreateRoomModal();
}
});

// Sự kiện bấm Đăng Xuất
logoutBtn.addActionListener(new ActionListener() {
@Override
public void actionPerformed(ActionEvent e) {
JOptionPane.showMessageDialog(lobby.this, "Đang đăng xuất khỏi hệ thống...", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
}
});

// Sự kiện bấm Làm Mới danh sách phòng
refreshBtn.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        // Ví dụ làm mới: xóa và thêm một phòng mẫu
        roomsListPanel.removeAll();
        addRoomItem("Phòng Kiện Tướng Go", "1");
        roomsListPanel.revalidate();
        roomsListPanel.repaint();
    }
});
    }

    // Hiển thị modal tạo phòng (cài đặt đơn giản)
    private void showCreateRoomModal() {
        String roomName = JOptionPane.showInputDialog(this, "Nhập tên phòng:", "Tạo Phòng Mới", JOptionPane.PLAIN_MESSAGE);
        if (roomName != null && !roomName.trim().isEmpty()) {
            addRoomItem(roomName.trim(), "0/2");
            roomsListPanel.revalidate();
            roomsListPanel.repaint();
        }
    }

    // Kiểm tra nếu danh sách phòng rỗng
    private void checkRoomsEmpty() {
        if (roomsListPanel.getComponentCount() == 0) {
            roomsListPanel.setLayout(new BorderLayout());
            roomsListPanel.removeAll();
            roomsListPanel.add(emptyMessageLabel, BorderLayout.CENTER);
        }
    }

    // Thêm một mục phòng đơn giản
    private void addRoomItem(String name, String players) {
        // Nếu trước đó đang hiển thị thông báo rỗng, đổi lại layout
        if (roomsListPanel.getLayout() instanceof BorderLayout) {
            roomsListPanel.setLayout(new GridLayout(0, 2, 15, 15));
            roomsListPanel.removeAll();
        }
        JPanel item = new JPanel(new BorderLayout());
        item.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        JLabel lbl = new JLabel(name + " (" + players + ")");
        lbl.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        item.add(lbl, BorderLayout.CENTER);
        JButton joinBtn = new JButton("Tham Gia");
        item.add(joinBtn, BorderLayout.SOUTH);
        roomsListPanel.add(item);
    }
}

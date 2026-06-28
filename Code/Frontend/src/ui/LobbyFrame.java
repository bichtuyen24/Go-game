package ui;

import javax.swing.*;
import java.awt.*;

public class LobbyFrame extends JFrame {

    private JLabel currentUserLabel;
    private JButton logoutBtn;
    private JButton createRoomBtn;
    private JButton refreshBtn;
    private JPanel roomsListPanel;
    private JLabel emptyMessageLabel;
    private String currentUsername = "KySuCoDoc";

    public LobbyFrame() {
        setTitle("Lobby - Go Game");
        setSize(850, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        headerPanel.setBackground(new Color(40, 44, 52));

        JLabel titleLabel = new JLabel("🎮 Go Game Lobby");
        titleLabel.setFont(new Font("Inter", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);

        JPanel userInfoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        userInfoPanel.setOpaque(false);

        currentUserLabel = new JLabel("Người chơi: " + currentUsername);
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

        JPanel contentPanel = new JPanel(new BorderLayout(10, 15));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

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

        JPanel roomsSection = new JPanel(new BorderLayout(0, 10));

        JLabel sectionTitle = new JLabel("Danh Sách Phòng");
        sectionTitle.setFont(new Font("Inter", Font.BOLD, 16));
        roomsSection.add(sectionTitle, BorderLayout.NORTH);

        roomsListPanel = new JPanel(new GridLayout(0, 2, 15, 15));

        emptyMessageLabel = new JLabel("Chưa có phòng nào. Hãy tạo phòng mới!", SwingConstants.CENTER);
        emptyMessageLabel.setFont(new Font("Inter", Font.ITALIC, 14));
        emptyMessageLabel.setForeground(Color.GRAY);

        JScrollPane scrollPane = new JScrollPane(roomsListPanel);
        scrollPane.setBorder(null);

        roomsSection.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(roomsSection, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);

        refreshRoomsList();
        setupActions();
    }

    public LobbyFrame(String username) {
        this(); // Gọi hàm dựng mặc định để dựng giao diện
        if (username != null && !username.trim().isEmpty()) {
            this.currentUsername = username;
            this.currentUserLabel.setText("Người chơi: " + username);
        }
        refreshRoomsList();
    }

    private void checkRoomsEmpty() {
        if (roomsListPanel.getComponentCount() == 0) {
            roomsListPanel.setLayout(new BorderLayout());
            roomsListPanel.add(emptyMessageLabel, BorderLayout.CENTER);
        } else {
            if (roomsListPanel.getLayout() instanceof BorderLayout) {
                roomsListPanel.removeAll();
                roomsListPanel.setLayout(new GridLayout(0, 2, 15, 15));
            }
        }
        roomsListPanel.revalidate();
        roomsListPanel.repaint();
    }

    private void addRoomItem(String roomName, String roomId) {
        if (roomsListPanel.getLayout() instanceof BorderLayout) {
            roomsListPanel.removeAll();
            roomsListPanel.setLayout(new GridLayout(0, 2, 15, 15));
        }

        JPanel roomCard = new JPanel(new BorderLayout(10, 10));
        roomCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        roomCard.setBackground(Color.WHITE);

        JPanel roomInfo = new JPanel(new GridLayout(2, 1, 2, 2));
        roomInfo.setOpaque(false);
        JLabel lblTitle = new JLabel(roomName);
        lblTitle.setFont(new Font("Inter", Font.BOLD, 14));
        JLabel lblSub = new JLabel("ID: " + roomId);
        lblSub.setForeground(Color.GRAY);
        roomInfo.add(lblTitle);
        roomInfo.add(lblSub);

        JButton joinBtn = new JButton("Vào Phòng");
        joinBtn.setBackground(new Color(0, 123, 255));
        joinBtn.setForeground(Color.WHITE);

        joinBtn.addActionListener(e -> {
            GameRoom gameWindow = new GameRoom(roomId, currentUsername);
            gameWindow.setVisible(true);
            this.dispose();
        });

        roomCard.add(roomInfo, BorderLayout.CENTER);
        roomCard.add(joinBtn, BorderLayout.EAST);

        roomsListPanel.add(roomCard);
        roomsListPanel.revalidate();
        roomsListPanel.repaint();
    }

    private void refreshRoomsList() {
        roomsListPanel.removeAll();
        addRoomItem("Phòng Kiện Tướng Go", "101");
        addRoomItem("Sân Chơi Nhập Môn", "102");
        checkRoomsEmpty();
    }

    private void showCreateRoomModal() {
        String roomName = JOptionPane.showInputDialog(this, "Nhập tên phòng muốn tạo:", "Tạo Phòng Mới", JOptionPane.PLAIN_MESSAGE);
        if (roomName != null && !roomName.trim().isEmpty()) {
            String generatedId = String.valueOf((int) (Math.random() * 899) + 100);
            GameRoom gameWindow = new GameRoom(generatedId, currentUsername);
            gameWindow.setVisible(true);
            this.dispose();
        }
    }

    private void setupActions() {
        createRoomBtn.addActionListener(e -> showCreateRoomModal());

        logoutBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Đang đăng xuất khỏi hệ thống...", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
            SwingUtilities.invokeLater(() -> {
                new LoginFrame().setVisible(true);
            });
        });

        refreshBtn.addActionListener(e -> refreshRoomsList());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LobbyFrame().setVisible(true));
    }
}
package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LobbyRoom extends JFrame {

    private JLabel currentUserLabel;
    private JButton logoutBtn;
    private JButton createRoomBtn;
    private JButton refreshBtn;
    private JPanel roomsListPanel;
    private JLabel emptyMessageLabel;
    private final String currentUsername;


    private final Color primaryColor = new Color(10, 25, 47);        // Nền tối chủ đạo
    private final Color bgPanelColor = new Color(17, 34, 64);        // Nền phụ đậm
    private final Color textPrimary = new Color(204, 214, 246);      // Chữ sáng thanh lịch
    private final Color textSecondary = new Color(136, 146, 176);    // Chữ phụ
    private final Color accentColor = new Color(100, 255, 218);      // Xanh Neon huyền thoại
    private final Color dangerColor = new Color(255, 107, 107);      // Đỏ cảnh báo

    public LobbyRoom() {
        this("KySuCoDoc");
    }

    public LobbyRoom(String username) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        this.currentUsername = (username != null && !username.trim().isEmpty()) ? username : "Player";

        setTitle("Lobby - Go Game");
        setSize(850, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));

        getContentPane().setBackground(primaryColor);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(100, 255, 218, 51)),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        headerPanel.setBackground(bgPanelColor);

        JLabel titleLabel = new JLabel("🎮 Go Game Lobby");
        titleLabel.setFont(new Font("Inter", Font.BOLD, 22));
        titleLabel.setForeground(accentColor);

        JPanel userInfoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        userInfoPanel.setOpaque(false);

        currentUserLabel = new JLabel("Người chơi: " + currentUsername);
        currentUserLabel.setFont(new Font("Inter", Font.PLAIN, 14));
        currentUserLabel.setForeground(textPrimary);

        logoutBtn = new JButton("Đăng Xuất");
        logoutBtn.setBackground(dangerColor);
        logoutBtn.setForeground(primaryColor);
        logoutBtn.setFont(new Font("Inter", Font.BOLD, 12));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        userInfoPanel.add(currentUserLabel);
        userInfoPanel.add(logoutBtn);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(userInfoPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new BorderLayout(10, 15));
        contentPanel.setBackground(primaryColor);
        contentPanel.setOpaque(true);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        actionsPanel.setOpaque(false);

        createRoomBtn = new JButton("+ Tạo Phòng Mới");
        createRoomBtn.setBackground(accentColor);
        createRoomBtn.setForeground(primaryColor);
        createRoomBtn.setFont(new Font("Inter", Font.BOLD, 14));
        createRoomBtn.setFocusPainted(false);
        createRoomBtn.setBorderPainted(false);
        createRoomBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        refreshBtn = new JButton("🔄 Làm Mới");
        refreshBtn.setBackground(bgPanelColor);
        refreshBtn.setForeground(accentColor);
        refreshBtn.setFont(new Font("Inter", Font.BOLD, 14));
        refreshBtn.setFocusPainted(false);
        refreshBtn.setBorder(BorderFactory.createLineBorder(accentColor, 1));
        refreshBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        actionsPanel.add(createRoomBtn);
        actionsPanel.add(refreshBtn);
        contentPanel.add(actionsPanel, BorderLayout.NORTH);

        JPanel roomsSection = new JPanel(new BorderLayout(0, 10));
        roomsSection.setOpaque(false);

        JLabel sectionTitle = new JLabel("Danh Sách Phòng Tự Do");
        sectionTitle.setFont(new Font("Inter", Font.BOLD, 16));
        sectionTitle.setForeground(textPrimary);
        roomsSection.add(sectionTitle, BorderLayout.NORTH);

        roomsListPanel = new JPanel(new GridLayout(0, 2, 15, 15));
        roomsListPanel.setBackground(primaryColor);

        emptyMessageLabel = new JLabel("Chưa có phòng nào. Hãy tạo phòng mới!", SwingConstants.CENTER);
        emptyMessageLabel.setFont(new Font("Inter", Font.ITALIC, 14));
        emptyMessageLabel.setForeground(textSecondary);

        JScrollPane scrollPane = new JScrollPane(roomsListPanel);
        scrollPane.setBorder(null);
        scrollPane.setBackground(primaryColor);
        scrollPane.setOpaque(true);
        scrollPane.getViewport().setBackground(primaryColor);
        scrollPane.getViewport().setOpaque(true);
        scrollPane.getVerticalScrollBar().setOpaque(false);
        scrollPane.getVerticalScrollBar().setBackground(primaryColor);

        roomsSection.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(roomsSection, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);

        refreshRoomsList();

        setupActions();
    }

    private void setupActions() {
        createRoomBtn.addActionListener(e -> showCreateRoomModal());

        logoutBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Đang đăng xuất khỏi hệ thống...", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
            SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
        });

        refreshBtn.addActionListener(e -> refreshRoomsList());
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
        roomCard.setBackground(bgPanelColor);
        roomCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 255, 218, 51), 1, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JPanel roomInfo = new JPanel(new GridLayout(2, 1, 2, 2));
        roomInfo.setOpaque(false);

        JLabel lblTitle = new JLabel(roomName);
        lblTitle.setFont(new Font("Inter", Font.BOLD, 15));
        lblTitle.setForeground(textPrimary);

        JLabel lblSub = new JLabel("ID: " + roomId);
        lblSub.setFont(new Font("Inter", Font.PLAIN, 12));
        lblSub.setForeground(textSecondary);
        roomInfo.add(lblTitle);
        roomInfo.add(lblSub);

        JButton joinBtn = new JButton("Vào Phòng");
        joinBtn.setFont(new Font("Inter", Font.BOLD, 13));
        joinBtn.setBackground(accentColor);
        joinBtn.setForeground(primaryColor);
        joinBtn.setFocusPainted(false);
        joinBtn.setBorderPainted(false);
        joinBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LobbyRoom("KySuCoDoc").setVisible(true));
    }
}
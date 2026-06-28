package ui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import board.BoardCanvas;
import network.SocketClient;

public class GameRoom extends JFrame {
    private JLabel roomNameLabel, roomStatusLabel;
    private JButton leaveRoomBtn, passBtn, resignBtn, sendChatBtn;
    private JTextArea playersListArea, chatMessagesArea;
    private JTextField chatInput;
    private JLabel currentTurnLabel, blackCapturesLabel, whiteCapturesLabel, pingValueLabel;

    private BoardCanvas gameBoard;
    private SocketClient socketClient;
    private String currentRoomId;
    private String currentUsername;
    private Timer pingTimerReference;

    public GameRoom() {
        this("Room_101", "Player_Guest");
    }

    public GameRoom(String roomId, String username) {
        this.currentRoomId = roomId;
        this.currentUsername = username;

        Color primaryColor = new Color(10, 25, 47);
        Color bgPanelColor = new Color(17, 34, 64);
        Color textPrimary = new Color(204, 214, 246);
        Color textSecondary = new Color(136, 146, 176);
        Color accentColor = new Color(100, 255, 218);
        Color dangerColor = new Color(255, 107, 107);

        setTitle("Game Room - Phòng: " + roomId + " - User: " + username);
        setSize(1100, 750);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(primaryColor);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        headerPanel.setBackground(bgPanelColor);
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(100, 255, 218, 51)));

        JPanel roomInfoPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        roomInfoPanel.setOpaque(false);

        roomNameLabel = new JLabel("Phòng Game: " + roomId);
        roomNameLabel.setFont(new Font("Inter", Font.BOLD, 20));
        roomNameLabel.setForeground(accentColor);

        roomStatusLabel = new JLabel("Chờ người chơi... (Bạn: " + username + ")");
        roomStatusLabel.setFont(new Font("Inter", Font.PLAIN, 13));
        roomStatusLabel.setForeground(textSecondary);
        roomInfoPanel.add(roomNameLabel);
        roomInfoPanel.add(roomStatusLabel);

        leaveRoomBtn = new JButton("Rời Phòng");
        leaveRoomBtn.setFont(new Font("Inter", Font.BOLD, 13));
        leaveRoomBtn.setBackground(dangerColor);
        leaveRoomBtn.setForeground(primaryColor);
        leaveRoomBtn.setFocusPainted(false);
        leaveRoomBtn.setBorderPainted(false);
        leaveRoomBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        headerPanel.add(roomInfoPanel, BorderLayout.WEST);
        headerPanel.add(leaveRoomBtn, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        JPanel mainContentPanel = new JPanel(new BorderLayout(15, 15));
        mainContentPanel.setOpaque(false);
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));

        JPanel leftPanel = new JPanel();
        leftPanel.setOpaque(false);
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setPreferredSize(new Dimension(260, 0));

        JPanel playersSection = new JPanel(new BorderLayout());
        playersSection.setBackground(bgPanelColor);
        playersSection.setBorder(createCyberpunkTitledBorder("Người Chơi", accentColor));

        playersListArea = new JTextArea(8, 20);
        playersListArea.setFont(new Font("Inter", Font.PLAIN, 13));
        playersListArea.setEditable(false);
        playersListArea.setForeground(textPrimary);
        playersListArea.setBackground(bgPanelColor);
        playersListArea.setText("- " + username + " (Chủ phòng)\n- Đang đợi đối thủ kết nối...");

        JScrollPane playersScroll = new JScrollPane(playersListArea);
        playersScroll.setBorder(BorderFactory.createEmptyBorder());
        playersSection.add(playersScroll, BorderLayout.CENTER);

        JPanel gameInfoSection = new JPanel(new GridLayout(3, 1, 5, 8));
        gameInfoSection.setBackground(bgPanelColor);
        gameInfoSection.setBorder(createCyberpunkTitledBorder("Thông Tin Game", accentColor));

        currentTurnLabel = new JLabel("Lượt: Bạn (Quân Đen)");
        currentTurnLabel.setFont(new Font("Inter", Font.BOLD, 14));
        currentTurnLabel.setForeground(accentColor);

        blackCapturesLabel = new JLabel("Quân đen: 0");
        blackCapturesLabel.setFont(new Font("Inter", Font.PLAIN, 13));
        blackCapturesLabel.setForeground(textPrimary);

        whiteCapturesLabel = new JLabel("Quân trắng: 0");
        whiteCapturesLabel.setFont(new Font("Inter", Font.PLAIN, 13));
        whiteCapturesLabel.setForeground(textPrimary);

        gameInfoSection.add(currentTurnLabel);
        gameInfoSection.add(blackCapturesLabel);
        gameInfoSection.add(whiteCapturesLabel);

        leftPanel.add(playersSection);
        leftPanel.add(Box.createVerticalStrut(15));
        leftPanel.add(gameInfoSection);
        mainContentPanel.add(leftPanel, BorderLayout.WEST);

        JPanel boardSection = new JPanel(new BorderLayout(5, 10));
        boardSection.setOpaque(false);

        gameBoard = new BoardCanvas();
        gameBoard.setScoreLabels(blackCapturesLabel, whiteCapturesLabel);
        socketClient = new SocketClient();

        gameBoard.setSocketClient(socketClient);
        socketClient.connectAsync(gameBoard);

        boardSection.add(gameBoard, BorderLayout.CENTER);

        JPanel gameControls = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        gameControls.setOpaque(false);

        passBtn = new JButton("Bỏ Lượt");
        passBtn.setFont(new Font("Inter", Font.BOLD, 14));
        passBtn.setBackground(bgPanelColor);
        passBtn.setForeground(accentColor);
        passBtn.setBorder(BorderFactory.createLineBorder(accentColor, 1));
        passBtn.setPreferredSize(new Dimension(120, 40));
        passBtn.setFocusPainted(false);
        passBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        resignBtn = new JButton("Xin Thua");
        resignBtn.setFont(new Font("Inter", Font.BOLD, 14));
        resignBtn.setBackground(dangerColor);
        resignBtn.setForeground(primaryColor);
        resignBtn.setBorderPainted(false);
        resignBtn.setPreferredSize(new Dimension(120, 40));
        resignBtn.setFocusPainted(false);
        resignBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        gameControls.add(passBtn);
        gameControls.add(resignBtn);
        boardSection.add(gameControls, BorderLayout.SOUTH);

        mainContentPanel.add(boardSection, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel();
        rightPanel.setOpaque(false);
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setPreferredSize(new Dimension(260, 0));

        JPanel chatSection = new JPanel(new BorderLayout());
        chatSection.setBackground(bgPanelColor);
        chatSection.setBorder(createCyberpunkTitledBorder("Trò Chuyện", accentColor));

        chatMessagesArea = new JTextArea(15, 20);
        chatMessagesArea.setFont(new Font("Inter", Font.PLAIN, 13));
        chatMessagesArea.setEditable(false);
        chatMessagesArea.setForeground(textSecondary);
        chatMessagesArea.setBackground(bgPanelColor);

        JScrollPane chatScroll = new JScrollPane(chatMessagesArea);
        chatScroll.setBorder(BorderFactory.createEmptyBorder());
        chatSection.add(chatScroll, BorderLayout.CENTER);

        JPanel chatForm = new JPanel(new BorderLayout(5, 5));
        chatForm.setOpaque(false);
        chatForm.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        chatInput = new JTextField();
        chatInput.setBackground(new Color(10, 25, 47));
        chatInput.setForeground(textPrimary);
        chatInput.setCaretColor(accentColor);
        chatInput.setFont(new Font("Inter", Font.PLAIN, 13));
        chatInput.setBorder(BorderFactory.createLineBorder(new Color(100, 255, 218, 51), 1));

        sendChatBtn = new JButton("Gửi");
        sendChatBtn.setFont(new Font("Inter", Font.BOLD, 13));
        sendChatBtn.setBackground(accentColor);
        sendChatBtn.setForeground(primaryColor);
        sendChatBtn.setBorderPainted(false);
        sendChatBtn.setFocusPainted(false);
        sendChatBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        chatForm.add(chatInput, BorderLayout.CENTER);
        chatForm.add(sendChatBtn, BorderLayout.EAST);
        chatSection.add(chatForm, BorderLayout.SOUTH);

        JPanel pingSection = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pingSection.setBackground(bgPanelColor);
        pingSection.setBorder(createCyberpunkTitledBorder("Tốc độ mạng", accentColor));

        pingValueLabel = new JLabel("Đang tính toán...");
        pingValueLabel.setFont(new Font("Inter", Font.PLAIN, 13));
        pingValueLabel.setForeground(accentColor);
        pingSection.add(pingValueLabel);

        rightPanel.add(chatSection);
        rightPanel.add(Box.createVerticalStrut(15));
        rightPanel.add(pingSection);
        mainContentPanel.add(rightPanel, BorderLayout.EAST);

        add(mainContentPanel, BorderLayout.CENTER);

        if (socketClient != null) {
            this.pingTimerReference = socketClient.startNetworkPingTicker(pingValueLabel, roomId);
        }

        setupActions(username);
    }

    private TitledBorder createCyberpunkTitledBorder(String title, Color accentColor) {
        TitledBorder border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(100, 255, 218, 51), 1),
                title
        );
        border.setTitleFont(new Font("Inter", Font.BOLD, 12));
        border.setTitleColor(accentColor);
        return border;
    }

    private void setupActions(String username) {
        resignBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Bạn có chắc chắn muốn xin thua trận đấu này không?",
                    "Xác nhận đầu hàng",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (confirm == JOptionPane.YES_OPTION) {
                if (socketClient != null) {
                    socketClient.sendPacketMove(-1, -1, currentRoomId, "RESIGN");
                }
                closeRoomAndReturnToLobby();
            }
        });

        passBtn.addActionListener(e -> currentTurnLabel.setText("Lượt: Đối thủ"));

        ActionListener sendChatAction = e -> {
            String text = chatInput.getText().trim();
            if (!text.isEmpty()) {
                chatMessagesArea.append("Bạn: " + text + "\n");
                chatInput.setText("");
            }
            if (gameBoard != null) {
                gameBoard.requestFocusInWindow();
            }
        };
        sendChatBtn.addActionListener(sendChatAction);
        chatInput.addActionListener(sendChatAction);

        leaveRoomBtn.addActionListener(e -> {
            if (socketClient != null) {
                socketClient.sendPacketMove(-1, -1, currentRoomId, "LEAVE");
            }
            closeRoomAndReturnToLobby();
        });
    }

    public void handleOpponentResign() {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(this, "Đối thủ đã xin thua! Bạn giành chiến thắng vang dội!", "Chiến Thắng 🎉", JOptionPane.INFORMATION_MESSAGE);
            closeRoomAndReturnToLobby();
        });
    }

    private void closeRoomAndReturnToLobby() {
        if (pingTimerReference != null) {
            pingTimerReference.stop();
        }
        this.dispose();

        SwingUtilities.invokeLater(() -> {
            LobbyRoom lobby = new LobbyRoom(currentUsername);
            lobby.setVisible(true);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GameRoom().setVisible(true));
    }
}
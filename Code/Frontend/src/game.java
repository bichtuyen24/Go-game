package src;
import src.board.BoardCanvas;
import backend.src.main.java.com.company.Packets;
import src.network.SocketClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Màn hình phòng chơi - ĐÃ TÍCH HỢP với backend qua SocketClient (cổng 9000).
 * Dùng org.example.board.BoardCanvas (bàn cờ đầy đủ: vẽ quân, hiệu ứng đặt quân,
 * nhận nước đi từ Server) thay cho lớp GoBoardCanvas tĩnh chỉ vẽ lưới trước đây.
 */
public class game extends JFrame {
    private JLabel roomNameLabel, roomStatusLabel;
    private JButton leaveRoomBtn, passBtn, resignBtn, sendChatBtn, pingBtn;
    private JTextArea playersListArea, chatMessagesArea;
    private JTextField chatInput;
    private JLabel currentTurnLabel, blackCapturesLabel, whiteCapturesLabel, pingValueLabel;
    private BoardCanvas gameBoard;

    private final String currentUsername;
    private final String roomId;

    /** Constructor mặc định (giữ tương thích, dùng để test UI độc lập không cần server). */
    public game() {
        this("Khách", "01");
    }

    /** Constructor mới: nhận username + roomId thật từ màn hình lobby sau khi vào phòng. */
    public game(String username, String roomId) {
        this.currentUsername = username;
        this.roomId = roomId;

        setTitle("Game Room - Go Game");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        headerPanel.setBackground(new Color(240, 240, 240));

        JPanel roomInfoPanel = new JPanel(new GridLayout(2, 1));
        roomNameLabel = new JLabel("Phòng Game #" + roomId);
        roomNameLabel.setFont(new Font("Inter", Font.BOLD, 18));
        roomStatusLabel = new JLabel("Chờ người chơi...");
        roomStatusLabel.setForeground(Color.GRAY);
        roomInfoPanel.add(roomNameLabel);
        roomInfoPanel.add(roomStatusLabel);

        leaveRoomBtn = new JButton("Rời Phòng");
        leaveRoomBtn.setBackground(new Color(220, 53, 69));
        leaveRoomBtn.setForeground(Color.WHITE);

        headerPanel.add(roomInfoPanel, BorderLayout.WEST);
        headerPanel.add(leaveRoomBtn, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        JPanel mainContentPanel = new JPanel(new BorderLayout(10, 10));
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setPreferredSize(new Dimension(250, 0));

        JPanel playersSection = new JPanel(new BorderLayout());
        playersSection.setBorder(BorderFactory.createTitledBorder("Người Chơi"));
        playersListArea = new JTextArea(currentUsername + "\n", 8, 20);
        playersListArea.setEditable(false);
        playersSection.add(new JScrollPane(playersListArea), BorderLayout.CENTER);

        JPanel gameInfoSection = new JPanel(new GridLayout(3, 1, 5, 5));
        gameInfoSection.setBorder(BorderFactory.createTitledBorder("Thông Tin Game"));
        currentTurnLabel = new JLabel("Lượt: -");
        blackCapturesLabel = new JLabel("Quân đen: 0");
        whiteCapturesLabel = new JLabel("Quân trắng: 0");
        gameInfoSection.add(currentTurnLabel);
        gameInfoSection.add(blackCapturesLabel);
        gameInfoSection.add(whiteCapturesLabel);

        leftPanel.add(playersSection);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(gameInfoSection);
        mainContentPanel.add(leftPanel, BorderLayout.WEST);

        JPanel boardSection = new JPanel(new BorderLayout(5, 5));

        // ── Bàn cờ thật (vẽ quân, hiệu ứng, nhận nước đi từ server qua SocketClient) ──
        gameBoard = new BoardCanvas();
        gameBoard.setBackground(new Color(219, 172, 100));
        boardSection.add(gameBoard, BorderLayout.CENTER);

        JPanel gameControls = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        passBtn = new JButton("Bỏ Lượt");
        resignBtn = new JButton("Xin Thua");
        resignBtn.setBackground(new Color(220, 53, 69));
        resignBtn.setForeground(Color.WHITE);
        gameControls.add(passBtn);
        gameControls.add(resignBtn);
        boardSection.add(gameControls, BorderLayout.SOUTH);

        mainContentPanel.add(boardSection, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setPreferredSize(new Dimension(250, 0));

        JPanel chatSection = new JPanel(new BorderLayout());
        chatSection.setBorder(BorderFactory.createTitledBorder("Chat"));
        chatMessagesArea = new JTextArea(15, 20);
        chatMessagesArea.setEditable(false);
        chatSection.add(new JScrollPane(chatMessagesArea), BorderLayout.CENTER);

        JPanel chatForm = new JPanel(new BorderLayout(5, 5));
        chatInput = new JTextField();
        sendChatBtn = new JButton("Gửi");
        chatForm.add(chatInput, BorderLayout.CENTER);
        chatForm.add(sendChatBtn, BorderLayout.EAST);
        chatSection.add(chatForm, BorderLayout.SOUTH);

        JPanel pingSection = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pingSection.setBorder(BorderFactory.createTitledBorder("Ping"));
        pingValueLabel = new JLabel("- ms");
        pingBtn = new JButton("Ping");
        pingSection.add(pingValueLabel);
        pingSection.add(pingBtn);

        rightPanel.add(chatSection);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(pingSection);
        mainContentPanel.add(rightPanel, BorderLayout.EAST);

        add(mainContentPanel, BorderLayout.CENTER);

        setupActions();
    }

    private void setupActions() {
        SocketClient client = SocketClient.getInstance();
        client.setUsername(currentUsername);
        client.setSocketCanvas(gameBoard);   // BoardCanvas sẽ tự vẽ khi nhận gói MOVE/PING từ server
        gameBoard.setSocketClient(client);   // BoardCanvas tự gọi client.sendMove() khi người chơi click

        // Lắng nghe các sự kiện không liên quan trực tiếp tới bàn cờ: lượt đi, lỗi, chat, kết thúc ván
        client.setPacketListener(packet -> {
            switch (packet.getAction()) {
                case "MOVE":
                    String[] moveData = packet.getData().split(",");
                    int r = Integer.parseInt(moveData[0]);
                    int c = Integer.parseInt(moveData[1]);
                    String colorStr = moveData[2];

                    // Đẩy dữ liệu vào bàn cờ vẽ thực tế
                    gameBoard.handleServerMove(r, c, colorStr);

                    currentTurnLabel.setText("Lượt: Của Bạn");
                    break;
                case "GAME_ERROR":
                    chatMessagesArea.append("[Hệ thống]: " + packet.getData() + "\n");
                    break;
                case "CHAT":
                    chatMessagesArea.append("[" + packet.getSender() + "]: " + packet.getData() + "\n");
                    break;
                case "PASS":
                    chatMessagesArea.append("[Hệ thống]: " + packet.getSender() + " đã Bỏ Lượt.\n");
                    break;
                case "RESIGN":
                    roomStatusLabel.setText(packet.getSender() + " đã Xin Thua. Trận đấu kết thúc.");
                    break;
                case Packets.GAME_OVER:
                    roomStatusLabel.setText("Kết thúc ván: " + packet.getData());
                    JOptionPane.showMessageDialog(game.this, "Trận đấu kết thúc!\n" + packet.getData(), "Tính điểm", JOptionPane.INFORMATION_MESSAGE);
                    break;
                default:
                    // ROOM_JOINED, JOIN_ROOM... đã được lobby xử lý trước đó, ở đây bỏ qua
            }
        });

        resignBtn.addActionListener(e -> {
            client.sendResign(roomId);
            JOptionPane.showMessageDialog(this, "Bạn đã xin thua trận đấu!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            new lobby(currentUsername).setVisible(true);
            this.dispose();
        });

        passBtn.addActionListener(e -> {
            client.sendPass(roomId);
            currentTurnLabel.setText("Lượt: Đối thủ");
        });

        ActionListener sendChatAction = e -> {
            String text = chatInput.getText().trim();
            if (!text.isEmpty()) {
                client.sendChat(roomId, text);
                chatMessagesArea.append("Bạn: " + text + "\n");
                chatInput.setText("");
            }
        };
        sendChatBtn.addActionListener(sendChatAction);
        chatInput.addActionListener(sendChatAction);

        pingBtn.addActionListener(e -> {
            // Ping minh hoạ tại tâm bàn cờ (giữa giao lộ 9,9) - gửi thật lên server qua SocketClient
                client.sendPing(9, 9);
                int fakePing = (int) (Math.random() * 50) + 10;
                pingValueLabel.setText(fakePing + " ms");
        });

        leaveRoomBtn.addActionListener(e -> {
//            client.sendLeaveRoom(roomId);
//            dispose();

            // Mở lại giao diện Lobby với username hiện tại
            new lobby(currentUsername).setVisible(true);
            // Đóng giao diện Game
            this.dispose();
        });



    }
    // Thêm hàm này vào game.java để BoardCanvas có thể gọi cập nhật giao diện chữ chữ trực tiếp
    public void updateTurnStatusLabel(String text) {
        if (currentTurnLabel != null) {
            currentTurnLabel.setText(text);
            if (text.contains("Của Bạn")) {
                currentTurnLabel.setForeground(new Color(40, 167, 69)); // Chữ màu xanh lá nếu tới lượt mình
            } else {
                currentTurnLabel.setForeground(Color.RED); // Chữ màu đỏ nếu là lượt đối thủ
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new game().setVisible(true));
    }
}
package src.ui;

import src.board.BoardCanvas;
import src.network.SocketClient;
import javax.swing.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;

public class GameScene   {
    private final String currentUsername;

    public GameScene(String username) {
        this.currentUsername = username;
    }

    public JPanel createMainPanel() {

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.decode("#2F3136"));


        BoardCanvas canvas = new BoardCanvas();


        SocketClient client = new SocketClient();
        canvas.setSocketClient(client);
        client.connectAsync(canvas);

        JPanel canvasContainer = new JPanel(new GridBagLayout());
        canvasContainer.setBackground(Color.decode("#2F3136"));
        canvasContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        canvasContainer.add(canvas);

        root.add(canvasContainer, BorderLayout.CENTER);



        JPanel rightSidebar = new JPanel(new BorderLayout(0, 15));
        rightSidebar.setBackground(Color.decode("#2F3136"));
        rightSidebar.setPreferredSize(new Dimension(300, 640));
        rightSidebar.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 20));


        JPanel playerInfoBox = new JPanel();
        playerInfoBox.setLayout(new BoxLayout(playerInfoBox, BoxLayout.Y_AXIS));
        playerInfoBox.setBackground(Color.decode("#202225"));
        playerInfoBox.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel titleLabel = new JLabel("PHÒNG CHƠI #01");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 15));

        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1)); // Đường kẻ ngang

        JLabel p1Label = new JLabel("⚫ Người chơi 1: " + currentUsername);
        p1Label.setForeground(Color.lightGray);

        JLabel p2Label = new JLabel("⚪ Người chơi 2: Đang đợi đối thủ...");
        p2Label.setForeground(Color.lightGray);

        playerInfoBox.add(titleLabel);
        playerInfoBox.add(Box.createRigidArea(new Dimension(0, 5))); // Khoảng cách
        playerInfoBox.add(separator);
        playerInfoBox.add(Box.createRigidArea(new Dimension(0, 5)));
        playerInfoBox.add(p1Label);
        playerInfoBox.add(Box.createRigidArea(new Dimension(0, 5)));
        playerInfoBox.add(p2Label);


        JPanel chatBox = new JPanel(new BorderLayout(0, 8));
        chatBox.setBackground(Color.decode("#202225"));
        chatBox.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel chatTitle = new JLabel("Trò Chuyện Realtime");
        chatTitle.setForeground(Color.WHITE);
        chatTitle.setFont(new Font("Arial", Font.BOLD, 13));
        chatBox.add(chatTitle, BorderLayout.NORTH);

        JTextArea chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setText("[Hệ thống]: Chào mừng bạn vào phòng chơi cờ vây!\n");
        chatArea.setBackground(Color.decode("#36393F"));
        chatArea.setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(chatArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Tắt viền xấu
        chatBox.add(scrollPane, BorderLayout.CENTER);

        JPanel chatInputRow = new JPanel(new BorderLayout(8, 0));
        chatInputRow.setBackground(Color.decode("#202225"));

        JTextField chatInputField = new JTextField();
        chatInputField.setBackground(Color.decode("#40444B"));
        chatInputField.setForeground(Color.WHITE);
        chatInputField.setCaretColor(Color.WHITE); // Đổi màu con trỏ chuột
        chatInputField.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JButton sendButton = new JButton("Gửi");
        sendButton.setBackground(Color.decode("#7289DA"));
        sendButton.setForeground(Color.WHITE);
        sendButton.setFont(new Font("Arial", Font.BOLD, 12));
        sendButton.setFocusPainted(false);

        sendButton.addActionListener(e -> {
            String text = chatInputField.getText();
            if (!text.trim().isEmpty()) {
                chatArea.append("[" + currentUsername + "]: " + text + "\n");
                chatInputField.setText(""); // Xóa trắng ô nhập
            }
        });

        chatInputField.addActionListener(e -> sendButton.doClick());

        chatInputRow.add(chatInputField, BorderLayout.CENTER);
        chatInputRow.add(sendButton, BorderLayout.EAST);

        chatBox.add(chatInputRow, BorderLayout.SOUTH);


        JPanel actionButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        actionButtons.setBackground(Color.decode("#2F3136"));

        JButton readyBtn = new JButton("Sẵn Sàng");
        readyBtn.setBackground(Color.decode("#43B581"));
        readyBtn.setForeground(Color.WHITE);
        readyBtn.setFont(new Font("Arial", Font.BOLD, 12));
        readyBtn.setPreferredSize(new Dimension(120, 35));
        readyBtn.setFocusPainted(false);

        JButton resignBtn = new JButton("Đầu Hàng");
        resignBtn.setBackground(Color.decode("#F04747"));
        resignBtn.setForeground(Color.WHITE);
        resignBtn.setFont(new Font("Arial", Font.BOLD, 12));
        resignBtn.setPreferredSize(new Dimension(120, 35));
        resignBtn.setFocusPainted(false);

        actionButtons.add(readyBtn);
        actionButtons.add(resignBtn);


        rightSidebar.add(playerInfoBox, BorderLayout.NORTH);
        rightSidebar.add(chatBox, BorderLayout.CENTER);
        rightSidebar.add(actionButtons, BorderLayout.SOUTH);

        root.add(rightSidebar, BorderLayout.EAST);
        return root;
    }

}



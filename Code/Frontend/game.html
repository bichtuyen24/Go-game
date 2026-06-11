import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameRoom extends JFrame {
private JLabel roomNameLabel, roomStatusLabel;
private JButton leaveRoomBtn, passBtn, resignBtn, sendChatBtn, pingBtn;
private JTextArea playersListArea, chatMessagesArea;
private JTextField chatInput;
private JLabel currentTurnLabel, blackCapturesLabel, whiteCapturesLabel, pingValueLabel;
private GoBoardCanvas gameBoard;

public GameRoom() {
setTitle("Game Room - Go Game");
setSize(1100, 700);
setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
setLocationRelativeTo(null);
setLayout(new BorderLayout(10, 10));

JPanel headerPanel = new JPanel(new BorderLayout());
headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
headerPanel.setBackground(new Color(240, 240, 240));

JPanel roomInfoPanel = new JPanel(new GridLayout(2, 1));
roomNameLabel = new JLabel("Phòng Game");
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
playersListArea = new JTextArea(8, 20);
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

gameBoard = new GoBoardCanvas();
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
resignBtn.addActionListener(e ->
JOptionPane.showMessageDialog(this, "Bạn đã xin thua trận đấu!", "Thông báo", JOptionPane.INFORMATION_MESSAGE)
);

passBtn.addActionListener(e -> currentTurnLabel.setText("Lượt: Đối thủ"));

ActionListener sendChatAction = e -> {
String text = chatInput.getText().trim();
if (!text.isEmpty()) {
chatMessagesArea.append("Bạn: " + text + "\n");
chatInput.setText("");
}
};
sendChatBtn.addActionListener(sendChatAction);
chatInput.addActionListener(sendChatAction);

pingBtn.addActionListener(e -> {
int fakePing = (int) (Math.random() * 50) + 10;
pingValueLabel.setText(fakePing + " ms");
});
}

public static void main(String[] args) {
SwingUtilities.invokeLater(() -> new GameRoom().setVisible(true));
}

static class GoBoardCanvas extends JPanel {
@Override
protected void paintComponent(Graphics g) {
super.paintComponent(g);
Graphics2D g2 = (Graphics2D) g;
g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

int size = 19;
int margin = 30;
int width = getWidth() - (margin * 2);
int height = getHeight() - (margin * 2);

int cellWidth = width / (size - 1);
int cellHeight = height / (size - 1);

g2.setColor(Color.BLACK);

for (int i = 0; i < size; i++) {
g2.drawLine(margin, margin + (i * cellHeight), margin + ((size - 1) * cellWidth), margin + (i * cellHeight));
g2.drawLine(margin + (i * cellWidth), margin, margin + (i * cellWidth), margin + ((size - 1) * cellHeight));
}

int[] starPoints = {3, 9, 15};
for (int x : starPoints) {
for (int y : starPoints) {
int r = 4;
g2.fillOval(margin + (x * cellWidth) - r, margin + (y * cellHeight) - r, r * 2, r * 2);
}
}
}
}
}

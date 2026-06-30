package src.board;

import src.network.SocketClient;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import src.game;


public class BoardCanvas extends JPanel { // 1. Chuyển từ Canvas thành JPanel để kích hoạt Double-Buffering chống chớp hình
    private final BoardState boardState;
    private final BoardRenderer renderer;
    private final List<PingEffect> activePings = new ArrayList<>();
    private SocketClient socketClient;

    // --- LOGIC QUẢN LÝ LƯỢT CHƠI ---
    private Color myColor = Color.BLACK;     // Màu quân cờ của bạn (Server sẽ đồng bộ, mặc định chọn Đen)
    private boolean isMyTurn = true;         // Cờ hiệu kiểm tra xem có phải lượt của bạn không

    public BoardCanvas() {
        this.setPreferredSize(new Dimension(600, 600)); // Dùng PreferredSize thay cho setSize
        this.boardState = new BoardState();
        this.renderer = new BoardRenderer();

        // Chắc chắn rằng Double Buffered đã được bật để đồ họa chạy siêu mượt 60fps
        this.setDoubleBuffered(true);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                onCanvasClicked(event);
            }
        });

        Timer gameLoop = new Timer(16, e -> {
            updateEffects();
            repaint();
        });
        gameLoop.start();
    }

    public void setSocketClient(SocketClient client) {
        this.socketClient = client;
    }

    // Hàm để file game.java thiết lập cấu hình khi vào trận (Ví dụ bạn cầm quân Trắng hay Đen)
    public void setPlayerConfig(Color assignedColor, boolean startingTurn) {
        this.myColor = assignedColor;
        this.isMyTurn = startingTurn;
    }

    public void setMyTurn(boolean myTurn) {
        this.isMyTurn = myTurn;
    }

    public boolean isMyTurn() {
        return isMyTurn;
    }

    private void onCanvasClicked(MouseEvent event) {
        // 2. NGĂN CHẶN ĐẶT 2 QUÂN LIÊN TIẾP: Kiểm tra lượt đi trước khi tính toán tọa độ
        if (!isMyTurn) {
            JOptionPane.showMessageDialog(this,
                    "Chưa đến lượt của bạn! Vui lòng đợi đối thủ đi xong.",
                    "Cảnh Báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        double mouseX = event.getX();
        double mouseY = event.getY();

        double pad = renderer.getPadding();
        double cSize = renderer.getCellSize();

        int col = (int) Math.round((mouseX - pad) / cSize);
        int row = (int) Math.round((mouseY - pad) / cSize);

        // Đặt quân cờ dựa trên màu CHUẨN của bạn đã được cấu hình
        if (boardState.placeStone(row, col, myColor)) {
            // Chặn click tiếp ngay lập tức sau khi đặt quân thành công
            isMyTurn = false;

            if (socketClient != null) {
                socketClient.sendMove(row, col);
            }

            // Tìm cách cập nhật Label hiển thị lượt đi trên màn hình chính
            updateGameTurnLabel("Lượt: Đối thủ");
        }
    }

    public void handleServerMove(int row, int col, String colorStr) {
        Color stoneColor = "WHITE".equalsIgnoreCase(colorStr) ? Color.WHITE : Color.BLACK;

        if (boardState.placeStone(row, col, stoneColor)) {
            double pad = renderer.getPadding();
            double cSize = renderer.getCellSize();
            double x = pad + col * cSize;
            double y = pad + row * cSize;

            activePings.add(new PingEffect(x, y));

            // 3. CHUYỂN LƯỢT: Nếu nước đi vừa nhận từ server là của đối thủ -> Trả lại lượt cho bạn
            if (stoneColor != myColor) {
                this.isMyTurn = true;
                updateGameTurnLabel("Lượt: Của Bạn");
            }

            repaint();
        }
    }

    // Hàm hỗ trợ tìm kiếm Container cha để đổi text hiển thị trạng thái lượt đi
    private void updateGameTurnLabel(String status) {
        Component parent = SwingUtilities.getWindowAncestor(this);
        if (parent instanceof game) {
            // Giả định bạn bổ sung hàm updateTurnText trong file game.java công khai
            ((game) parent).updateTurnStatusLabel(status);
        }
    }

    private void updateEffects() {
        Iterator<PingEffect> iterator = activePings.iterator();
        while (iterator.hasNext()) {
            PingEffect ping = iterator.next();
            ping.update();
            if (!ping.isActive()) {
                iterator.remove();
            }
        }
    }

    // 4. Thay thế hàm paint(Graphics g) bằng paintComponent chuẩn Swing để triệt tiêu chớp màn hình
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D gc = (Graphics2D) g;
        renderer.drawBoard(gc, boardState, getWidth(), getHeight(), activePings);
    }

    public void triggerPingAt(int row, int col) {
        double pad = renderer.getPadding();
        double cSize = renderer.getCellSize();
        double x = pad + col * cSize;
        double y = pad + row * cSize;
        activePings.add(new PingEffect(x, y));
    }
}



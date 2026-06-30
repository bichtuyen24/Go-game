package src.board;

import java.awt.*;
import java.awt.MultipleGradientPaint.CycleMethod;
import java.util.List;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.RadialGradientPaint;




public class BoardRenderer {
    private double padding = 35.0;
    private double cellSize;
    private final int BOARD_SIZE = 19;

    public void drawBoard(Graphics2D gc, BoardState boardState, int width, int height, List<PingEffect> activePings) {
        int size = boardState.getSize();
        double availableWidth = width - (padding * 2);
        double availableHeight = height - (padding * 2);
        cellSize = Math.min(availableWidth, availableHeight) / (size - 1);

        double boardWidth = cellSize * (size - 1);
        double boardHeight = cellSize * (size - 1);
        double startX = (width - boardWidth) / 2;
        double startY = (height - boardHeight) / 2;
        this.padding = Math.min(startX, startY);
        // 1. Vẽ nền gỗ bàn cờ
        gc.setColor(new Color(0xDE, 0xB8, 0x87)); // Sử dụng mã màu Hex chuẩn cho gỗ BurlyWood
        gc.fillRect(0, 0, width, height);

        // 2. Vẽ lưới bàn cờ 19x19
        gc.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gc.setColor(Color.BLACK);
        gc.setStroke(new BasicStroke(1.2f)); // Độ đậm nét đường lưới

        for (int i = 0; i < size; i++) {
            double posV = padding + i * cellSize;
            // Đường ngang
            gc.drawLine((int) padding, (int) posV, (int) (padding + boardWidth), (int) posV);
            // Đường dọc
            gc.drawLine((int) posV, (int) padding, (int) posV, (int) (padding + boardHeight));
        }

        // 3. Vẽ 9 điểm Sao (Hoshi) đặc trưng của cờ vây
        int[] starPoints = {3, 9, 15};
        gc.setColor(Color.BLACK);
        for (int r : starPoints) {
            for (int c : starPoints) {
                double x = padding + c * cellSize;
                double y = padding + r * cellSize;
                gc.fillOval((int) (x - 4), (int) (y - 4), 8, 8);
            }
        }

        // 4. Vẽ ma trận quân cờ từ BoardState
        Stone[][] grid = boardState.getGrid();
        double stoneRadius = cellSize * 0.46; // Quân cờ chiếm 92% ô lưới là chuẩn thị giác nhất

        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                Stone stone = grid[r][c];
                if (stone != null) {
                    double x = padding + c * cellSize;
                    double y = padding + r * cellSize;

                    if (stone.getOpacity() < 1.0) {
                        stone.setOpacity(Math.min(1.0, stone.getOpacity() + 0.1));
                    }

                    int alpha = (int) (stone.getOpacity() * 255);

                    // Hiệu ứng bóng đổ phía dưới quân cờ để tạo độ nổi khối tách biệt nền bàn cờ
                    gc.setColor(new Color(0, 0, 0, (int)(alpha * 0.2)));
                    gc.fillOval((int)(x - stoneRadius + 2), (int)(y - stoneRadius + 3), (int)(stoneRadius * 2), (int)(stoneRadius * 2));

                    // Điểm tập trung ánh sáng (Focus) lệch góc trên bên trái tạo khối cầu 3D
                    java.awt.geom.Point2D center = new java.awt.geom.Point2D.Double(x, y);
                    java.awt.geom.Point2D focus = new java.awt.geom.Point2D.Double(x - stoneRadius * 0.25, y - stoneRadius * 0.25);

                    RadialGradientPaint shade;
                    if (stone.getColor() == Color.BLACK) {
                        float[] fractions = {0.0f, 1.0f};
                        Color[] colors = {
                                new Color(0x66, 0x66, 0x66, alpha), // Tâm sáng bóng
                                new Color(0x11, 0x11, 0x11, alpha)  // Rìa đen huyền
                        };
                        shade = new RadialGradientPaint(center, (float) stoneRadius, focus, fractions, colors, CycleMethod.NO_CYCLE);
                    } else {
                        float[] fractions = {0.0f, 0.8f, 1.0f};
                        Color[] colors = {
                                new Color(0xFF, 0xFF, 0xFF, alpha), // Tâm trắng tinh
                                new Color(0xEE, 0xEE, 0xEE, alpha),
                                new Color(0xCC, 0xCC, 0xCC, alpha)  // Khối xám đổ về rìa quân cờ
                        };
                        shade = new RadialGradientPaint(center, (float) stoneRadius, focus, fractions, colors, CycleMethod.NO_CYCLE);
                    }

                    gc.setPaint(shade);
                    gc.fillOval((int)(x - stoneRadius), (int)(y - stoneRadius), (int)(stoneRadius * 2), (int)(stoneRadius * 2));

                    // Vẽ viền mảnh bao quanh để phân tách các quân cờ đứng sát nhau
                    gc.setColor(stone.getColor() == Color.WHITE ? new Color(180,180,180, alpha) : new Color(30,30,30, alpha));
                    gc.setStroke(new BasicStroke(1f));
                    gc.drawOval((int)(x - stoneRadius), (int)(y - stoneRadius), (int)(stoneRadius * 2), (int)(stoneRadius * 2));
                }
            }
        }

// 5. Đánh dấu quân cờ vừa được hạ (Last Placed Stone) bằng vòng tròn đỏ quyến rũ
        Stone lastStone = boardState.getLastPlacedStone();
        if (lastStone != null) {
            double lx = padding + lastStone.getCol() * cellSize;
            double ly = padding + lastStone.getRow() * cellSize;
            gc.setColor(Color.RED);
            gc.setStroke(new BasicStroke(2.0f));
            gc.drawOval((int) lx - 6, (int) ly - 6, 12, 12);
        }

    // 6. Vẽ hiệu ứng radar phát tín hiệu Ping
        for (PingEffect ping : activePings) {
            ping.draw(gc);
        }
    }

    public double getPadding() { return padding; }
    public double getCellSize() { return cellSize; }
}


package board;

import java.awt.*;
import java.awt.MultipleGradientPaint.CycleMethod;
import java.util.List;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.RadialGradientPaint;




public class BoardRenderer {
    private double padding = 30.0;
    private double cellSize;

    public void drawBoard(Graphics2D gc, BoardState boardState, int width, int height, List<PingEffect> activePings) {
        int size = boardState.getSize();
        cellSize = (width - (padding * 2)) / (size - 1);

        // 1. Vẽ nền gỗ bàn cờ
        gc.fill((Shape) Color.getColor("#DEB887"));
        gc.fillRect(0, 0, (int) width, (int) height);

        // 2. Vẽ lưới bàn cờ 19x19
        gc.setStroke((Stroke) Color.BLACK);
        gc.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 1.0);
        for (int i = 0; i < size; i++) {
            double pos = padding + i * cellSize;
            gc.drawLine ((int) padding, (int)  pos, (int)  (width - padding), (int)  pos);
            gc.drawLine((int) pos, (int) padding, (int) pos, (int) (height - padding));
        }


        int[] starPoints = {3, 9, 15};
        gc.fill((Shape) Color.BLACK);
        for (int r : starPoints) {
            for (int c : starPoints) {
                double x = padding + c * cellSize;
                double y = padding + r * cellSize;
                gc.fillOval((int) (x - 4), (int) (y - 4), 8, 8);
            }
        }


        Stone[][] grid = boardState.getGrid();
        double stoneRadius = cellSize * 0.45;

        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                Stone stone = grid[r][c];
                if (stone != null) {
                    double x = padding + c * cellSize;
                    double y = padding + r * cellSize;

                    if (stone.getOpacity() < 1.0) {
                        stone.setOpacity(Math.min(1.0, stone.getOpacity() + 0.1));
                    }

                    // Chuyển opacity từ dạng float (0.0 -> 1.0) sang dạng int (0 -> 255) cho AWT
                    int alpha = (int) (stone.getOpacity() * 255);

                    // Xác định Tâm hình tròn (center) và Điểm tập trung ánh sáng (focus) để tạo hiệu ứng 3D khối cầu
                    java.awt.geom.Point2D center = new java.awt.geom.Point2D.Double(x, y);
                    java.awt.geom.Point2D focus = new java.awt.geom.Point2D.Double(x - stoneRadius * 0.3, y - stoneRadius * 0.3);

                    RadialGradientPaint shade;

                    // KIỂM TRA QUÂN CỜ ĐEN (Lưu ý: Cần đảm bảo stone.getColor() trả về Color của AWT hoặc so sánh bằng logic của bạn)
                    if (stone.getColor() == Color.BLACK) {
                        float[] fractions = {0.0f, 1.0f};
                        Color[] colors = {
                                new Color(0x55, 0x55, 0x55, alpha), // Màu xám sáng ở điểm tụ sáng
                                new Color(0x00, 0x00, 0x00, alpha)  // Màu đen tuyền ở rìa cờ
                        };
                        shade = new RadialGradientPaint(center, (float) stoneRadius, focus, fractions, colors, CycleMethod.NO_CYCLE);
                    }
                    // KIỂM TRA QUÂN CỜ TRẮNG
                    else {
                        float[] fractions = {0.0f, 0.8f, 1.0f};
                        Color[] colors = {
                                new Color(0xFF, 0xFF, 0xFF, alpha), // Màu trắng ở điểm tụ sáng
                                new Color(0xE0, 0xE0, 0xE0, alpha), // Màu xám nhẹ
                                new Color(0xB0, 0xB0, 0xB0, alpha)  // Màu xám đậm hơn ở rìa tạo khối 3D
                        };
                        shade = new RadialGradientPaint(center, (float) stoneRadius, focus, fractions, colors, CycleMethod.NO_CYCLE);
                    }

                    // Nạp hiệu ứng đổ màu vào cọ vẽ
                    gc.setPaint(shade);

                    // Thực hiện tô màu khối cờ tròn
                    gc.fillOval((int)(x - stoneRadius), (int)(y - stoneRadius), (int)(stoneRadius * 2), (int)(stoneRadius * 2));
                }
            }
        }


        Stone lastStone = boardState.getLastPlacedStone();
        if (lastStone != null) {
            double lx = padding + lastStone.getCol() * cellSize;
            double ly = padding + lastStone.getRow() * cellSize;
            gc.setStroke((Stroke) Color.RED);
            gc.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 2.0);
            gc.drawOval((int) lx - 5, (int) ly - 5, 10, 10);
        }


        for (PingEffect ping : activePings) {
            ping.draw(gc);
        }
    }

    public double getPadding() { return padding; }
    public double getCellSize() { return cellSize; }
}

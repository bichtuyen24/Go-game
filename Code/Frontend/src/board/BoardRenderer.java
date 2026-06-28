package board;

import java.awt.*;
import java.util.List;

public class BoardRenderer {
    private double padding = 30.0;
    private double cellSize;

    public void drawBoard(Graphics2D g2, BoardState boardState, double width, double height, List<PingEffect> activePings) {
        int size = boardState.getSize();
        cellSize = (width - (padding * 2)) / (size - 1);

        g2.setColor(new Color(222, 184, 135));
        g2.fillRect(0, 0, (int)width, (int)height);

        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(1f));
        for (int i = 0; i < size; i++) {
            double pos = padding + i * cellSize;
            g2.drawLine((int)padding, (int)pos, (int)(width - padding), (int)pos);
            g2.drawLine((int)pos, (int)padding, (int)pos, (int)(height - padding));
        }

        int[] starPoints = {3, 9, 15};
        g2.setColor(Color.BLACK);
        for (int r : starPoints) {
            for (int c : starPoints) {
                double x = padding + c * cellSize;
                double y = padding + r * cellSize;
                g2.fillOval((int)x - 4, (int)y - 4, 8, 8);
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

                    if (stone.getColor() == Color.BLACK) {
                        g2.setColor(Color.BLACK);
                    } else {
                        g2.setColor(Color.WHITE);
                    }
                    g2.fillOval((int)(x - stoneRadius), (int)(y - stoneRadius), (int)(stoneRadius * 2), (int)(stoneRadius * 2));

                    if (stone.getColor() == Color.WHITE) {
                        g2.setColor(Color.LIGHT_GRAY);
                        g2.drawOval((int)(x - stoneRadius), (int)(y - stoneRadius), (int)(stoneRadius * 2), (int)(stoneRadius * 2));
                    }
                }
            }
        }

        Stone lastStone = boardState.getLastPlacedStone();
        if (lastStone != null) {
            double lx = padding + lastStone.getCol() * cellSize;
            double ly = padding + lastStone.getRow() * cellSize;
            g2.setColor(Color.RED);
            g2.setStroke(new BasicStroke(2));
            g2.drawOval((int)lx - 5, (int)ly - 5, 10, 10);
        }

        for (PingEffect ping : activePings) {
            ping.draw(g2);
        }
    }

    public double getPadding() { return padding; }
    public double getCellSize() { return cellSize; }
}

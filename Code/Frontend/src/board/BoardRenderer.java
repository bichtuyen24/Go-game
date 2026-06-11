package board;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;
import java.util.List;


public class BoardRenderer {
    private double padding = 30.0; 
    private double cellSize;

    public void drawBoard(GraphicsContext gc, BoardState boardState, double width, double height, List<PingEffect> activePings) {
        int size = boardState.getSize();
        cellSize = (width - (padding * 2)) / (size - 1);

        // 1. Vẽ nền gỗ bàn cờ
        gc.setFill(Color.web("#DEB887"));
        gc.fillRect(0, 0, width, height);

        // 2. Vẽ lưới bàn cờ 19x19
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1.0);
        for (int i = 0; i < size; i++) {
            double pos = padding + i * cellSize;
            gc.strokeLine(padding, pos, width - padding, pos); 
            gc.strokeLine(pos, padding, pos, height - padding); 
        }

      
        int[] starPoints = {3, 9, 15};
        gc.setFill(Color.BLACK);
        for (int r : starPoints) {
            for (int c : starPoints) {
                double x = padding + c * cellSize;
                double y = padding + r * cellSize;
                gc.fillOval(x - 4, y - 4, 8, 8);
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

                    RadialGradient shade;
                    if (stone.getColor() == Color.BLACK) {
                        shade = new RadialGradient(0, 0, x - stoneRadius*0.3, y - stoneRadius*0.3, stoneRadius, false, CycleMethod.NO_CYCLE,
                                new Stop(0, Color.web("#555555", stone.getOpacity())),
                                new Stop(1, Color.web("#000000", stone.getOpacity())));
                    } else {
                        shade = new RadialGradient(0, 0, x - stoneRadius*0.3, y - stoneRadius*0.3, stoneRadius, false, CycleMethod.NO_CYCLE,
                                new Stop(0, Color.web("#FFFFFF", stone.getOpacity())),
                                new Stop(0.8, Color.web("#E0E0E0", stone.getOpacity())),
                                new Stop(1, Color.web("#B0B0B0", stone.getOpacity())));
                    }
                    gc.setFill(shade);
                    gc.fillOval(x - stoneRadius, y - stoneRadius, stoneRadius * 2, stoneRadius * 2);
                }
            }
        }

        
        Stone lastStone = boardState.getLastPlacedStone();
        if (lastStone != null) {
            double lx = padding + lastStone.getCol() * cellSize;
            double ly = padding + lastStone.getRow() * cellSize;
            gc.setStroke(Color.RED);
            gc.setLineWidth(2);
            gc.strokeOval(lx - 5, ly - 5, 10, 10);
        }

        
        for (PingEffect ping : activePings) {
            ping.draw(gc);
        }
    }

    public double getPadding() { return padding; }
    public double getCellSize() { return cellSize; }
}

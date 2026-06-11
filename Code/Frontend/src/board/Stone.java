package board;

import javafx.scene.paint.Color;

public class Stone {
    private final int row;
    private final int col;
    private final Color color; 
    private double opacity = 0.0; 

    public Stone(int row, int col, Color color) {
        this.row = row;
        this.col = col;
        this.color = color;
    }

    public int getRow() { return row; }
    public int getCol() { return col; }
    public Color getColor() { return color; }
    public double getOpacity() { return opacity; }
    public void setOpacity(double opacity) { this.opacity = opacity; }
}

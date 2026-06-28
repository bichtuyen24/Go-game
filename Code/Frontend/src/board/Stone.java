package board;

import java.awt.Color;

public class Stone {
    private final int row;
    private final int col;
    private final Color color;

    public Stone(int row, int col, Color color) {
        this.row = row;
        this.col = col;
        this.color = color;
    }

    public int getRow() { return row; }
    public int getCol() { return col; }
    public Color getColor() { return color; }
}
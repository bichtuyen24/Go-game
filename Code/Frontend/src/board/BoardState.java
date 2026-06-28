package board;

import java.awt.Color;

public class BoardState {
    private final int size = 19;
    private final Stone[][] grid = new Stone[size][size];
    private Stone lastPlacedStone = null;

    public boolean placeStone(int row, int col, Color color) {
        if (row < 0 || row >= size || col < 0 || col >= size) return false;
        if (grid[row][col] != null) return false;

        Stone newStone = new Stone(row, col, color);
        grid[row][col] = newStone;
        lastPlacedStone = newStone;
        return true;
    }

    public Stone[][] getGrid() { return grid; }
    public Stone getLastPlacedStone() { return lastPlacedStone; }
    public int getSize() { return size; }

    public Color getStoneColor(int r, int c) {

        return null;
    }
}
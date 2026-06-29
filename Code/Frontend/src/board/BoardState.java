package board;
import java.awt.Color;


public class BoardState {
    private final int size = 19;
    private final Stone[][] grid = new Stone[size][size];
    private Stone lastPlacedStone = null;

    public boolean placeStone(int row, int col, Color color) {
        if (row < 0 || row >= size || col < 0 || col >= size) return false;
        if (grid[row][col] != null) return false; // Ô đã có quân cờ

        Stone newStone = new Stone(row, col, color);
        grid[row][col] = newStone;
        lastPlacedStone = newStone;
        return true;
    }

    public Stone[][] getGrid() { return grid; }
    public Stone getLastPlacedStone() { return lastPlacedStone; }
    public int getSize() { return size; }

    // Test
    private int passCount = 0;
    private boolean gameOver = false;

    private int blackScore = 0;
    private int whiteScore = 0;

    /* dùng cho luật Ko */
    private String lastBoardState = "";

    public void pass() {

        passCount++;

        if(passCount >= 2){
            gameOver = true;
            calculateScore();
        }

    }
    public boolean isGameOver() {
        return gameOver;
    }
    public String getWinner(){

        if(blackScore > whiteScore){
            return "BLACK";
        }

        if(whiteScore > blackScore){
            return "WHITE";
        }

        return "DRAW";
    }
    private void calculateScore(){

        blackScore = 0;
        whiteScore = 0;

        for(int r=0;r<size;r++){

            for(int c=0;c<size;c++){

                if(grid[r][c]==null)
                    continue;

                if(grid[r][c].getColor()==Color.BLACK)
                    blackScore++;

                else
                    whiteScore++;

            }

        }

    }
    private boolean hasLiberty(int row,int col,boolean[][] visited){

        if(row<0 || row>=size)
            return false;

        if(col<0 || col>=size)
            return false;

        if(visited[row][col])
            return false;

        Stone stone = grid[row][col];

        if(stone==null)
            return true;

        visited[row][col]=true;

        Color color = stone.getColor();

        if(row>0){

            Stone s = grid[row-1][col];

            if(s==null)
                return true;

            if(s.getColor()==color){

                if(hasLiberty(row-1,col,visited))
                    return true;

            }

        }

        if(row<size-1){

            Stone s = grid[row+1][col];

            if(s==null)
                return true;

            if(s.getColor()==color){

                if(hasLiberty(row+1,col,visited))
                    return true;

            }

        }

        if(col>0){

            Stone s = grid[row][col-1];

            if(s==null)
                return true;

            if(s.getColor()==color){

                if(hasLiberty(row,col-1,visited))
                    return true;

            }

        }

        if(col<size-1){

            Stone s = grid[row][col+1];

            if(s==null)
                return true;

            if(s.getColor()==color){

                if(hasLiberty(row,col+1,visited))
                    return true;

            }

        }

        return false;

    }

    private void removeGroup(int row,int col,Color color){

        if(row<0 || row>=size)
            return;

        if(col<0 || col>=size)
            return;

        Stone stone = grid[row][col];

        if(stone==null)
            return;

        if(stone.getColor()!=color)
            return;

        grid[row][col]=null;

        removeGroup(row-1,col,color);
        removeGroup(row+1,col,color);
        removeGroup(row,col-1,color);
        removeGroup(row,col+1,color);

    }

    private void captureOpponent(int row,int col,Color myColor){

        Color enemy =
                myColor==Color.BLACK ?
                        Color.WHITE :
                        Color.BLACK;

        int[][] dir={
                {-1,0},
                {1,0},
                {0,-1},
                {0,1}
        };

        for(int[] d:dir){

            int nr=row+d[0];
            int nc=col+d[1];

            if(nr<0||nr>=size)
                continue;

            if(nc<0||nc>=size)
                continue;

            Stone s = grid[nr][nc];

            if(s==null)
                continue;

            if(s.getColor()!=enemy)
                continue;

            boolean liberty =
                    hasLiberty(nr,nc,new boolean[size][size]);

            if(!liberty){

                removeGroup(nr,nc,enemy);

            }

        }

    }
    private String boardHash(){

        StringBuilder sb = new StringBuilder();

        for(int r=0;r<size;r++){

            for(int c=0;c<size;c++){

                if(grid[r][c]==null)
                    sb.append(".");

                else if(grid[r][c].getColor()==Color.BLACK)
                    sb.append("B");

                else
                    sb.append("W");

            }

        }

        return sb.toString();

    }

}


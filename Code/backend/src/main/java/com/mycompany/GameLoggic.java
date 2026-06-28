package com.mycompany;

import java.util.*;

public class GameLoggic {

    private static final RoomManager roomManager = new RoomManager();
    public static RoomManager getRoomManagerInstance() { return roomManager; }

    private int[][] board = new int[19][19];

    public static boolean checkMove(String moveData, Room room) {
        try {
            if (moveData == null || !moveData.contains(",")) return false;

            String[] parts = moveData.split(",");
            int row = Integer.parseInt(parts[0].trim());
            int col = Integer.parseInt(parts[1].trim());

            if (row < 0 || row >= 19 || col < 0 || col >= 19) return false;

            System.out.println("[Trong Tai] Nuoc di hop le tai toa do: (" + row + "," + col + ")");
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    public int calculateLiberties(int startRow, int startCol, int playerColor, int[][] currentBoard) {
        boolean[][] visited = new boolean[19][19];
        Queue<int[]> queue = new LinkedList<>();
        Set<String> liberties = new HashSet<>();

        queue.add(new int[]{startRow, startCol});
        visited[startRow][startCol] = true;

        int[] dRow = {-1, 1, 0, 0};
        int[] dCol = {0, 0, -1, 1};

        while (!queue.isEmpty()) {
            int[] curr = queue.poll();
            int r = curr[0];
            int c = curr[1];

            for (int i = 0; i < 4; i++) {
                int nextR = r + dRow[i];
                int nextC = c + dCol[i];

                if (nextR >= 0 && nextR < 19 && nextC >= 0 && nextC < 19) {
                    if (currentBoard[nextR][nextC] == 0) {
                        liberties.add(nextR + "," + nextC);
                    } else if (currentBoard[nextR][nextC] == playerColor && !visited[nextR][nextC]) {
                        visited[nextR][nextC] = true;
                        queue.add(new int[]{nextR, nextC});
                    }
                }
            }
        }
        return liberties.size();
    }
}
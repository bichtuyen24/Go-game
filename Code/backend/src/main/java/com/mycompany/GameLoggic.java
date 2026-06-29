package com.mycompany;

public class GameLoggic {
    public static boolean checkMove(String moveData, Room room) {
        try {
            if (moveData == null || !moveData.contains(",")) return false;

            String[] parts = moveData.split(",");
            int row = Integer.parseInt(parts[0].trim());
            int col = Integer.parseInt(parts[1].trim());

            if (row < 0 || row >= 19 || col < 0 || col >= 19) {
                return false;
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
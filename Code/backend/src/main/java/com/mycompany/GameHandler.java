package com.mycompany;

public class GameHandler {
    public static void handleGameAction(Packet packet, ClientHandler sender) {
        Room room = SocketServer.roomManager.getRoom(packet.getRoomId());
        if (room == null) return;

        String type = packet.getType();

        if (type.equals(Protocol.MOVE)) {
            // 1. Chống cheat lượt đi (Nếu phòng đủ 2 người mới kích hoạt chặn chặt chẽ)
            if (room.getPlayers().size() == 2 && !room.getCurrentTurnPlayer().equals(sender.getUsername())) {
                sender.sendPacket(new Packet(Protocol.GAME_ERROR, "SERVER", room.getRoomId(), "Chưa tới lượt của bạn!"));
                return;
            }

            // 2. Kiểm tra nước đi nằm trong lưới cờ vây 19x19 (Chống cheat sửa client)
            boolean isValidMove = GameLoggic.checkMove(packet.getData(), room);
            if (isValidMove) {
                String playerColor = room.getPlayerColor(sender.getUsername());
                
                // Chuẩn hóa định dạng trả về cho FE: "row,col,color"
                String broadcastData = packet.getData() + "," + playerColor;
                Packet movePacket = new Packet(Protocol.MOVE, sender.getUsername(), room.getRoomId(), broadcastData);
                
                room.switchTurn();          // Đổi lượt trên Server
                room.broadcast(movePacket); // Đồng bộ nước đi cho toàn phòng vẽ lên Canvas
            } else {
                sender.sendPacket(new Packet(Protocol.GAME_ERROR, "SERVER", room.getRoomId(), "Vị trí không hợp lệ!"));
            }
        } 
        else if (type.equals(Protocol.PASS) || type.equals(Protocol.RESIGN)) {
            room.switchTurn();
            room.broadcast(packet);
        }
        // Xử lý Signaling WebRTC kết nối Peer-to-Peer
        else if (type.equals(Protocol.RTC_OFFER) || type.equals(Protocol.RTC_ANSWER) || type.equals(Protocol.RTC_ICE)) {
            room.sendToOpponent(packet, sender.getUsername());
        }
    }
}
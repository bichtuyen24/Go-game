package com.mycompany;

public class GameHandler {
    public static void handleGameAction(Packet packet, ClientHandler sender) {
        Room room = SocketServer.roomManager.getRoom(packet.getRoomId());
        if (room == null) return;

        String type = packet.getType();

        if (type.equals(Protocol.MOVE)) {
            if (room.getPlayers().size() == 2 && !room.getCurrentTurnPlayer().equals(sender.getUsername())) {
                sender.sendPacket(new Packet(Protocol.GAME_ERROR, "SERVER", room.getRoomId(), "Chưa tới lượt của bạn!"));
                return;
            }

            boolean isValidMove = GameLoggic.checkMove(packet.getData(), room);
            if (isValidMove) {
                String playerColor = room.getPlayerColor(sender.getUsername());

                String broadcastData = packet.getData() + "," + playerColor;
                Packet movePacket = new Packet(Protocol.MOVE, sender.getUsername(), room.getRoomId(), broadcastData);

                room.switchTurn();          
                room.broadcast(movePacket); 
            } else {
                sender.sendPacket(new Packet(Protocol.GAME_ERROR, "SERVER", room.getRoomId(), "Vị trí không hợp lệ!"));
            }
        }
        else if (type.equals(Protocol.PASS) || type.equals(Protocol.RESIGN)) {
            room.switchTurn();
            room.broadcast(packet);
        }
        else if (type.equals(Protocol.RTC_OFFER) || type.equals(Protocol.RTC_ANSWER) || type.equals(Protocol.RTC_ICE)) {
            room.sendToOpponent(packet, sender.getUsername());
        }
    }
}
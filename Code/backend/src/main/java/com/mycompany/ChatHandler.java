package com.mycompany;

public class ChatHandler {
    public static void handleChat(Packet packet, ClientHandler sender) {
        Room room = SocketServer.roomManager.getRoom(packet.getRoomId());
        if (room != null) {
            room.broadcast(packet);
        }
    }
}
package com.mycompany;

public class LobbyHandler {
    public static void handleLobby(Packet packet, ClientHandler sender) {
        String roomId = packet.getRoomId();
        
        if (packet.getType().equals(Protocol.CREATE_ROOM)) {
            Room newRoom = SocketServer.roomManager.createRoom(roomId, sender);
            if (newRoom != null) {
                sender.setCurrentRoomId(roomId);
                sender.sendPacket(new Packet(Protocol.CREATE_ROOM, "SERVER", roomId, "SUCCESS"));
            } else {
                sender.sendPacket(new Packet(Protocol.ROOM_ERROR, "SERVER", roomId, "Phòng đã tồn tại!"));
            }
        } 
        else if (packet.getType().equals(Protocol.JOIN_ROOM)) {
            Room room = SocketServer.roomManager.joinRoom(roomId, sender);
            if (room != null) {
                sender.setCurrentRoomId(roomId);
                sender.sendPacket(new Packet(Protocol.JOIN_ROOM, "SERVER", roomId, "SUCCESS"));
                room.broadcast(new Packet(Protocol.JOIN_ROOM, sender.getUsername(), roomId, "READY"));
            } else {
                sender.sendPacket(new Packet(Protocol.ROOM_ERROR, "SERVER", roomId, "Phòng đầy hoặc không hợp lệ!"));
            }
        } 
        else if (packet.getType().equals(Protocol.LEAVE_ROOM)) {
            SocketServer.roomManager.leaveRoom(roomId, sender);
            sender.setCurrentRoomId(null);
            sender.sendPacket(new Packet(Protocol.LEAVE_ROOM, "SERVER", roomId, "SUCCESS"));
        }
    }
}
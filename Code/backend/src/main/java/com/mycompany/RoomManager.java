package com.mycompany;
import java.util.concurrent.ConcurrentHashMap;

public class RoomManager {
    private ConcurrentHashMap<String, Room> rooms = new ConcurrentHashMap<>();

    public synchronized Room createRoom(String roomId, ClientHandler host) {
        if (rooms.containsKey(roomId)) return rooms.get(roomId);
        Room room = new Room(roomId, host);
        rooms.put(roomId, room);
        return room;
    }

    public synchronized Room joinRoom(String roomId, ClientHandler player) {
        Room room = rooms.get(roomId);
        if (room != null && room.addPlayer(player)) {
            return room;
        }
        return null;
    }

    public synchronized void leaveRoom(String roomId, ClientHandler player) {
        Room room = rooms.get(roomId);
        if (room != null) {
            room.removePlayer(player);
            if (room.getPlayers().isEmpty() && !roomId.equals("01")) rooms.remove(roomId);
        }
    }

    public Room getRoom(String roomId) { return rooms.get(roomId); }
}
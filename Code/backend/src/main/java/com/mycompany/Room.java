package com.mycompany;

import java.util.concurrent.CopyOnWriteArrayList;
public class Room {
    private String roomId;
    private CopyOnWriteArrayList<ClientHandler> players = new CopyOnWriteArrayList<>();
    private String currentTurnPlayer = "";
    private String blackPlayer = "";
    private String whitePlayer = "";

    public Room(String roomId, ClientHandler host) {
        this.roomId = roomId;
        if (host != null) {
            this.players.add(host);
            this.blackPlayer = host.getUsername();
            this.currentTurnPlayer = host.getUsername();
        }
    }

    public synchronized boolean addPlayer(ClientHandler player) {
        // Nếu player đã tồn tại trong danh sách thì bỏ qua
        for (ClientHandler p : players) {
            if (p.getUsername().equals(player.getUsername())) return true;
        }
        if (players.size() >= 2) return false;

        players.add(player);
        if (blackPlayer.isEmpty()) {
            blackPlayer = player.getUsername();
            currentTurnPlayer = player.getUsername();
        } else {
            whitePlayer = player.getUsername();
        }
        return true;
    }

    public synchronized void removePlayer(ClientHandler player) {
        players.remove(player);
    }

    public void broadcast(Packet packet) {
        for (ClientHandler p : players) {
            p.sendPacket(packet);
        }
    }

    public void sendToOpponent(Packet packet, String senderUsername) {
        for (ClientHandler p : players) {
            if (!p.getUsername().equals(senderUsername)) {
                p.sendPacket(packet);
            }
        }
    }

    public void switchTurn() {
        if (players.size() == 2) {
            currentTurnPlayer = currentTurnPlayer.equals(blackPlayer) ? whitePlayer : blackPlayer;
        }
    }

    public String getPlayerColor(String username) {
        if (username.equals(whitePlayer)) return "WHITE";
        return "BLACK"; // Mặc định hoặc người đi trước cầm quân đen
    }

    public String getRoomId() { return roomId; }
    public CopyOnWriteArrayList<ClientHandler> getPlayers() { return players; }
    public String getCurrentTurnPlayer() { return currentTurnPlayer; }
}
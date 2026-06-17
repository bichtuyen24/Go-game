package com.mycompany;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String username = "Anonymous";
    private String currentRoomId = null;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        try {
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.err.println("Lỗi I/O luồng Client: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                Packet packet = (Packet) in.readObject();
                if (packet == null) break;

                switch (packet.getType()) {
                    case Protocol.LOGIN:
                        this.username = packet.getSender();
                        this.currentRoomId = packet.getRoomId();
                        // Tự động đưa người chơi vào phòng mặc định hệ thống vừa tạo
                        Room defaultRoom = SocketServer.roomManager.getRoom(currentRoomId);
                        if (defaultRoom != null) {
                            defaultRoom.addPlayer(this);
                        }
                        System.out.println("User [" + username + "] đã tham gia phòng [" + currentRoomId + "].");
                        break;
                        
                    case Protocol.PING:
                        sendPacket(new Packet(Protocol.PING, "SERVER", null, "PONG"));
                        break;

                    case Protocol.CREATE_ROOM:
                    case Protocol.JOIN_ROOM:
                    case Protocol.LEAVE_ROOM:
                        LobbyHandler.handleLobby(packet, this);
                        break;

                    case Protocol.CHAT:
                        ChatHandler.handleChat(packet, this);
                        break;

                    case Protocol.MOVE:
                    case Protocol.PASS:
                    case Protocol.RESIGN:
                    case Protocol.RTC_OFFER:
                    case Protocol.RTC_ANSWER:
                    case Protocol.RTC_ICE:
                        GameHandler.handleGameAction(packet, this);
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println("Kết nối của [" + username + "] bị đóng.");
        } finally {
            close();
        }
    }

    public void sendPacket(Packet packet) {
        try {
            synchronized (out) {
                out.writeObject(packet);
                out.flush();
            }
        } catch (IOException e) {
            System.err.println("Lỗi gửi gói tin: " + e.getMessage());
        }
    }

    private void close() {
        try {
            SocketServer.onlineClients.remove(this);
            if (currentRoomId != null) {
                SocketServer.roomManager.leaveRoom(currentRoomId, this);
            }
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUsername() { return username; }
    public String getCurrentRoomId() { return currentRoomId; }
    public void setCurrentRoomId(String roomId) { this.currentRoomId = roomId; }
}
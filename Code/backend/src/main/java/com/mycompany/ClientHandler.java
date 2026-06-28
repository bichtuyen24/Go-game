package com.mycompany;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import com.google.gson.Gson;

public class ClientHandler implements Runnable {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String username = "Anonymous";
    private String currentRoomId = null;
    private final Gson gson = new Gson();

    public ClientHandler(Socket socket) {
        this.socket = socket;
        try {
            this.out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
        } catch (IOException e) {
            System.err.println("Loi I/O luong Client: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                Packet packet = gson.fromJson(message, Packet.class);
                if (packet == null) continue;

                if ("MOVE".equals(packet.getType())) {
                    Room room = GameLoggic.getRoomManagerInstance().getRoom(packet.getRoomId());
                    if (room != null && !room.getCurrentTurnPlayer().equals(packet.getSender())) {
                        System.out.println("[Anti-Cheat] Chan nuoc di sai luot cua: " + packet.getSender());
                        continue;
                    }
                }

                switch (packet.getType()) {
                    case "LOGIN":
                        String loginUser = packet.getSender();
                        String loginPassword = packet.getData();

                        boolean isValid = DatabaseManager.getInstance().checkLogin(loginUser, loginPassword);

                        if (isValid) {
                            this.username = loginUser;
                            this.currentRoomId = packet.getRoomId();
                            System.out.println("User [" + username + "] dang nhap THANH CONG vao phong [" + currentRoomId + "].");

                            sendPacket(new Packet("LOGIN_RESPONSE", "SERVER", currentRoomId, "SUCCESS"));
                        } else {
                            System.out.println("User [" + loginUser + "] dang nhap THAT BAI (Sai tai khoan/mat khau).");

                            sendPacket(new Packet("LOGIN_RESPONSE", "SERVER", null, "FAILED_AUTH"));
                        }
                        break;

                    case "PING":
                        sendPacket(new Packet("PONG", "SERVER", packet.getRoomId(), ""));
                        break;

                    case "MOVE":
                        Room targetRoom = GameLoggic.getRoomManagerInstance().getRoom(packet.getRoomId());
                        if (targetRoom != null && GameLoggic.checkMove(packet.getData(), targetRoom)) {
                            targetRoom.sendToOpponent(packet, this.username);
                            targetRoom.switchTurn();
                        }
                        break;

                    default:
                        Room r = GameLoggic.getRoomManagerInstance().getRoom(packet.getRoomId());
                        if (r != null) {
                            r.sendToOpponent(packet, this.username);
                        }
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println("Ket noi cua [" + username + "] bi dong.");
        } finally {
            close();
        }
    }

    public void sendPacket(Packet packet) {
        try {
            synchronized (out) {
                String jsonStr = gson.toJson(packet);
                out.println(jsonStr);
            }
        } catch (Exception e) {
            System.err.println("Loi gui goi tin: " + e.getMessage());
        }
    }

    private void close() {
        try {
            if (currentRoomId != null) {
                GameLoggic.getRoomManagerInstance().leaveRoom(currentRoomId, this);
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
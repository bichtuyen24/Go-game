package com.mycompany;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

public class SocketServer {
    private static final int PORT = 8081;
    public static CopyOnWriteArrayList<ClientHandler> onlineClients = new CopyOnWriteArrayList<>();
    public static RoomManager roomManager = new RoomManager();

    public static void main(String[] args) {
        System.out.println("=== GO GAME SERVER RUNNING ON PORT " + PORT + " ===");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            // Khởi tạo phòng mặc định "01" khớp với phòng chờ ngầm của FE hiện tại
            roomManager.createRoom("01", null);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Kết nối mới đến từ: " + socket.getRemoteSocketAddress());
                
                ClientHandler handler = new ClientHandler(socket);
                onlineClients.add(handler);
                new Thread(handler).start();
            }
        } catch (IOException e) {
            System.err.println("Lỗi Server: " + e.getMessage());
        }
    }
}
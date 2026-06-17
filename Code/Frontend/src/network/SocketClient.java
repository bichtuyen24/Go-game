package network;

import java.io.*;
import java.net.Socket;
import board.BoardCanvas;
import javafx.application.Platform;
import com.mycompany.Packet;
import com.google.gson.JsonObject;

public class SocketClient {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private boolean isConnected = false;

    
    public void connectAsync(BoardCanvas canvas) {
        new Thread(() -> {
            try {
                System.out.println("[Client] Đang kết nối tới Server...");
                socket = new Socket("localhost", 9999);
                
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                isConnected = true;
                System.out.println("[Client] Kết nối Server thành công!");

               
                listenFromServer(canvas);

            } catch (Exception e) {
                System.err.println("[Lỗi] Không thể kết nối Server (Có thể bạn chưa bật Server). Ứng dụng vẫn chạy ở chế độ Offline để test UI.");
            }
        }).start();
    }

    
    private void listenFromServer(BoardCanvas canvas) {
        try {
            String message;
            while (isConnected && (message = in.readLine()) != null) {
                final String msg = message;
                
                
                Platform.runLater(() -> {
                    processServerMessage(msg, canvas);
                });
            }
        } catch (IOException e) {
            System.err.println("[Mạng] Mất kết nối với Server.");
            isConnected = false;
        }
    }

    
    private void processServerMessage(String message, BoardCanvas canvas) {
        try {
            if (message.startsWith("OPPONENT_MOVE:")) {
                
                String data = message.substring("OPPONENT_MOVE:".length());
                String[] tokens = data.split(",");
                int row = Integer.parseInt(tokens[0]);
                int col = Integer.parseInt(tokens[1]);
                canvas.handleServerMove(row, col, "WHITE"); 
            } 
            else if (message.startsWith("OPPONENT_PING:")) {
                
                String data = message.substring("OPPONENT_PING:".length());
                String[] tokens = data.split(",");
                int row = Integer.parseInt(tokens[0]);
                int col = Integer.parseInt(tokens[1]);
                canvas.triggerPingAt(row, col); 
            }
            else if (message.contains("|")) { 
                // Giải mã chuỗi dạng "ACTION|{JSON}" nhận từ server thành đối tượng Packet
                com.mycompany.Packet packet = com.mycompany.Packet.fromString(message);
                if (packet != null) {
                    String action = packet.getAction();
                    com.google.gson.JsonObject payload = packet.getPayload();

                    if ("MOVE".equals(action)) {
                        int row = payload.get("row").getAsInt();
                        int col = payload.get("col").getAsInt();
                        // Lấy màu từ server truyền về hoặc mặc định là WHITE nếu thiếu
                        String colorStr = payload.has("color") ? payload.get("color").getAsString() : "WHITE";
                        canvas.handleServerMove(row, col, colorStr); 
                    } 
                    else if ("PING".equals(action)) {
                        int row = payload.get("row").getAsInt();
                        int col = payload.get("col").getAsInt();
                        canvas.triggerPingAt(row, col); 
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("[Lỗi] Gói tin từ Server không hợp lệ: " + message);
        }
    }

    public void sendMove(int row, int col) {
        if (isConnected && out != null) {
            out.println("MOVE:" + row + "," + col);
        }
    }

    public void sendPing(int row, int col) {
        if (isConnected && out != null) {
            out.println("PING:" + row + "," + col);
        }
    }
    public void sendPacketMove(int row, int col, String roomId) {
        if (isConnected && out != null) {
            com.google.gson.JsonObject data = new com.google.gson.JsonObject();
            data.addProperty("row", row);
            data.addProperty("col", col);
            data.addProperty("roomId", roomId);
            
            // Sử dụng hàm Packet.of gốc của nhóm bạn
            com.mycompany.Packet p = com.mycompany.Packet.of("MOVE", data);
            out.println(p.toString()); // Gửi chuỗi "MOVE|{...}" lên Server của bạn
        }
    }

    public void sendPacketPing(int row, int col) {
        if (isConnected && out != null) {
            com.google.gson.JsonObject data = new com.google.gson.JsonObject();
            data.addProperty("row", row);
            data.addProperty("col", col);
            
            com.mycompany.Packet p = com.mycompany.Packet.of("PING", data);
            out.println(p.toString());
        }
    }
}

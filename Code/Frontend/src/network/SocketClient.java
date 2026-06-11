package network;

import java.io.*;
import java.net.Socket;
import board.BoardCanvas;
import javafx.application.Platform;

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
}

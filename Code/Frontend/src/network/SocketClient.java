package src.network;
import com.google.gson.JsonObject;
import src.board.BoardCanvas;
import backend.src.com.company.Packets;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.function.Consumer;

/**
 * CLIENT TCP DUY NHẤT của ứng dụng.
 * - Cổng chính thức: 9000 (khớp với Server.java đã gộp)
 * - Giao thức: TEXT STREAM "ACTION|{json}\n" (BufferedReader/PrintWriter, KHÔNG dùng ObjectStream)
 * - Singleton: Login -> Lobby -> Game dùng CHUNG 1 kết nối socket (không reconnect liên tục)
 */
public class SocketClient {

    private static SocketClient instance;

    public static synchronized SocketClient getInstance() {
        if (instance == null) instance = new SocketClient();
        return instance;
    }

    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 9000;

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private volatile boolean isConnected = false;

    private String username = "Anonymous";
    private BoardCanvas canvas;                 // bàn cờ hiện tại, set khi vào game
    private Consumer<Packets> packetListener;    // callback của màn hình đang mở (Login/Lobby/Game)

    public void setUsername(String username) { this.username = username; }
    public String getUsername() { return username; }
    public boolean isConnected() { return isConnected; }
    public void setSocketCanvas(BoardCanvas canvas) { this.canvas = canvas; }

    /** Đăng ký callback để nhận MỌI gói tin thô từ server (mỗi màn hình tự lọc theo getAction()). */
    public void setPacketListener(Consumer<Packets> listener) { this.packetListener = listener; }

    /** Kết nối bất đồng bộ tới Server. onConnected/onFailed chạy trên EDT (an toàn cho Swing). */
    public void connectAsync(Runnable onConnected, Runnable onFailed) {
        if (isConnected) {
            if (onConnected != null) EventQueue.invokeLater(onConnected);
            return;
        }
        new Thread(() -> {
            try {
                System.out.println("[Client] Đang kết nối tới Server " + SERVER_IP + ":" + SERVER_PORT + " ...");
                socket = new Socket(SERVER_IP, SERVER_PORT);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
                isConnected = true;
                System.out.println("[Client] Kết nối Server thành công!");

                if (onConnected != null) EventQueue.invokeLater(onConnected);
                listenFromServer();
            } catch (Exception e) {
                System.err.println("[Lỗi] Không thể kết nối Server: " + e.getMessage());
                isConnected = false;
                if (onFailed != null) EventQueue.invokeLater(onFailed);
            }
        }, "socket-client-thread").start();
    }

    /** Giữ chữ ký cũ (connectAsync(BoardCanvas)) để KHÔNG phá vỡ code GameScene.java đang gọi nó. */
    public void connectAsync(BoardCanvas canvas) {
        setSocketCanvas(canvas);
        connectAsync(null, null);
    }

    private void listenFromServer() {
        try {
            String line;
            while (isConnected && (line = in.readLine()) != null) {
                final String raw = line;
                EventQueue.invokeLater(() -> processServerMessage(raw));
            }
        } catch (IOException e) {
            System.err.println("[Mạng] Mất kết nối với Server.");
        } finally {
            isConnected = false;
        }
    }

    private void processServerMessage(String message) {
        Packets packet;
        try {
            packet = Packets.decode(message);
        } catch (Exception e) {
            System.err.println("[Lỗi] Gói tin từ Server không hợp lệ: " + message);
            return;
        }

        // Xử lý sẵn các sự kiện liên quan trực tiếp tới bàn cờ (nếu đang ở màn hình Game)
        try {
            String action = packet.getAction();
            if ("MOVE".equals(action) && canvas != null) {
                // GameHandler trả về data="row,col,COLOR,capturedCount"
                String raw = packet.getData();
                if (raw != null) {
                    String[] tokens = raw.split(",");
                    if (tokens.length >= 3) {
                        int row = Integer.parseInt(tokens[0].trim());
                        int col = Integer.parseInt(tokens[1].trim());
                        String color = tokens[2].trim();
                        canvas.handleServerMove(row, col, color);
                    }
                }
            } else if ("PING".equals(action) && canvas != null) {
                JsonObject payload = packet.getPayload();
                if (payload.has("row") && payload.has("col")) {
                    canvas.triggerPingAt(payload.get("row").getAsInt(), payload.get("col").getAsInt());
                }
            }
        } catch (Exception e) {
            System.err.println("[Lỗi] Không xử lý được payload bàn cờ: " + message);
        }

        // Forward gói tin thô cho màn hình đang mở (Login/Register/Lobby/Game) tự xử lý UI riêng
        if (packetListener != null) {
            packetListener.accept(packet);
        }
    }

    // ===== Các hàm gửi gói tin lên Server =====

    public void send(Packets packet) {
        if (isConnected && out != null) {
            out.println(packet.encode());
        } else {
            System.err.println("[Client] Chưa kết nối Server, không thể gửi: " + packet.getAction());
        }
    }

    public void sendLogin(String u, String p) { send(Packets.login(u, p)); }
    public void sendRegister(String u, String email, String p) { send(Packets.register(u, email, p)); }

    public void sendCreateRoom(String roomId) { send(new Packets(Packets.CREATE_ROOM, username, roomId, "")); }
    public void sendJoinRoom(String roomId) { send(new Packets(Packets.JOIN_ROOM, username, roomId, "")); }
    public void sendLeaveRoom(String roomId) { send(new Packets(Packets.LEAVE_ROOM, username, roomId, "")); }

    public void sendChat(String roomId, String message) { send(new Packets(Packets.CHAT, username, roomId, message)); }

    /** Giữ tương thích chữ ký cũ - mặc định gửi vào phòng "01". */
    public void sendMove(int row, int col) { sendMove(row, col, "01"); }
    public void sendMove(int row, int col, String roomId) {
        send(new Packets(Packets.MOVE, username, roomId, row + "," + col));
    }

    public void sendPass(String roomId) { send(new Packets(Packets.PASS, username, roomId, "")); }
    public void sendResign(String roomId) { send(new Packets(Packets.RESIGN, username, roomId, "")); }

    /** Giữ tương thích chữ ký cũ (sendPing không roomId) - dùng cho hiệu ứng Ping trên canvas. */
    public void sendPing(int row, int col) {
        send(Packets.of(Packets.PING).put("row", row).put("col", col));
    }

    public void disconnect() {
        isConnected = false;
        try {
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException ignored) {}
    }
}

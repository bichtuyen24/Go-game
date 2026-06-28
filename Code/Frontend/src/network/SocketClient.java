package network;

import java.io.*;
import java.net.Socket;
import javax.swing.SwingUtilities;
import javax.swing.JLabel;
import javax.swing.Timer;
import board.BoardCanvas;
import com.mycompany.Packet;
import com.google.gson.Gson;
import ui.GameRoom;

public class SocketClient {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private boolean isConnected = false;
    private final Gson gson = new Gson();

    private long pingStartTime;
    private JLabel pingValueLabel;
    private String currentRoomId = "default_room";
    private Timer pingTimer;

    public void connectAsync(BoardCanvas canvas) {
        new Thread(() -> {
            try {
                System.out.println("[Client] Đang kết nối tới Server...");
                socket = new Socket("localhost", 9000);

                in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
                isConnected = true;
                System.out.println("[Client] Kết nối Server thành công!");

                listenFromServer(canvas);

            } catch (Exception e) {
                System.err.println("[Lỗi] Không thể kết nối Server. Ứng dụng chạy ở chế độ Offline.");
            }
        }).start();
    }

    public Timer startNetworkPingTicker(JLabel pingLabel, String roomId) {
        this.pingValueLabel = pingLabel;
        this.currentRoomId = roomId;

        if (pingTimer != null && pingTimer.isRunning()) {
            pingTimer.stop();
        }

        pingTimer = new Timer(2000, e -> {
            if (isConnected && out != null) {
                pingStartTime = System.currentTimeMillis();
                Packet p = new Packet("PING", "Player", currentRoomId, "");
                out.println(gson.toJson(p));
            }
        });
        pingTimer.start();
        return pingTimer;
    }

    private void listenFromServer(BoardCanvas canvas) {
        try {
            String message;
            while (isConnected && (message = in.readLine()) != null) {
                final String msg = message;

                SwingUtilities.invokeLater(() -> {
                    processServerMessage(msg, canvas);
                });
            }
        } catch (IOException e) {
            System.err.println("[Mạng] Mất kết nối với Server.");
            isConnected = false;
            if (pingTimer != null) pingTimer.stop();
        }
    }

    private void processServerMessage(String message, BoardCanvas canvas) {
        try {
            Packet packet = gson.fromJson(message, Packet.class);

            if (packet != null) {
                String type = packet.getType();
                String data = packet.getData();
                String sender = packet.getSender();

                if ("PONG".equals(type)) {
                    long currentPing = System.currentTimeMillis() - pingStartTime;
                    if (pingValueLabel != null) {
                        pingValueLabel.setText(currentPing + " ms");
                    }
                }
                else if ("RESIGN".equals(type) || "RESIGN".equals(sender) || "RESIGN".equals(data)) {
                    java.awt.Window window = SwingUtilities.getWindowAncestor(canvas);
                    if (window instanceof GameRoom) {
                        GameRoom room = (GameRoom) window;
                        room.handleOpponentResign();
                    }
                }
                else if ("MOVE".equals(type) && data != null) {
                    String[] tokens = data.split(",");
                    int row = Integer.parseInt(tokens[0]);
                    int col = Integer.parseInt(tokens[1]);

                    String colorHint = sender.equalsIgnoreCase("Server") ? "WHITE" : "BLACK";
                    canvas.handleServerMove(row, col, colorHint);
                }
                else if ("PING".equals(type) && data != null) {
                    String[] tokens = data.split(",");
                    int row = Integer.parseInt(tokens[0]);
                    int col = Integer.parseInt(tokens[1]);
                    canvas.triggerPingAt(row, col);
                }
            }
        } catch (Exception e) {
            System.err.println("[Lỗi] Xử lý gói tin từ Server thất bại: " + e.getMessage());
        }
    }

    public void sendMove(int row, int col) {
        sendPacketMove(row, col, currentRoomId, "Player");
    }

    public void sendPacketMove(int row, int col, String roomId, String sender) {
        if (isConnected && out != null) {
            String coordData = row + "," + col;
            Packet p = new Packet("MOVE", sender, roomId, coordData);

            String jsonStr = gson.toJson(p);
            out.println(jsonStr);
        }
    }

    public void sendPacketPing(int row, int col, String roomId, String sender) {
        if (isConnected && out != null) {
            String coordData = row + "," + col;
            Packet p = new Packet("PING", sender, roomId, coordData);

            String jsonStr = gson.toJson(p);
            out.println(jsonStr);
        }
    }
}
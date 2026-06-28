package board;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.Timer;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import network.SocketClient;

public class BoardCanvas extends JPanel {
    private final BoardState boardState;
    private final BoardRenderer renderer;
    private final List<PingEffect> activePings = new ArrayList<>();
    private SocketClient socketClient;

    private JLabel blackScoreLabel;
    private JLabel whiteScoreLabel;

    private Color myColor = Color.BLACK;

    public BoardCanvas() {
        this.boardState = new BoardState();
        this.renderer = new BoardRenderer();

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                requestFocusInWindow();
                onCanvasClicked(event);
            }
        });

        Timer gameLoop = new Timer(16, e -> {
            updateEffects();
            repaint();
        });
        gameLoop.start();
    }

    public void setSocketClient(SocketClient client) {
        this.socketClient = client;
    }

    public void setMyColor(Color color) {
        this.myColor = color;
    }

    public void setScoreLabels(JLabel blackLabel, JLabel whiteLabel) {
        this.blackScoreLabel = blackLabel;
        this.whiteScoreLabel = whiteLabel;
        updateScoreDisplay();
    }

    private void updateScoreDisplay() {
        if (blackScoreLabel == null || whiteScoreLabel == null) return;

        int blackCount = 0;
        int whiteCount = 0;
        int size = boardState.getSize();

        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                Object stone = boardState.getStoneColor(r, c);
                if (stone != null) {
                    String stoneStr = stone.toString().toUpperCase();
                    if (stoneStr.contains("BLACK") || stoneStr.equals("1")) {
                        blackCount++;
                    } else if (stoneStr.contains("WHITE") || stoneStr.equals("2")) {
                        whiteCount++;
                    }
                }
            }
        }

        blackScoreLabel.setText("Quân đen: " + blackCount);
        whiteScoreLabel.setText("Quân trắng: " + whiteCount);
    }

    private void onCanvasClicked(MouseEvent event) {
        double mouseX = event.getX();
        double mouseY = event.getY();

        double pad = renderer.getPadding();
        double cSize = renderer.getCellSize();

        int col = (int) Math.round((mouseX - pad) / cSize);
        int row = (int) Math.round((mouseY - pad) / cSize);

        if (row >= 0 && row < boardState.getSize() && col >= 0 && col < boardState.getSize()) {
            if (boardState.placeStone(row, col, myColor)) {
                updateScoreDisplay();
                if (socketClient != null) {
                    socketClient.sendMove(row, col);
                }
            }
        }
    }

    public void handleServerMove(int row, int col, String colorStr) {
        Color stoneColor = "WHITE".equalsIgnoreCase(colorStr) ? Color.WHITE : Color.BLACK;
        if (boardState.placeStone(row, col, stoneColor)) {
            updateScoreDisplay();

            double pad = renderer.getPadding();
            double cSize = renderer.getCellSize();
            double x = pad + col * cSize;
            double y = pad + row * cSize;
            activePings.add(new PingEffect(x, y));
        }
    }

    private void updateEffects() {
        Iterator<PingEffect> iterator = activePings.iterator();
        while (iterator.hasNext()) {
            PingEffect ping = iterator.next();
            ping.update();
            if (!ping.isActive()) {
                iterator.remove();
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        renderer.drawBoard(g2, boardState, getWidth(), getHeight(), activePings);
    }

    public void triggerPingAt(int row, int col) {
        double pad = renderer.getPadding();
        double cSize = renderer.getCellSize();
        double x = pad + col * cSize;
        double y = pad + row * cSize;
        activePings.add(new PingEffect(x, y));
    }
}
package src.board;

import org.example.SocketClient;

import javax.swing.Timer;
import java.awt.Canvas;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.event.MouseAdapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BoardCanvas extends Canvas {
    private final BoardState boardState;
    private final BoardRenderer renderer;
    private final List<PingEffect> activePings = new ArrayList<>();
    private SocketClient socketClient;

    public BoardCanvas() {
        this.setSize(600, 600);
        this.boardState = new BoardState();
        this.renderer = new BoardRenderer();

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                onCanvasClicked(event);
            }
        });


        Timer gameLoop = new Timer(16, e-> {
            updateEffects();
            repaint();
        });
        gameLoop.start();
    }

    public void setSocketClient(SocketClient client) {
        this.socketClient = client;
    }

    private void onCanvasClicked(MouseEvent event) {
        double mouseX = event.getX();
        double mouseY = event.getY();

        double pad = renderer.getPadding();
        double cSize = renderer.getCellSize();


        int col = (int) Math.round((mouseX - pad) / cSize);
        int row = (int) Math.round((mouseY - pad) / cSize);


        if (boardState.placeStone(row, col, Color.BLACK)) {

            if (socketClient != null) {
                socketClient.sendMove(row, col);
            }
        }
    }


    public void handleServerMove(int row, int col, String colorStr) {
        Color stoneColor = "WHITE".equalsIgnoreCase(colorStr) ? Color.WHITE : Color.BLACK;

        if (boardState.placeStone(row, col, stoneColor)) {

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
    public void paint(Graphics g) {
        super.paint(g);
        // Ép kiểu g thành Graphics2D nâng cao để truyền vào BoardRenderer
        Graphics2D gc = (Graphics2D) g;

        // Gọi hàm drawBoard của BoardRenderer (Sử dụng getWidth() và getHeight() có sẵn của Canvas)
        renderer.drawBoard(gc, boardState, getWidth(), getHeight(), activePings);
    }

    public void triggerPingAt(int row, int col) {
        double pad = renderer.getPadding();
        double cSize = renderer.getCellSize();
        double x = pad + col * cSize;
        double y = pad + row * cSize;
        activePings.add(new PingEffect(x, y));
    }
}


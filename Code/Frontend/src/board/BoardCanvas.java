package src.board;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import network.SocketClient; 

public class BoardCanvas extends Canvas {
    private final BoardState boardState;
    private final BoardRenderer renderer;
    private final List<PingEffect> activePings = new ArrayList<>();
    private SocketClient socketClient; 

    public BoardCanvas() {
        super(600, 600); 
        this.boardState = new BoardState();
        this.renderer = new BoardRenderer();

        
        this.setOnMouseClicked(this::onCanvasClicked);

        
        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateEffects();
                render();
            }
        };
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

    public void render() {
        GraphicsContext gc = this.getGraphicsContext2D();
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
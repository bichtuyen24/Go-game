package board;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class PingEffect {
    private final double centerX;
    private final double centerY;
    private double radius = 5.0;
    private final double maxRadius = 40.0;
    private double opacity = 1.0;
    private boolean active = true;

    public PingEffect(double centerX, double centerY) {
        this.centerX = centerX;
        this.centerY = centerY;
    }

    public void update() {
        if (!active) return;
        radius += 1.5; 
        opacity = 1.0 - (radius / maxRadius); 
        if (radius >= maxRadius) {
            active = false;
        }
    }

    public void draw(GraphicsContext gc) {
        if (!active) return;
        gc.setStroke(Color.rgb(255, 0, 0, opacity)); 
        gc.setLineWidth(3);
        gc.strokeOval(centerX - radius, centerY - radius, radius * 2, radius * 2);
    }

    public boolean isActive() { return active; }
}

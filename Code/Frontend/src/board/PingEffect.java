package board;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.BasicStroke;

public class PingEffect {
    private final double centerX;
    private final double centerY;
    private double radius = 5.0;
    private final double maxRadius = 40.0;
    private float opacity = 1.0f;
    private boolean active = true;

    public PingEffect(double centerX, double centerY) {
        this.centerX = centerX;
        this.centerY = centerY;
    }

    public void update() {
        if (!active) return;
        radius += 1.5;
        opacity = 1.0f - (float)(radius / maxRadius);
        if (radius >= maxRadius) {
            active = false;
        }
    }

    public void draw(Graphics2D g2) {
        if (!active) return;
        g2.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, Math.max(0.0f, Math.min(1.0f, opacity))));
        g2.setColor(Color.RED);
        g2.setStroke(new BasicStroke(3));
        g2.drawOval((int)(centerX - radius), (int)(centerY - radius), (int)(radius * 2), (int)(radius * 2));
        g2.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, 1.0f)); // Reset composite
    }

    public boolean isActive() { return active; }
}

package src.board;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.RenderingHints;

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

    public void draw(Graphics2D gc) {
        if (!active) return;
        gc.setColor(new Color(1.0f,0.0f, 0.0f, (float) opacity));

        // Hoặc nếu `opacity` là số nguyên int (từ 0 đến 255), hãy dùng dòng dưới đây:
        // gc.setColor(new Color(255, 0, 0, opacity));

        // 2. Sửa lệnh làm mượt hình ảnh (Khử răng cưa)
        gc.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 3. Sửa hàm vẽ hình tròn (Ép kiểu về int vì drawOval nhận số nguyên)
        int x = (int) (centerX - radius);
        int y = (int) (centerY - radius);
        int size = (int) (radius * 2);

        gc.drawOval(x, y, size, size);
    }

    public boolean isActive() { return active; }
}


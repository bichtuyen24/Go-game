package src.ui;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import src.ui.GameScene;

public class Main   {
    private final String currentUsername;

    public Main(String username) {
        this.currentUsername = username;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // 1. Tạo cửa sổ JFrame
            JFrame frame = new JFrame("Game Vây Kỳ");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(960, 640);

            // 2. Tạo GameScene và lấy Panel giao diện ráp vào cửa sổ
            GameScene scene = new GameScene("Player_01");
            frame.add(scene.createMainPanel());

            // 3. Hiển thị ra giữa màn hình
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }


}

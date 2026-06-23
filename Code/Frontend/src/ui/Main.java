package ui;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
  
        SwingUtilities.invokeLater(() -> {
           
            LoginFrame loginWindow = new LoginFrame();
            loginWindow.setVisible(true);
        });
    }
}

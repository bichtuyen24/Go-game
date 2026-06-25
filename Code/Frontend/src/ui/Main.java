package src.ui;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
       
        GameScene scene = new GameScene("T");

        stage.setScene(scene.createScene());
        stage.setTitle("Go Game");
        stage.setResizable(false); 
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

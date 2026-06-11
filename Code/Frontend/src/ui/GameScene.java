package ui;

import board.BoardCanvas;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import network.SocketClient;

public class GameScene {

    
    private final String currentUsername;

   
    public GameScene(String username) {
        this.currentUsername = username;
    }

    public Scene createScene() {
       
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #2F3136;"); 

       
        BoardCanvas canvas = new BoardCanvas();
        
       
        SocketClient client = new SocketClient();
        canvas.setSocketClient(client);
        client.connectAsync(canvas);

        StackPane canvasContainer = new StackPane(canvas);
        canvasContainer.setPadding(new Insets(20));
        canvasContainer.setAlignment(Pos.CENTER);
        root.setCenter(canvasContainer);

        
        VBox rightSidebar = new VBox(15);
        rightSidebar.setPrefWidth(300);
        rightSidebar.setMinWidth(300);
        rightSidebar.setMaxWidth(300);
        rightSidebar.setPadding(new Insets(20, 20, 20, 0));

        
        VBox playerInfoBox = new VBox(10);
        playerInfoBox.setPadding(new Insets(12));
        playerInfoBox.setStyle("-fx-background-color: #202225; -fx-background-radius: 8;");

        Label titleLabel = new Label("PHÒNG CHƠI #01");
        titleLabel.setTextFill(Color.WHITE);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 15));

        
        Label p1Label = new Label("⚫ Người chơi 1: " + currentUsername);
        p1Label.setTextFill(Color.LIGHTGRAY);
        Label p2Label = new Label("⚪ Người chơi 2: Đang đợi đối thủ...");
        p2Label.setTextFill(Color.LIGHTGRAY);

        playerInfoBox.getChildren().addAll(titleLabel, new Separator(), p1Label, p2Label);

        
        VBox chatBox = new VBox(8);
        chatBox.setStyle("-fx-background-color: #202225; -fx-background-radius: 8;");
        chatBox.setPadding(new Insets(12));
        VBox.setVgrow(chatBox, Priority.ALWAYS);

        Label chatTitle = new Label("Trò Chuyện Realtime");
        chatTitle.setTextFill(Color.WHITE);
        chatTitle.setFont(Font.font("Arial", FontWeight.BOLD, 13));

        TextArea chatArea = new TextArea();
        chatArea.setEditable(false);
        chatArea.setWrapText(true);
        chatArea.setText("[Hệ thống]: Chào mừng bạn vào phòng chơi cờ vây!\n");
        chatArea.setStyle("-fx-control-inner-background: #36393F; -fx-text-fill: white;");
        VBox.setVgrow(chatArea, Priority.ALWAYS);

        HBox chatInputRow = new HBox(8);
        TextField chatInputField = new TextField();
        chatInputField.setPromptText("Nhập tin nhắn...");
        chatInputField.setStyle("-fx-background-color: #40444B; -fx-text-fill: white;");
        HBox.setHgrow(chatInputField, Priority.ALWAYS);
        
        Button sendButton = new Button("Gửi");
        sendButton.setStyle("-fx-background-color: #7289DA; -fx-text-fill: white; -fx-font-weight: bold;");

        sendButton.setOnAction(e -> {
            if (!chatInputField.getText().trim().isEmpty()) {
                chatArea.appendText("[" + currentUsername + "]: " + chatInputField.getText() + "\n");
                chatInputField.clear();
            }
        });

        chatInputRow.getChildren().addAll(chatInputField, sendButton);
        chatBox.getChildren().addAll(chatTitle, chatArea, chatInputRow);

        
        HBox actionButtons = new HBox(15);
        actionButtons.setAlignment(Pos.CENTER);

        Button readyBtn = new Button("Sẵn Sàng");
        readyBtn.setStyle("-fx-background-color: #43B581; -fx-text-fill: white; -fx-font-weight: bold;");
        readyBtn.setPrefWidth(120);

        Button resignBtn = new Button("Đầu Hàng");
        resignBtn.setStyle("-fx-background-color: #F04747; -fx-text-fill: white; -fx-font-weight: bold;");
        resignBtn.setPrefWidth(120);

        actionButtons.getChildren().addAll(readyBtn, resignBtn);

        
        rightSidebar.getChildren().addAll(playerInfoBox, chatBox, actionButtons);
        root.setRight(rightSidebar);

        return new Scene(root, 960, 640);
    }
}

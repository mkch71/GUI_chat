package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Клиент сетевого чата");
        primaryStage.setScene(new Scene(root, 275, 300));
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
        //primaryStage.getScene().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, primaryStage.setScene();));

    }


    public static void main(String[] args) {
        launch(args);
    }
}

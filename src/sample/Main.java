package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
/*
Работает с https://github.com/mkch71/chat/tree/master
*/
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
       // Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        // передать stage в контроллер
        controller.setStage(primaryStage);
        primaryStage.setTitle("Клиент сетевого чата");
        primaryStage.setScene(new Scene(root, 400, 300));
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

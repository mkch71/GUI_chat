package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Controller {
    Socket socket;
    DataOutputStream out;
    Thread thread;
    DataInputStream in;
    @FXML
    TextArea textArea;
    @FXML
    TextField textField;
    @FXML
    Button buttonConnect;

    @FXML
    private void onSubmit(){
        if (socket == null || socket.isClosed()) {
            Platform.runLater(() -> textArea.appendText("Необходимо подключится! \n"));
            return;}
        String text = textField.getText();
        Platform.runLater(() -> textArea.appendText(text+"\n")); //Почему то просто appendText после отключения -
                                                                    // подключения падал с ошибкой NullPoinEx...
        textField.clear();
        try {
            out.writeUTF(text);
        } catch (IOException e) {
            Platform.runLater(() -> textArea.appendText("Ошибка"));
            e.printStackTrace();
        }

    }
    /*@FXML
    private void exitApplication(ActionEvent event){
        socket = null;
        Platform.exit();
    }*/

    @FXML
    private void connect(){
        try {
            if(socket!=null && !socket.isClosed()){
                thread.interrupt();

                if(thread.isInterrupted())
                    throw new InterruptedException();

            } else {
                socket = new Socket("localhost", 8188);
                out = new DataOutputStream(socket.getOutputStream());
                //System.out.println("Подключился");
                in = new DataInputStream(socket.getInputStream());
                buttonConnect.setText("Отключиться");
                String response = in.readUTF(); // Ждём сообщение от сервера
                Platform.runLater(() -> textArea.appendText("Ответ от сервера: " + response + "\n"));

                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (!Thread.currentThread().isInterrupted()) {
                            try {
                                String response = in.readUTF();
                                Platform.runLater(() -> textArea.appendText(response + "\n"));
                            } catch (IOException exception) {
                                Platform.runLater(() -> textArea.appendText("Нет связи с сервером!\n"));
                                //exception.printStackTrace();
                            }
                        }
                    }
                });
                thread.start();
            }


            } catch (InterruptedException ex){
                try {
                    socket.close();
                    in.close();
                    out.close();

                    thread.interrupt();
                    textArea.appendText("Отключились \n");
                    buttonConnect.setText("Подключиться");
                } catch (IOException e) {
                    e.printStackTrace();
                }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }


}

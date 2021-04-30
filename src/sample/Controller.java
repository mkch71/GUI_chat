package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
    @FXML
    TextArea textArea;
    @FXML
    TextField textField;

    @FXML
    private void onSubmit(){
        if (socket == null) {
            textArea.appendText("Необходимо подключится! \n");
            return;}
        String text = textField.getText();
        textArea.appendText(text+"\n");
        textField.clear();
        try {
            out.writeUTF(text);
        } catch (IOException e) {
            textArea.appendText("Ошибка");
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
            socket = new Socket("localhost",8188);
            out=new DataOutputStream(socket.getOutputStream());
            System.out.println("Подключился");
            DataInputStream in =new DataInputStream(socket.getInputStream());

            String response = in.readUTF(); // Ждём сообщение от сервера
            textArea.appendText("Ответ от сервера: "+response+"\n");

            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while(true){
                        try {
                            String response = in.readUTF();
                            textArea.appendText(response+"\n");
                        } catch (IOException exception) {
                            exception.printStackTrace();
                        }
                    }
                }
            });
            thread.start();



        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }


}

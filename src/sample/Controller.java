package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Controller {
    Socket socket;
    DataOutputStream out;
    Thread thread;
    //DataInputStream in;
    ObjectInputStream ois;
    @FXML
    TextArea textArea;
    @FXML
    TextField textField;
    @FXML
    Button buttonConnect;
    @FXML
    TextArea textAreaUserList;
    @FXML
    Label label;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }
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
                //in = new DataInputStream(socket.getInputStream());
                ois = new ObjectInputStream(socket.getInputStream());
                label.setText("OnLine");
                buttonConnect.setText("Отключиться");
                String response = ois.readObject().toString(); // Ждём сообщение от сервера
                Platform.runLater(() -> textArea.appendText("Ответ от сервера: " + response + "\n"));

                //stage.setTitle("Клиент...");
                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (!Thread.currentThread().isInterrupted()) {
                            try {
                                Object responseObject = ois.readObject();
                                //System.out.println(responseObject.getClass().toString());
                                if(String.class == responseObject.getClass()){
                                    Platform.runLater(() -> textArea.appendText(responseObject + "\n"));

                                } else if (ArrayList.class == responseObject.getClass()){
                                    ArrayList<String> usersName= new ArrayList<>();
                                    usersName = (ArrayList<String>) responseObject;
                                    textAreaUserList.clear();
                                    for (String userName:usersName){
                                        Platform.runLater(() -> textAreaUserList.appendText(userName+"\n"));
                                    }
                                } else {
                                    Platform.runLater(() -> textAreaUserList.appendText(
                                            responseObject.getClass().toString()+"\n"));

                                    System.out.println("Ответ не распознан");
                                }

                            } catch (Exception exception) {
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
                    ois.close();
                    out.close();

                    thread.interrupt();
                    textArea.appendText("Отключились \n");
                    label.setText("NoConnection...");
                    buttonConnect.setText("Подключиться");
                } catch (IOException e) {
                    e.printStackTrace();
                }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


}

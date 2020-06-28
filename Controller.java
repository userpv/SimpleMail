package sample;


import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Controller {
    @FXML
    private Button btn_enter;
    @FXML
    private Button btn_reg;
    @FXML
    private Button btn_reg_finish;
    @FXML
    private Button btn_write;
    @FXML
    private Button btn_Exit;
    @FXML
    private Button btn_Send;
    @FXML
    private Button btn_received;
    @FXML
    private Pane pane_enter;
    @FXML
    private Pane pane_box;
    @FXML
    private Pane pane_reg;
    @FXML
    private Pane pane_send;
    @FXML
    private Pane pane_read;
    @FXML
    private TextField textName;
    @FXML
    private TextField textFamilyName;
    @FXML
    private TextField textLogin;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField text_enterLogin;
    @FXML
    private PasswordField password_enterField;
    @FXML
    private TextField titleLater;
    @FXML
    private TextField receiver;
    @FXML
    private Text incorrect_password;
    @FXML
    private ListView listLater;
    @FXML
    private TextArea write_later;
    @FXML
    private TextArea read_latter;
    @FXML
    private Label sender_login;
    @FXML
    private Label sender_name;


    private Client client;
    private final int PORT = 1900;
    private final String HOST = "localhost";
    static Controller controller;
    static ArrayList <Latter> letters  = new ArrayList<Latter>();



    public void onClick_enter() {

        controller = new Controller();
        controller.client = new Client();

        String lineCheck = "";
        String lineLater = "";

        try {
            controller.client.setSocket(new Socket(HOST, PORT));

            controller.client.setLogin(text_enterLogin.getText());
            controller.client.setPassword(password_enterField.getText());
            InputStream inputStream = controller.client.getSocket().getInputStream();
            OutputStream outputStream = controller.client.getSocket().getOutputStream();
            String enter_data = "Вход" + controller.client.getLogin() + ";" + controller.client.getPassword();

            outputStream.write(enter_data.getBytes());

            byte[] data = new byte[128];
            int bytes = 0;
            do
            {
                bytes = inputStream.read(data, 0, data.length);
                lineCheck += new String(data, 0, bytes);
            }
            while (inputStream.available()!=0);

            String[] split_message = lineCheck.split(";");
            String sender = split_message[0];
            lineCheck = split_message[1];

        if (lineCheck.equals("Вход")) {



            String later_data = "Письма";
            outputStream.write(later_data.getBytes());

            Thread thread_receiver = new ThreadReceiver(inputStream, letters, listLater);
            thread_receiver.start();

            pane_enter.setVisible(false);
            pane_enter.setDisable(true);
            pane_box.setVisible(true);
            pane_box.setDisable(false);
            incorrect_password.setVisible(false);
            text_enterLogin.clear();
            password_enterField.clear();
        } else
            incorrect_password.setVisible(true);
    }
        catch (IOException e) {
            e.printStackTrace();}
    }
    public void onClick_reg_begin(){

        pane_enter.setVisible(false);
        pane_enter.setDisable(true);
        pane_reg.setVisible(true);
        pane_reg.setDisable(false);
    }
    public void onClick_reg_complete() {

        pane_box.setVisible(true);
        pane_box.setDisable(false);
        pane_reg.setVisible(false);
        pane_reg.setDisable(true);
        controller = new Controller();
        controller.client = new Client();

        try {
            controller.client.setSocket(new Socket(HOST, PORT));
            controller.client.setName(textName.getText());
            controller.client.setFamily_name(textFamilyName.getText());
            controller.client.setLogin(textLogin.getText());
            //login = textLogin.getText();
            controller.client.setPassword(passwordField.getText());
            OutputStream outputStream = controller.client.getSocket().getOutputStream();
            String reg_data = "Регистрация"+ controller.client.getName()+";"+controller.client.getFamily_name()
                +";"+controller.client.getLogin()+";"+controller.client.getPassword();
            outputStream.write(reg_data.getBytes());
        }
        catch (IOException e) {
        e.printStackTrace();}
        textName.clear();
        textFamilyName.clear();
        textLogin.clear();
        passwordField.clear();

    }
    public void onClick_Exit(){

        pane_enter.setVisible(true);
        pane_enter.setDisable(false);
        pane_box.setVisible(false);
        pane_box.setDisable(true);
        pane_send.setVisible(false);
        pane_send.setDisable(true);
        pane_read.setVisible(false);
        pane_read.setDisable(true);
        letters.clear();
        listLater.getItems().clear();
        listLater.setVisible(false);
        listLater.setDisable(true);
        read_latter.clear();

        if (controller.client.getSocket()!=null)
            {
                try {
                    controller.client.getSocket().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
    }

    public void onClick_Write(){

        pane_send.setVisible(true);
        pane_send.setDisable(false);
        listLater.setVisible(false);
        listLater.setDisable(true);
        pane_read.setDisable(true);
        pane_read.setVisible(false);
        read_latter.clear();
    }

    public void onClick_Receive(){

        pane_send.setVisible(false);
        pane_send.setDisable(true);
        listLater.setVisible(true);
        listLater.setDisable(false);
        pane_read.setDisable(true);
        pane_read.setVisible(false);
        read_latter.clear();
    }

    public void onClick_Read(){

        pane_send.setVisible(false);
        pane_send.setDisable(true);
        listLater.setVisible(false);
        listLater.setDisable(true);
        int i = listLater.getSelectionModel().getSelectedIndex();
        read_latter.appendText(letters.get(i).getText());
        sender_login.setText(letters.get(i).getSender());
        sender_name.setText(letters.get(i).getName()+" "+letters.get(i).getFamily_name());
       // sender_login.setText(letters.get(i).getSender().);
        pane_read.setVisible(true);
        pane_read.setDisable(false);
    }


    public void onClick_Send(){

        pane_box.setVisible(true);
        pane_box.setDisable(false);
        pane_send.setVisible(false);
        pane_send.setDisable(true);

        try {
        OutputStream outputStream = controller.client.getSocket().getOutputStream();
        String later_data = "Письмо"+ receiver.getText()+";"+ write_later.getText()
                +";"+controller.client.getLogin()+";"+titleLater.getText();
            outputStream.write(later_data.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        receiver.clear();
        titleLater.clear();
        write_later.clear();
    }

}

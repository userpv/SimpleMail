package sample;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;


public class ClientTasks {
    public Socket socket;
    Server server;
    String userName;
    String family_name;
    String login;
    String password;
    public InputStream in;
    public OutputStream out;

    public ClientTasks(Socket socket, Server server)  {
        this.socket = socket;
        this.server = server;
        try {
            in = this.socket.getInputStream();
            out = this.socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void workClient() {
        try {
            in = socket.getInputStream();
            out = socket.getOutputStream();
            String message=null;

            while (true) {
                try {

                    //Получение сообщения
                    message = getMessage();

                    //Определение типа данных

                    //Данные о регистрации
                    if (message.startsWith("Регистрация")){

                        //удаление опознавательной подстроки
                        message = message.substring("Регистрация".length());

                        //разбиение общей строки на нужные
                        String [] split_message = message.split(";");

                        //заполнение полей
                        userName = split_message[0];
                        family_name = split_message[1];
                        login = split_message[2];
                        password = split_message[3];
                        message = userName+" "+family_name+" "+login;

                        //добавление нового соединения в список активных
                        server.addConnection(this);

                        //добавление нового пользователя
                        server.addUser(this);

                        System.out.println(message);
                    }


                    //Данные о входе
                    if (message.startsWith("Вход")) {
                        message = message.substring("Вход".length());
                        String[] split_message = message.split(";");
                        login = split_message[0];
                        password = split_message[1];
                        Latter latter = new Latter();


                        //проверка пользователя
                        if (server.checkUser(login, password)) {
                            latter.setReceiver(login);
                            latter.setText("Вход");
                            latter.setSender(login);
                        }
                        //неверные данные для входа
                        else {
                            latter.setReceiver(login);
                            latter.setText("Запрещено");
                            latter.setSender(login);
                        }

                        //проверка списка соединений
                        server.checkConnection(this);

                        //отправка разрешения на вход
                        server.serverMessage(latter);

                    }

                    //Данные представляют отправленное письмо
                    if (message.startsWith("Письмо")){
                        message = message.substring("Письмо".length());

                        String [] split_message = message.split(";");

                        Latter latter = new Latter();
                        latter.setReceiver(split_message[0]);
                        latter.setText(split_message[1]);
                        latter.setSender(split_message[2]);
                        latter.setTitle(split_message[3]);
                        latter.setName(server.findUser_name(split_message[2]));
                        latter.setFamily_name(server.findUser_nameFamily(split_message[2]));

                        //отправка письма в хранилище

                        DB_work.sendMail(latter.getReceiver(), latter.getText(), latter.getSender(),
                                latter.getTitle(), latter.getName(), latter.getFamily_name());
                        //Server.boxMail.add(latter);

                    }

                    //Данные представляют запрос на получение писем
                    if (message.startsWith("Письма")) {

                        //отправка писем
                        server.sendMail(login);
                    }

                } catch (Exception e) {
                    message = login + ": соединение прервано";
                    System.out.println(message);
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            server.removeConnection(this.login);
            close();
        }
    }

    private String getMessage() throws Exception {
        byte[] data = new byte[128];
        int bytes = 0;
        bytes = in.read(data, 0, data.length);
        String line = "";
        if(new String(data, 0, bytes)!=null)
        {
            line = new String(data, 0, bytes);
        }
        return line;

    }


    void close() {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}

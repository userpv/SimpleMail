package sample;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    public static final int PORT = 1900;
    static ArrayList<ClientTasks> clients;
    static ArrayList<ClientTasks> userList;
    static ArrayList<Latter> boxMail = new ArrayList<Latter>();
    private static ServerSocket serverSocket;
    private static Socket socket;

    public Server()  {
        clients = new ArrayList<ClientTasks>();
        userList = new ArrayList<ClientTasks>();
        serverSocket = null;
    }

    public static void main(String[] args)  {

        try {

            //поток для обработки подключений
            Thread  listenThread = new ListenThread();
            listenThread.start();
        }

        catch (Exception e) {
            e.printStackTrace();
            try {
                disconnect();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    public void serverMessage(Latter latter) throws IOException {


        for (int i = 0; i < clients.size(); i++)
        {
            if(clients.get(i).login== latter.getReceiver())
            {
                String message = latter.getSender()+";"+latter.getText()+';'+latter.getTitle();
                clients.get(i).out.write(message.getBytes());
                clients.get(i).out.flush();
            }
        }
    }

    public void removeConnection(String login) {
        ClientTasks client = null;
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).login == login) {
                client = clients.get(i);
            }
        }
        clients.remove(client);
    }

    public void addConnection(ClientTasks clientTasks) {
        clients.add(clientTasks);
    }

    public void addUser(ClientTasks clientTasks) {
        DB_work.addUser(clientTasks.userName, clientTasks.family_name, clientTasks.login, clientTasks.password);
        //userList.add(clientTasks);
    }

    public static void disconnect() throws IOException {
        serverSocket.close();
        for (int i = 0; i < clients.size(); i++) {
            clients.get(i).close();
        }
    }

    public boolean checkUser(String login, String password) {
        /*for (int i = 0; i < userList.size(); i++){
            if ((userList.get(i).login.equals(login)) && (userList.get(i).password.equals(password)))
            {
                return true;
            }
        }*/
        if (DB_work.checkUser(login, password)){
            return true;
        };
        return false;
    }

    public void checkConnection(ClientTasks clientTasks) {
        if (!clients.contains(clientTasks)){
            addConnection(clientTasks);
        }
    }

    public void sendMail(String login) throws IOException {
        /*if (boxMail.size()!=0) {
            for (int j = 0; j < boxMail.size(); j++) {
                for (int i = 0; i < clients.size(); i++) {
                    if (clients.get(i).login.equals(boxMail.get(j).getReceiver())) {
                        String message = boxMail.get(j).getSender() + ";" + boxMail.get(j).getText() + ';'
                                + boxMail.get(j).getTitle() + ';' + boxMail.get(j).getName()
                                + ';' + boxMail.get(j).getFamily_name() + ';';
                        clients.get(i).out.write(message.getBytes());
                        clients.get(i).out.flush();
                    }
                }
            }
        }
        else for (int i = 0; i < clients.size(); i++) {
                String message = "нет писем";
                clients.get(i).out.write(message.getBytes());
                clients.get(i).out.flush();
            }*/

        String message = "";
        message = DB_work.receiveMail(login);

        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).login.equals(login)) {
                if (!message.equals(""))
                {
                    clients.get(i).out.write(message.getBytes());
                    clients.get(i).out.flush();
                }
                else {
                    message = "нет писем";
                    clients.get(i).out.write(message.getBytes());
                    clients.get(i).out.flush();
                }
            }
        }
    }

    public String findUser_name(String login) {
        /*for (int i = 0; i < userList.size(); i++){
            if (userList.get(i).login.equals(login))
            {
                return userList.get(i).userName;
            }
            return "";
        }*/
        return DB_work.findUser_name(login);
    }

    public String findUser_nameFamily(String login) {
        /*for (int i = 0; i < userList.size(); i++){
            if (userList.get(i).login.equals(login))
            {
                return userList.get(i).family_name;
            }
        }
        return "";*/
        return DB_work.findUser_nameFamily(login);
    }

    private static class workThread extends Thread {
        ClientTasks clientTasks;
        public workThread(ClientTasks clientTasks) {
            this.clientTasks = clientTasks;
        }

        @Override
        public void run() {
            System.out.println("Accept" + clientTasks.socket.getInetAddress());
            this.clientTasks.workClient();
        }
    }

    private static class ListenThread extends Thread {

        @Override
        public void run() {
            try {
                ServerSocket serverSocket = new ServerSocket(PORT);

                int numThr = 0;
                ArrayList<Thread> pool = new ArrayList<Thread>();
                Server server = new Server();
                while (true)
                {
                    socket = serverSocket.accept();
                    System.out.println("Server running");

                    ClientTasks clientTasks = new ClientTasks(socket, server);

                    //поток для обработки действий со стороны клиента
                    Thread thr = new workThread(clientTasks);
                    pool.add(thr);
                    pool.get(numThr).start();
                    numThr++;
                }
            }

            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}



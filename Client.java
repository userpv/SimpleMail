package sample;

import java.net.Socket;

public class Client {
    private Socket socket;
    private String name;
    private String family_name;
    private String login;
    private String password;


    public Client(){
    }

    public Client(Socket socket, String name, String family_name, String login, String password){
        this.socket = socket;
        this.name = name;
        this.family_name = family_name;
        this.login = login;
        this.password = password;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFamily_name() {
        return family_name;
    }

    public void setFamily_name(String family_name) {
        this.family_name = family_name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

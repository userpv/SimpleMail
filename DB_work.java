package sample;

import java.sql.*;

public class DB_work {


    static void addUser(String name, String family_name, String login, String password) {
        Connection connection = null;
        String url = "jdbc:mysql://localhost/mail_server?serverTimezone=Europe/Moscow&useSSL=false";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            System.out.println("Connection succesfull!");
            connection = DriverManager.getConnection(url, ("root"), ("password"));
            if (connection == null) {
                System.out.println("Нет соединения с БД");
                System.exit(0);
            }

            Statement statement = connection.createStatement();

            String query_insert = "INSERT INTO `mail_server`.`users` " +
                    "(`user_name`, `user_family_name`, `user_login`, `user_password`) " +
                    "VALUES ('"+name+"', '"+family_name+"', '"+login+"', '"+password+"');";
            int num = statement.executeUpdate(query_insert);
            statement.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean checkUser(String login, String password) {
        Connection connection = null;
        String url = "jdbc:mysql://localhost/mail_server?serverTimezone=Europe/Moscow&useSSL=false";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            System.out.println("Connection succesfull!");
            connection = DriverManager.getConnection(url, ("root"), ("password"));
            if (connection == null) {
                System.out.println("Нет соединения с БД");
                System.exit(0);
            }

            Statement statement = connection.createStatement();
            String checkQuery = "SELECT * FROM `mail_server`.`users` WHERE user_login = '" + login + "' AND user_password = '" + password + "';";
            ResultSet resultSet = statement.executeQuery(checkQuery);
            resultSet.next();
            if (resultSet.getRow() == 0) {
                System.out.println(resultSet.getRow());
                return false;
            }
            resultSet.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    public static String findUser_name(String login) {
        Connection connection = null;
        String url = "jdbc:mysql://localhost/mail_server?serverTimezone=Europe/Moscow&useSSL=false";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            System.out.println("Connection succesfull!");
            connection = DriverManager.getConnection(url, ("root"), ("password"));
            if (connection == null) {
                System.out.println("Нет соединения с БД");
                System.exit(0);
            }

            Statement statement = connection.createStatement();
            String checkQuery = "SELECT * FROM `mail_server`.`users` WHERE user_login = '" + login + "';";
            ResultSet resultSet = statement.executeQuery(checkQuery);
            resultSet.next();
            String string = resultSet.getString(1);
            return string;

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    public static String findUser_nameFamily(String login) {
        Connection connection = null;
        String url = "jdbc:mysql://localhost/mail_server?serverTimezone=Europe/Moscow&useSSL=false";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            System.out.println("Connection succesfull!");
            connection = DriverManager.getConnection(url, ("root"), ("password"));
            if (connection == null) {
                System.out.println("Нет соединения с БД");
                System.exit(0);
            }

            Statement statement = connection.createStatement();
            String checkQuery = "SELECT * FROM `mail_server`.`users` WHERE user_login = '" + login + "';";
            ResultSet resultSet = statement.executeQuery(checkQuery);
            resultSet.next();
            String string = resultSet.getString(2);
            return string;

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    public static void sendMail(String receiver, String text, String sender, String title, String name, String family_name) {
        Connection connection = null;
        String url = "jdbc:mysql://localhost/mail_server?serverTimezone=Europe/Moscow&useSSL=false";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            System.out.println("Connection succesfull!");
            connection = DriverManager.getConnection(url, ("root"), ("password"));
            if (connection == null) {
                System.out.println("Нет соединения с БД");
                System.exit(0);
            }

            Statement statement = connection.createStatement();

            String query_insert = "INSERT INTO `mail_server`.`latters` (`resever`, `text`, `sender`, `title`, `name`, `name_family`) " +
                            "VALUES ('"+receiver+"', '"+text+"', '"+sender+"', '"+title+"', '"+name+"', '"+family_name+"');";
            int num = statement.executeUpdate(query_insert);
            statement.close();
        }

        catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String receiveMail(String login) {

        Connection connection = null;
        String url = "jdbc:mysql://localhost/mail_server?serverTimezone=Europe/Moscow&useSSL=false";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            System.out.println("Connection succesfull!");
            connection = DriverManager.getConnection(url, ("root"), ("password"));
            if (connection == null) {
                System.out.println("Нет соединения с БД");
                System.exit(0);
            }

            Statement statement = connection.createStatement();
            String checkQuery = "SELECT * FROM `mail_server`.`latters` WHERE resever = '" + login + "';";
            ResultSet resultSet = statement.executeQuery(checkQuery);
            resultSet.next();
            String data = "";
                do {
                    for (int i = 2; i < 7; i++) {

                        data += resultSet.getString(i) + ";";

                    }
                    resultSet.next();

                } while (!resultSet.isAfterLast());
            return data;

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }
}

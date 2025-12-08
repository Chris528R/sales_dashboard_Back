package config;


import java.sql.Connection;
import java.sql.DriverManager;

public class DB {
    private static final String URL = "jdbc:mysql://mysql:3306/proyecto?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";

    public static Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

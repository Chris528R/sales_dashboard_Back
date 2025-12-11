package config;


import java.sql.Connection;
import java.sql.DriverManager;

public class DB {
    private static final String URL = "jdbc:mysql://localhost/ventas_ds?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";

    public static Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

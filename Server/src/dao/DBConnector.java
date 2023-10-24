package dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DBConnector {
    private final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private final String DB_URL = "jdbc:mysql://localhost/clientserver?&useSSL=false";
    private final String USER_NAME = "root";
    private final String PASSWORD = "choi2unt@2k)mysql";
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName(JDBC_DRIVER);
        return DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
    }
}

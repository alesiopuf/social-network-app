package ubb.scs.map.util;

import ubb.scs.map.configuration.DatabaseConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionUtil {
    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            DatabaseConfiguration config = new DatabaseConfiguration();
            connection = DriverManager.getConnection(config.getUrl(), config.getUser(), config.getPassword());
        }
        return connection;
    }
}
package itmo.lab6.managers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Отвечает только за получение JDBC-соединения.
 * Выделено отдельно, чтобы DAO не знали деталей подключения.
 */
public class ConnectionProvider {
    private final String url;
    private final String user;
    private final String password;

    public ConnectionProvider(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}

package itmo.lab6.managers;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Базовый класс для DAO.
 * Здесь хранится provider соединений и общая вспомогательная логика.
 */
public abstract class BaseDao {
    protected final ConnectionProvider connectionProvider;

    protected BaseDao(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    protected Connection getConnection() throws SQLException {
        return connectionProvider.getConnection();
    }
}

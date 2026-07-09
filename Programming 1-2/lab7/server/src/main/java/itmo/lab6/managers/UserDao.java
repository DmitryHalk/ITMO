package itmo.lab6.managers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * DAO для таблицы users.
 */
public class UserDao extends BaseDao {
    private static final Logger logger = Logger.getLogger(UserDao.class.getName());

    public UserDao(ConnectionProvider connectionProvider) {
        super(connectionProvider);
    }

    public boolean insert(String login, String passwordHash) {
        if (login == null || login.isBlank() || passwordHash == null || passwordHash.isBlank()) {
            return false;
        }

        String sql = """
                INSERT INTO users(login, password)
                VALUES(?, ?)
                ON CONFLICT(login) DO NOTHING
                """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, login);
            ps.setString(2, passwordHash);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.severe("Ошибка регистрации пользователя: " + e.getMessage());
            return false;
        }
    }

    public boolean authenticate(String login, String passwordHash) {
        if (login == null || login.isBlank() || passwordHash == null || passwordHash.isBlank()) {
            return false;
        }

        String sql = "SELECT 1 FROM users WHERE login = ? AND password = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, login);
            ps.setString(2, passwordHash);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            logger.severe("Ошибка аутентификации пользователя: " + e.getMessage());
            return false;
        }
    }
}

package itmo.lab6.managers;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

/**
 * Инициализация схемы БД.
 * Это не DAO: этот класс только создаёт sequence и таблицы.
 */
public class SchemaInitializer {
    private static final Logger logger = Logger.getLogger(SchemaInitializer.class.getName());

    private final ConnectionProvider connectionProvider;

    public SchemaInitializer(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    public void initSchema() {
        String createSeq = "CREATE SEQUENCE IF NOT EXISTS person_id_seq START 1";

        String createUsers = """
            CREATE TABLE IF NOT EXISTS users (
                login    VARCHAR(255) PRIMARY KEY,
                password VARCHAR(56)  NOT NULL
            )""";

        String createPersons = """
            CREATE TABLE IF NOT EXISTS persons (
                id            INTEGER          PRIMARY KEY DEFAULT nextval('person_id_seq'),
                name          VARCHAR(255)     NOT NULL,
                coord_x       BIGINT           NOT NULL,
                coord_y       DOUBLE PRECISION NOT NULL,
                creation_date TIMESTAMP        NOT NULL,
                height        REAL             NOT NULL,
                birthday      TIMESTAMP,
                hair_color    VARCHAR(50),
                nationality   VARCHAR(50),
                loc_x         DOUBLE PRECISION,
                loc_y         DOUBLE PRECISION,
                loc_z         INTEGER,
                owner_login   VARCHAR(255)     REFERENCES users(login) ON DELETE SET NULL
            )""";

        try (Connection conn = connectionProvider.getConnection();
             Statement st = conn.createStatement()) {
            st.execute(createSeq);
            st.execute(createUsers);
            st.execute(createPersons);
            logger.info("Схема БД инициализирована.");
        } catch (SQLException e) {
            logger.severe("Ошибка инициализации схемы: " + e.getMessage());
            throw new RuntimeException("Не удалось инициализировать схему БД", e);
        }
    }
}

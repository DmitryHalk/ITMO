package itmo.lab6.managers;

import itmo.lab6.model.Person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * DAO для таблицы persons.
 * Здесь находится вся SQL-логика по объектам коллекции.
 */
public class PersonDao extends BaseDao {
    private static final Logger logger = Logger.getLogger(PersonDao.class.getName());

    private final PersonMapper mapper = new PersonMapper();

    public PersonDao(ConnectionProvider connectionProvider) {
        super(connectionProvider);
    }

    public int insert(Person person) {
        if (person == null || person.getCoordinates() == null || person.getCreationDate() == null) {
            return -1;
        }
        if (person.getOwnerLogin() == null || person.getOwnerLogin().isBlank()) {
            return -1;
        }

        String sql = """
            INSERT INTO persons
                (name, coord_x, coord_y, creation_date, height, birthday,
                 hair_color, nationality, loc_x, loc_y, loc_z, owner_login)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            RETURNING id
            """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            fillInsertStatement(ps, person);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt("id") : -1;
            }
        } catch (SQLException e) {
            logger.severe("Ошибка вставки Person: " + e.getMessage());
            return -1;
        }
    }

    public boolean update(int id, Person person, String ownerLogin) {
        if (person == null || person.getCoordinates() == null) {
            return false;
        }
        if (ownerLogin == null || ownerLogin.isBlank()) {
            return false;
        }

        String sql = """
            UPDATE persons
            SET name=?, coord_x=?, coord_y=?, height=?, birthday=?,
                hair_color=?, nationality=?, loc_x=?, loc_y=?, loc_z=?
            WHERE id=? AND owner_login=?
            """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, person.getName());
            ps.setLong(2, person.getCoordinates().getX());
            ps.setDouble(3, person.getCoordinates().getY());
            ps.setFloat(4, person.getHeight());

            if (person.getBirthday() != null) {
                ps.setTimestamp(5, Timestamp.valueOf(person.getBirthday()));
            } else {
                ps.setNull(5, Types.TIMESTAMP);
            }

            if (person.getHairColor() != null) {
                ps.setString(6, person.getHairColor().name());
            } else {
                ps.setNull(6, Types.VARCHAR);
            }

            if (person.getNationality() != null) {
                ps.setString(7, person.getNationality().name());
            } else {
                ps.setNull(7, Types.VARCHAR);
            }

            if (person.getLocation() != null) {
                ps.setDouble(8, person.getLocation().getX());

                if (person.getLocation().getY() != null) {
                    ps.setDouble(9, person.getLocation().getY());
                } else {
                    ps.setNull(9, Types.DOUBLE);
                }

                if (person.getLocation().getZ() != null) {
                    ps.setInt(10, person.getLocation().getZ());
                } else {
                    ps.setNull(10, Types.INTEGER);
                }
            } else {
                ps.setNull(8, Types.DOUBLE);
                ps.setNull(9, Types.DOUBLE);
                ps.setNull(10, Types.INTEGER);
            }

            ps.setInt(11, id);
            ps.setString(12, ownerLogin);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.severe("Ошибка обновления Person id=" + id + ": " + e.getMessage());
            return false;
        }
    }

    public boolean deleteById(int id, String ownerLogin) {
        if (ownerLogin == null || ownerLogin.isBlank()) {
            return false;
        }

        String sql = "DELETE FROM persons WHERE id = ? AND owner_login = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setString(2, ownerLogin);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.severe("Ошибка удаления Person id=" + id + ": " + e.getMessage());
            return false;
        }
    }

    public int deleteAllByOwner(String ownerLogin) {
        if (ownerLogin == null || ownerLogin.isBlank()) {
            return -1;
        }

        String sql = "DELETE FROM persons WHERE owner_login = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ownerLogin);
            return ps.executeUpdate();
        } catch (SQLException e) {
            logger.severe("Ошибка удаления объектов пользователя " + ownerLogin + ": " + e.getMessage());
            return -1;
        }
    }

    public List<Person> findAll() {
        String sql = "SELECT * FROM persons ORDER BY id";
        List<Person> persons = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                persons.add(mapper.map(rs));
            }
        } catch (SQLException e) {
            logger.severe("Ошибка загрузки коллекции: " + e.getMessage());
        }

        return persons;
    }

    public Person findById(int id) {
        String sql = "SELECT * FROM persons WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapper.map(rs) : null;
            }
        } catch (SQLException e) {
            logger.severe("Ошибка поиска Person id=" + id + ": " + e.getMessage());
            return null;
        }
    }

    private void fillInsertStatement(PreparedStatement ps, Person person) throws SQLException {
        ps.setString(1, person.getName());
        ps.setLong(2, person.getCoordinates().getX());
        ps.setDouble(3, person.getCoordinates().getY());
        ps.setTimestamp(4, new Timestamp(person.getCreationDate().getTime()));
        ps.setFloat(5, person.getHeight());

        if (person.getBirthday() != null) {
            ps.setTimestamp(6, Timestamp.valueOf(person.getBirthday()));
        } else {
            ps.setNull(6, Types.TIMESTAMP);
        }

        if (person.getHairColor() != null) {
            ps.setString(7, person.getHairColor().name());
        } else {
            ps.setNull(7, Types.VARCHAR);
        }

        if (person.getNationality() != null) {
            ps.setString(8, person.getNationality().name());
        } else {
            ps.setNull(8, Types.VARCHAR);
        }

        if (person.getLocation() != null) {
            ps.setDouble(9, person.getLocation().getX());

            if (person.getLocation().getY() != null) {
                ps.setDouble(10, person.getLocation().getY());
            } else {
                ps.setNull(10, Types.DOUBLE);
            }

            if (person.getLocation().getZ() != null) {
                ps.setInt(11, person.getLocation().getZ());
            } else {
                ps.setNull(11, Types.INTEGER);
            }
        } else {
            ps.setNull(9, Types.DOUBLE);
            ps.setNull(10, Types.DOUBLE);
            ps.setNull(11, Types.INTEGER);
        }

        ps.setString(12, person.getOwnerLogin());
    }
}

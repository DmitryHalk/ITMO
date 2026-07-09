package itmo.lab6.managers;

import itmo.lab6.enums.Color;
import itmo.lab6.enums.Country;
import itmo.lab6.model.Coordinates;
import itmo.lab6.model.Location;
import itmo.lab6.model.Person;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Преобразует строку ResultSet в объект Person.
 * Это отдельный класс, чтобы не дублировать маппинг в DAO.
 */
public class PersonMapper {

    public Person map(ResultSet rs) throws SQLException {
        Person p = new Person();

        p.setId(rs.getInt("id"));
        p.setName(rs.getString("name"));

        Coordinates coords = new Coordinates();
        coords.setX(rs.getLong("coord_x"));
        coords.setY(rs.getDouble("coord_y"));
        p.setCoordinates(coords);

        Timestamp created = rs.getTimestamp("creation_date");
        if (created != null) {
            p.setCreationDate(new Date(created.getTime()));
        }

        p.setHeight(rs.getFloat("height"));

        Timestamp birthday = rs.getTimestamp("birthday");
        if (birthday != null) {
            p.setBirthday(birthday.toLocalDateTime());
        }

        String hairColor = rs.getString("hair_color");
        if (hairColor != null && !hairColor.isBlank()) {
            p.setHairColor(Color.valueOf(hairColor));
        }

        String nationality = rs.getString("nationality");
        if (nationality != null && !nationality.isBlank()) {
            p.setNationality(Country.valueOf(nationality));
        }

        double locX = rs.getDouble("loc_x");
        if (!rs.wasNull()) {
            Location loc = new Location();
            loc.setX(locX);

            Double locY = (Double) rs.getObject("loc_y");
            loc.setY(locY);

            Integer locZ = (Integer) rs.getObject("loc_z");
            loc.setZ(locZ);

            p.setLocation(loc);
        }

        p.setOwnerLogin(rs.getString("owner_login"));
        return p;
    }
}

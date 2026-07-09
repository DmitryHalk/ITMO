package itmo.lab6.model;

import itmo.lab6.enums.*;
import itmo.lab6.interfaces.Validatable;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

public class Person implements Validatable, Comparable<Person>, Serializable {
    private static final long serialVersionUID = 2L;

    private int id;
    private String name;
    private Coordinates coordinates;
    private java.util.Date creationDate;
    private float height;
    private java.time.LocalDateTime birthday;
    private Color hairColor;
    private Country nationality;
    private Location location;
    private String ownerLogin; // кому принадлежит объект

    public Person() {}

    public Person(int id, String name, Coordinates coordinates, Date creationDate,
                  float height, LocalDateTime birthday, Color hairColor,
                  Country nationality, Location location) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.height = height;
        this.birthday = birthday;
        this.hairColor = hairColor;
        this.nationality = nationality;
        this.location = location;
    }

    // Getters
    public int getId()                      { return id; }
    public String getName()                 { return name; }
    public Coordinates getCoordinates()     { return coordinates; }
    public Date getCreationDate()           { return creationDate; }
    public float getHeight()               { return height; }
    public LocalDateTime getBirthday()     { return birthday; }
    public Color getHairColor()            { return hairColor; }
    public Country getNationality()        { return nationality; }
    public Location getLocation()          { return location; }
    public String getOwnerLogin()          { return ownerLogin; }

    // Setters
    public void setId(int id)                          { this.id = id; }
    public void setName(String name)                   { this.name = name; }
    public void setCoordinates(Coordinates coordinates) { this.coordinates = coordinates; }
    public void setCreationDate(Date creationDate)     { this.creationDate = creationDate; }
    public void setHeight(float height)               { this.height = height; }
    public void setBirthday(LocalDateTime birthday)   { this.birthday = birthday; }
    public void setHairColor(Color hairColor)         { this.hairColor = hairColor; }
    public void setNationality(Country nationality)   { this.nationality = nationality; }
    public void setLocation(Location location)        { this.location = location; }
    public void setOwnerLogin(String ownerLogin)      { this.ownerLogin = ownerLogin; }

    @Override
    public int compareTo(Person other) {
        int nameCmp = this.name.compareTo(other.name);
        if (nameCmp != 0) return nameCmp;
        int heightCmp = Float.compare(this.height, other.height);
        if (heightCmp != 0) return heightCmp;
        if (this.birthday != null && other.birthday != null) {
            int birthdayCmp = this.birthday.compareTo(other.birthday);
            if (birthdayCmp != 0) return birthdayCmp;
        } else if (this.birthday != null) return -1;
        else if (other.birthday != null) return 1;
        return 0;
    }

    @Override
    public boolean validate() {
        if (id < 0) return false;
        if (name == null || name.isEmpty()) return false;
        if (coordinates == null) return false;
        if (creationDate == null) return false;
        if (height <= 0) return false;
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return id == person.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", height=" + height +
                ", birthday=" + birthday +
                ", hairColor=" + hairColor +
                ", nationality=" + nationality +
                ", location=" + location +
                ", owner='" + ownerLogin + '\'' +
                '}';
    }
}

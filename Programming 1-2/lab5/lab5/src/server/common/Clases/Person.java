package server.common.Clases;

import server.common.Enums.Color;
import server.common.Enums.Country;
import server.common.Interfaces.Validatable;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;



public class Person implements Validatable, Comparable<Person>{
    private int id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.util.Date creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private float height; //Значение поля должно быть больше 0
    private java.time.LocalDateTime birthday; //Поле может быть null
    private Color hairColor; //Поле может быть null
    private Country nationality; //Поле может быть null
    private Location location; //Поле может быть null

    public Person(int id, String name, Coordinates coordinates, Date creationDate, float height, LocalDateTime birthday, Color hairColor, Country nationality, Location location) {
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

    public String getName(){
        return name;
    }

    public Person() {

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setBirthday(LocalDateTime birthday) {
        this.birthday = birthday;
    }

    public void setHairColor(Color hairColor) {
        this.hairColor = hairColor;
    }

    public void setNationality(Country nationality) {
        this.nationality = nationality;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public float getHeight() {
        return height;
    }

    public Color getHairColor() {
        return hairColor;
    }

    public LocalDateTime getBirthday() {
        return birthday;
    }

    public Country getNationality() {
        return nationality;
    }

    public Location getLocation() {
        return location;
    }

    public int getId(){
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public  int compareTo(Person other) {
        int nameCmp = this.name.compareTo(other.name);
        if (nameCmp != 0) return nameCmp;

        int heightCmp = Float.compare(this.height, other.height);
        if (heightCmp != 0) return heightCmp;

        if (this.birthday != null && other.birthday != null) {
            int birthdayCmp = this.birthday.compareTo(other.birthday);
            if (birthdayCmp != 0) return birthdayCmp;
        } else if (this.birthday != null) {
            return -1;
        } else if (other.birthday != null) return 1;

        return 0;
    }

    @Override
    public boolean validate() {
        if (id <= 0) return false; // добавить проверку на уникальность!!!!!!!!!!!!!!!!!!!
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
        return id == person.id && Float.compare(height, person.height) == 0 && Objects.equals(name, person.name) && Objects.equals(coordinates, person.coordinates) && Objects.equals(creationDate, person.creationDate) && Objects.equals(birthday, person.birthday) && hairColor == person.hairColor && nationality == person.nationality && Objects.equals(location, person.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate, height, birthday, hairColor, nationality, location);
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
                '}';
    }
}



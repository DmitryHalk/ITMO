package com;

import java.util.ArrayList;
import java.util.List;

public class Flat {
    private ArrayList<Room> rooms;
    private double area;

    // Конструктор без комнат - площадь 0
    public Flat() {
        this.rooms = new ArrayList<>();
        this.area = 0;
    }

    // Конструктор с начальными комнатами - вычисляем площадь
    public Flat(Room... initialRooms) {
        this.rooms = new ArrayList<>();
        for (Room room : initialRooms) {
            rooms.add(room);
        }
        this.area = calculateArea();
    }

    // Конструктор с существующим списком - вычисляем площадь
    public Flat(ArrayList<Room> rooms) {
        this.rooms = new ArrayList<>(rooms);
        this.area = calculateArea();
    }

    // Оставим и старый конструктор для обратной совместимости, но пересчитаем площадь
    public Flat(List<Room> rooms, int area) {
        this.rooms = new ArrayList<>(rooms);
        this.area = calculateArea(); // Игнорируем переданную площадь, вычисляем свою
    }

    private double calculateArea() {
        double totalArea = 0;
        for (Room room : rooms) {
            totalArea += room.getArea();
        }
        return totalArea;
    }

    public double getArea() {
        return area;
    }

    public void addRoom(Room room) {
        if (room != null) {
            rooms.add(room);
            area = calculateArea();
        }
    }

    public int getCountRooms(){
        return rooms.toArray().length;
    }
}

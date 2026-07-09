package com;

import java.util.ArrayList;

public class Room {
    protected String name;
    protected double area;
    protected int doorCount;
    protected ArrayList<Window> windows;

    public Room(String name, double area, int doorCount, ArrayList<Window> windows) {
        this.name = name;
        this.area = area;
        this.doorCount = doorCount;
        this.windows = windows;
    }

    public Room(String name, double area, int doorCount) {
        this.name = name;
        this.area = area;
        this.doorCount = doorCount;
    }

    public Room(double area, ArrayList<Window> windows) {
        this.name = name;
        this.area = area;
        this.windows = windows;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public int getDoorCount() {
        return doorCount;
    }

    public void setDoorCount(int doorCount) {
        this.doorCount = doorCount;
    }

    public ArrayList<Window> getWindows() {
        return windows;
    }

    public void setWindows(ArrayList<Window> windows) {
        this.windows = windows;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

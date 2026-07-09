package itmo.lab6.model;

import java.io.Serializable;

public class Location implements Serializable {
    private static final long serialVersionUID = 1L;
    private double x;
    private Double y;
    private Integer z;

    public Location(double x, Double y, Integer z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Location() {
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Integer getZ() {
        return z;
    }

    public void setZ(Integer z) {
        this.z = z;
    }

    @Override
    public String toString() {
        return "{x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}

package itmo.lab6.model;

import itmo.lab6.interfaces.Validatable;
import java.io.Serializable;
import java.lang.Double;

public class Coordinates implements Validatable, Serializable {
    private static final long serialVersionUID = 1L;
    private long x;
    private double y; //Максимальное значение поля: 663

    public Coordinates(long x, double y) {
        this.x = x;
        this.y = y;
    }

    public Coordinates() {
    }

    public long getX() {
        return x;
    }

    public void setX(long x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public boolean validate() {
        if (Double.compare(y, 663) > 0) return false;
        return true;
    }

    @Override
    public String toString() {
        return "{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}

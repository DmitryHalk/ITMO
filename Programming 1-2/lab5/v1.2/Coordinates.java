package itmo;

import java.lang.Double;

public class Coordinates implements Validatable {
    private long x;
    private double y; //Максимальное значение поля: 663

    public Coordinates(long x, double y) {
        this.x = x;
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

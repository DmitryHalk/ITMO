package interfaces;

import exception.FlyExeption;

public interface Flyable {

    public void takeWing() throws FlyExeption;
    public void up(double diff) throws FlyExeption;
    public void down(double diff) throws FlyExeption;

}

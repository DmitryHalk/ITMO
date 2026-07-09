package com;

import interfaces.Flyable;
import exception.FlyExeption;

import java.util.Objects;

public class Carlson extends Character implements Flyable{
    private boolean propeller;
    private boolean onAir;
    private double height;

    public Carlson(String name, Mood mood, Room location) {
        super(name, mood, location);
    }

    public Carlson(String name, Room location) {
        super(name, location);
    }

    public void togglePropeller(){
        if (propeller){
            propeller = false;
        } else {
            propeller = true;
        }
    }

    public boolean isPropeller() {
        return propeller;
    }

    @Override
    public void takeWing() throws FlyExeption {
        if (onAir){
            onAir = false;
        } else {
            if (!isPropeller()){
                throw new FlyExeption(this);
            }
            onAir = true;
            height += 5;
        }
    }
    @Override
    public void up(double h) throws FlyExeption {
        if (!isPropeller()){
            throw new FlyExeption(this);
        }
        height += h;
    }

    @Override
    public void down(double h) throws FlyExeption {
        height -= h;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Carlson carlson = (Carlson) o;
        return propeller == carlson.propeller && onAir == carlson.onAir && Double.compare(height, carlson.height) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(propeller, onAir, height);
    }

    @Override
    public String toString() {
        return "Carlson{" +
                "propeller=" + propeller +
                ", onAir=" + onAir +
                ", height=" + height +
                '}';
    }
}

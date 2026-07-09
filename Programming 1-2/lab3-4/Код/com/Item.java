package com;

import exception.eatException;

import java.util.Objects;

public abstract class Item {
    protected Character owner;
    protected String name;
    protected Room location;

    Item(String name, Character owner) {
        this.name = name;
        this.owner = owner;
    }

    Item(String name, Room location) {
        this.name = name;
        this.location = location;
    }



    public Character getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }

    public void setOwner(Character owner) {
        this.owner = owner;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(owner, item.owner) && Objects.equals(name, item.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(owner, name);
    }

    @Override
    public String toString() {
        return "Item{" +
                "owner=" + owner +
                ", name='" + name + '\'' +
                '}';
    }
}

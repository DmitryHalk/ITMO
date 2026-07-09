package com;

public class Pack extends Item{
    public Pack(String name, Character owner) {
        super(name, owner);
    }

    public Pack(String name, Room location) {
        super(name, location);
    }
}

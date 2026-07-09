package com;

import com.Item;

public class Knife extends Item{

    public Knife(String name, Character owner) {
        super(name, owner);
    }

    public Knife(String name, Room location) {
        super(name, location);
    }

}

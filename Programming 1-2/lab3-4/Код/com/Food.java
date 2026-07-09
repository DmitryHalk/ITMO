package com;

import exception.eatException;

public class Food extends Item{
    private boolean eaten;

    Food(String name, Character owner, boolean eaten){
        super(name, owner);
        this.eaten = eaten;
    }

    Food(String name, Character owner){
        super(name, owner);
        this.eaten = false;
    }

}

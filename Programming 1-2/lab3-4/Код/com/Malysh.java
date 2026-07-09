package com;

public class Malysh extends Character{
    Malysh(String name, Mood mood, Room location){
        super(name, mood, location);
    }

    public Malysh(String name, Room location) {
        super(name, location);
    }


}

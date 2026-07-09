package com;
import exception.LocationException;
import interfaces.Move;
import java.util.ArrayList;
import java.util.Objects;

public abstract class Character implements Move{
    protected String name;
    protected ArrayList<Item> hold = new ArrayList<>();
    protected Mood mood;
    protected Room location;
    protected boolean hiden;

    Character(String name, Mood mood, Room location){
        this.name = name;
        this.mood = mood;
        this.location = location;
    }

    Character(String name, Room location){
        this.name = name;
        this.mood = Mood.NEUTRAL;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Item> getHold() {
        return hold;
    }

    public Mood getMood() {
        return mood;
    }

    public String getLocation() {
        return location.getName();
    }

    public void hear(SoundType s){
        System.out.println(String.format("%s услышал %s", this.getName(), s.getTitle()));
    }

    public void makeSounds(SoundType s){
        System.out.println(String.format("%s издал %s", this.getName(), s.getTitle()));
    }

    public void eat(Food food){
        System.out.println(String.format("%s ел %s", this.getName(), food.getName()));
    }



    public void take(Item item, Room location) throws LocationException {
        if (this.location.equals(location)) {
            System.out.println(String.format("%s взял %s", this.getName(), item.getName()));
            hold.add(item);
        } else {
            throw new LocationException(this, location);
        }

    }

    public void give(Item item, Character pers) throws LocationException{
        if (this.location.equals(pers.location)) {
            pers.take(item, this.location);
            this.hold.remove(item);
            System.out.println(String.format("%s дал %s -> %s", this.getName(), item.getName(), pers.getName()));
        }else {
            throw new LocationException(pers, this.location);
        }
    }

    public void show(Item item, Character pers) throws LocationException{
        if (this.location.equals(pers.location)) {
            System.out.println(String.format("%s показал %s -> %s", this.getName(), item.getName(), pers.getName()));
        }else {
            throw new LocationException(pers, this.location);
        }
    }

    public void put(Item item, Room location){

    }

    public void changeMood(Mood mood){
        this.mood = mood;
        System.out.println(String.format("%s поменял настроение на %s", this.getName(), mood.getTitle()));
    }

    public void hide() {
        hiden = true;
        System.out.println(String.format("%s спрятался в %s", this.getName() ,this.getLocation()));
    }

    public void unhide() {
        hiden = true;
        System.out.println(String.format("%s нашёлся в %s", this.getName() ,this.getLocation()));
    }

    @Override
    public void walkTo(Room r) throws LocationException {
        if (!this.location.equals(r)) {
            location = r;
            System.out.println(String.format("%s пошёл в %s", this.getName(), r.getName()));
        } else {
            throw new LocationException(this);
        }

    }

    @Override
    public void runTo(Room r) throws LocationException{
        if (r == null){

        } if (!this.location.equals(r)) {
            location = r;
            System.out.println(String.format("%s побежал в %s", this.getName(), r.getName()));
        } else {
            throw new LocationException(this);
        }
    }

    @Override
    public void chase(Character c){
        System.out.println(String.format("%s преследует %s", this.getName(), c.getName()));
    }

    @Override
    public void walkToWindow(){
        System.out.println(String.format("%s подошёл к окну", this.getName()));
    };

    public void plantedPlant(Plant p, PlantPot pot){
        pot.planted(p);
        System.out.println(String.format("%s посадил %s в горшок на %s", this.getName(), p.name(), pot.getLocation().getName()));
    }

    public void plantedPlant(Seed seed, PlantPot pot){
        System.out.println(String.format("%s посадил %s в горшок на %s", this.getName(), seed.getName(), pot.getLocation().getName()));
    }

    public void unPlantedPlant(PlantPot pot){
        System.out.println(String.format("%s высадил %s из горшока на %s", this.getName(), pot.getPlant(), pot.getLocation().getName()));
        pot.unPlanted();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Character character = (Character) o;
        return Objects.equals(name, character.name) && Objects.equals(hold, character.hold) && mood == character.mood && Objects.equals(location, character.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, hold, mood, location);
    }

    @Override
    public String toString() {
        return "Character{" +
                "name='" + name + '\'' +
                ", hold=" + hold +
                ", mood=" + mood +
                ", location=" + location +
                '}';
    }
}

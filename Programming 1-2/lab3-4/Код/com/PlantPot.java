package com;

public class PlantPot {
    private Room location;
    private boolean isFill;
    private boolean isPlanted;
    private Plant plant;

    public PlantPot(Room location, boolean isFill, boolean isPlanted) {
        this.location = location;
        this.isFill = isFill;
        this.isPlanted = isPlanted;
    }

    public PlantPot(Room location, boolean isFill) {
        this.location = location;
        this.isFill = isFill;
    }

    public void fill(){
        if (isFill){
            return;
        } else {
            isFill = true;
        }
    }

    public Room getLocation() {
        return location;
    }

    public void setLocation(Room location) {
        this.location = location;
    }

    public void unfill(){

    }

    public boolean getFill(){
        return isFill;
    }

    public void planted(Plant p){
        plant = p;
    }

    public void unPlanted(){
        plant = null;
    }

    public Plant getPlant() {
        return plant;
    }
}

package com;

public enum Color {
    WHITE(100),
    RED(100),
    ORANGE(100),
    YELLOW(100),
    GREEN(100),
    BLUE(100),
    PURPULE(100);
    private int saturation;

    Color(int saturation) {
        this.saturation = saturation;
    }

    public int getSaturation() {
        return saturation;
    }



}

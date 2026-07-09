package com;

public record Plant(String name) {
    @Override
    public String name() {
        return name;
    }
}

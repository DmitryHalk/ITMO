package com;

public enum Mood {
    NEUTRAL("Нейтральный"),
    HAPPY("Весёлый"),
    SAD("Грустный"),
    ARGUMENTATIVE("В настроении спорить"),
    INTERESTED("Интересующийся");
    private String title;

    Mood(String title){
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}

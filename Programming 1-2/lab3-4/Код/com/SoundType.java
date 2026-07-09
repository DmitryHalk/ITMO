package com;

public enum SoundType {
    MUNCH("Чавканье", 70),
    SMACK("чмоканье", 50),
    CHEER("Весёлый возглас", 80),
    CLUCK("Кудахтать", 80),
    SQUELCH("Хлюпаье", 60);
    private String title;
    private int volume; // 0-100

    SoundType(String title, int volume){
        this.title = title;
        this.volume = volume;
    }

    public String getTitle() {
        return title;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

}

package com;

public class Window {
    private boolean isOpen;

    public Window(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public void toggle(){
        if (isOpen){
            isOpen = false;
        } else {
            isOpen = true;
        }
    }
}

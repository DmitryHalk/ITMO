package com;

import exception.LocationException;

import java.util.ArrayList;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        Room kitchen = new Room("Кухня",20.0, 1);
        ArrayList<Window> windows = new ArrayList<>();
        windows.add(new Window(false));
        Room livingRoom = new Room("Комната", 20.0, 1, windows);
        Room bathRoom = new Room("Ванна", 20.0, 1);

        ArrayList<Room> rooms = new ArrayList<>();
        rooms.add(kitchen);
        rooms.add(livingRoom);
        rooms.add(kitchen);

        Flat flat = new Flat(rooms);

        Knife knife = new Knife("Нож", kitchen);



        System.out.println("=== НАЧАЛО СЦЕНАРИЯ ===\n");

        Malysh malysh = new Malysh("Малыш", livingRoom);
        Carlson carlson = new Carlson("Карлсон", livingRoom);
        Peach peach = new Peach("Персик", carlson);
        Seed seed = new Seed("Косточка персика", carlson);
        PlantPot pot = new PlantPot(livingRoom, true);
        Plant begonia = new Plant("Бегония");
        pot.planted(begonia);
        Pack pack = new Pack("Пакет", livingRoom);


        try {
        malysh.runTo(kitchen);
        } catch (LocationException e) {
            System.err.println(e.getMessage()); // Выводим сообщение об ошибке
            System.exit(1); // Завершаем программу с кодом ошибки
        }

        try {
        malysh.take(knife, kitchen);
        } catch (LocationException e) {
            System.err.println(e.getMessage()); // Выводим сообщение об ошибке
            System.exit(1); // Завершаем программу с кодом ошибки
        }

        try {
        malysh.walkTo(livingRoom);
        } catch (LocationException e){
            System.err.println(e.getMessage()); // Выводим сообщение об ошибке
            System.exit(1); // Завершаем программу с кодом ошибки
        }

        carlson.hide();
        carlson.eat(peach);
        malysh.hear(SoundType.MUNCH);
        malysh.hear(SoundType.SMACK);
        carlson.unhide();

        try{
        carlson.show(seed, malysh);
        } catch (LocationException e) {
            System.err.println(e.getMessage()); // Выводим сообщение об ошибке
            System.exit(1); // Завершаем программу с кодом ошибки
        }

        carlson.walkToWindow();
        carlson.unPlantedPlant(pot);
        carlson.plantedPlant(seed, pot);
        carlson.changeMood(Mood.ARGUMENTATIVE);
        carlson.makeSounds(SoundType.CHEER);
        carlson.makeSounds(SoundType.CLUCK);
        carlson.changeMood(Mood.HAPPY);

        try{
        carlson.take(pack, livingRoom);
        } catch (LocationException e) {
            System.err.println(e.getMessage()); // Выводим сообщение об ошибке
            System.exit(1); // Завершаем программу с кодом ошибки
        }

        try{
            carlson.runTo(kitchen);
        } catch (LocationException e) {
            System.err.println(e.getMessage()); // Выводим сообщение об ошибке
            System.exit(1); // Завершаем программу с кодом ошибки
        }

        malysh.changeMood(Mood.INTERESTED);
        malysh.chase(carlson);



    }
}
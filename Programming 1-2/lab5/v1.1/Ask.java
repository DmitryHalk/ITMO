package itmo;

import java.time.DateTimeException;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.time.LocalDateTime;
import  java.time.format.DateTimeFormatter;


public class Ask {
    public static class AskBreak extends Exception {}

    public static Person askPerson(Console console) throws AskBreak{
        try{
            String name;
            while (true){
                console.print("name: ");
                name = console.readln().trim();
                if (name.equals("exit")) throw new AskBreak();
                if (!name.isEmpty()) break;
            }

            var coordinates = askCoordinates(console);

            float height;
            while (true) {
                console.print("height: ");
                var line = console.readln().trim();
                if (line.equals("exit")) throw new AskBreak();
                if (!line.equals("")) {
                    try {height = Float.parseFloat(line);
                        if (height <= 0) console.println("Рост должен быть больше 0");
                        else break;
                } catch (NumberFormatException e){}
                }
            }
            LocalDateTime birthday;
            while (true) {
                console.print("data-time (Exemple: " +
                        LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME) + " or 2023-03-11): ");
                var line = console.readln().trim();
                if (line.equals("exit")) throw  new AskBreak();
                if (line.equals("")) {birthday = null; break;}
                try {
                    birthday = LocalDateTime.parse(line, DateTimeFormatter.ISO_DATE_TIME); break;
                } catch (DateTimeParseException e) {}
                try { birthday = LocalDateTime.parse(line+"T00:00:00.0000",DateTimeFormatter.ISO_DATE_TIME);
                    break; } catch (DateTimeParseException e) { }
            }

            var hairColor = askHairColor(console);
            var nationality = askNationality(console);
            var location = askLocation(console);

            console.println("Объект создан");
            return new Person(0, name, coordinates, null, height, birthday, hairColor, nationality, location);

        } catch (NoSuchElementException | IllegalStateException e) {
            console.printError("Ошибка чтения");
            return null;
        }
    }

    public static Coordinates askCoordinates(Console console) throws AskBreak {
        try {
            int x;
            while (true) {
                console.print("coordinates.x: ");
                var line = console.readln().trim();
                if (line.equals("exit")) throw new AskBreak();
                if (!line.equals("")) {
                    try { x = Integer.parseInt(line); break; }catch(NumberFormatException e) { }
                }
            }
            float y;
            while (true) {
                console.print("coordinates.y: ");
                var line = console.readln().trim();
                if (line.equals("exit")) throw new AskBreak();
                if (!line.equals("")) {
                    try { y = Float.parseFloat(line); break; }catch(NumberFormatException e) { }
                }
            }
            return new Coordinates(x, y);
        } catch (NoSuchElementException | IllegalStateException e) {
            console.printError("Ошибка чтения");
            return null;
        }
    }

    public static Color askHairColor(Console console) throws AskBreak {
        try {
            Color color;
            while (true) {
                console.print("hair color: ");
                var line = console.readln().trim();
                if (line.equals("exit")) throw new AskBreak();
                if (line.isEmpty()) return null;
                if (!line.equals("")) {
                    try {
                        color = Color.valueOf(line.toUpperCase(Locale.ROOT));
                        return color;
                    } catch (IllegalArgumentException e) {}
                }
            }

        } catch (NoSuchElementException | IllegalStateException e) {
            console.printError("Ошибка чтения");
            return null;
        }
    }

    public static Country askNationality(Console console) throws AskBreak{
        try{
            Country nationality;
            while (true) {
                console.print("nationality(Country): ");
                var line = console.readln().trim();
                if (line.equals("exit")) throw new AskBreak();
                if (line.isEmpty()) return null;
                if (!line.equals("")) {
                    try{
                        nationality = Country.valueOf(line.toUpperCase());
                        return nationality;
                    } catch (IllegalArgumentException e) {}
                }

            }
        } catch (NoSuchElementException | IllegalStateException e) {
            console.printError("Ошибка чтения");
            return null;
        }
    }

    public static Location askLocation(Console console) throws AskBreak{
        try {
            double x;
            while (true) {
                console.println("Задайте Location:");
                console.print("Введите значение X: ");
                var line = console.readln().trim();
                if (line.equals("exit")) throw new AskBreak();
                if(line.isEmpty()) return null;
                if (!line.isEmpty()) {
                    try {
                        x = Double.parseDouble(line); break;
                    } catch (NumberFormatException e) {}
                }
            }
            double y;
            while (true){
                console.print("Введите значение Y: ");
                var line = console.readln().trim();
                if (line.equals("exit")) throw new AskBreak();
                if (line.isEmpty()) return null;
                if (!line.isEmpty()) {
                    try{
                        y = Double.parseDouble(line); break;
                    } catch (NumberFormatException e) {}
                    }
                }
            int z;
            while (true){
                console.print("Введите значение Z: ");
                var line = console.readln().trim();
                if (line.equals("exit")) throw new AskBreak();
                if (line.isEmpty()) return null;
                if (!line.isEmpty()) {
                    try{
                        z = Integer.parseInt(line); break;
                    } catch (NumberFormatException e) {}
                }
            }

            return new Location(x, y, z);
            } catch (NoSuchElementException | IllegalStateException e) {
            console.printError("Ошибка чтения");
            return null;
            }
        }

    public static Integer askId(Console console) throws AskBreak{
        int id;
        while (true) {
            console.print("Введите id: ");
            var line = console.readln();
            if (line.equals("exit")) throw new AskBreak();
            if (line.isEmpty()) console.println("id не может быть null");
            try{
                id = Integer.parseInt(line);
                return id;
            } catch (NumberFormatException e) {}
        }
    }
    }



package itmo.lab6;

import itmo.lab6.console.Console;
import itmo.lab6.model.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import itmo.lab6.model.*;
import itmo.lab6.enums.*;


public class ScriptExecutor {
    private final Console console;
    private final NetworkClient client;
    private final HashMap<String, TypeArgs> commandTypes;
    private final Set<String> activeScripts = new HashSet<>();
    private String login;
    private String password;

    public ScriptExecutor(Console console, NetworkClient client, HashMap<String, TypeArgs> commandTypes) {
        this.console = console;
        this.client = client;
        this.commandTypes = commandTypes;
    }

    public void setCredentials(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public void execute(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            console.println("Укажите имя файла скрипта.");
            return;
        }
        if (activeScripts.contains(fileName)) {
            console.println("Обнаружена рекурсия! Скрипт " + fileName + " уже выполняется.");
            return;
        }
        activeScripts.add(fileName);
        try (Scanner scriptScanner = new Scanner(new File(fileName))) {
            while (scriptScanner.hasNextLine()) {
                String line = scriptScanner.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] tokens = line.split(" ", 2);
                String cmdName = tokens[0];
                String cmdArg = tokens.length > 1 ? tokens[1] : null;

                if (cmdName.equals("execute_script")) {
                    execute(cmdArg);
                    continue;
                }

                processCommand(cmdName, cmdArg, scriptScanner);
            }
        } catch (FileNotFoundException e) {
            console.println("Файл скрипта не найден: " + fileName);
        } finally {
            activeScripts.remove(fileName);
        }
    }

    private void processCommand(String cmdName, String cmdArg, Scanner scriptScanner) {
        TypeArgs type = commandTypes.get(cmdName);
        if (type == null) {
            console.println("Неизвестная команда в скрипте: " + cmdName);
            return;
        }

        // Валидация аргумента
        if (type == TypeArgs.INTEGER) {
            if (cmdArg == null) {
                console.println("Ошибка в скрипте: команда " + cmdName + " требует число");
                return;
            }
            try {
                Integer.parseInt(cmdArg);
            } catch (NumberFormatException e) {
                console.println("Ошибка в скрипте: аргумент должен быть целым числом");
                return;
            }
        } else if (type == TypeArgs.STRING) {
            if (cmdArg == null || cmdArg.isEmpty()) {
                console.println("Ошибка в скрипте: команда " + cmdName + " требует непустую строку");
                return;
            }
        }

        Person person = null;
        if (type == TypeArgs.PERSON || type == TypeArgs.PERSON_WITH_ID) {
            try {
                person = readPersonFromScanner(scriptScanner);
            } catch (Exception e) {
                console.println("Ошибка чтения Person в скрипте: " + e.getMessage());
                return;
            }
            if (person == null) return;
        }

        Request request = new Request(cmdName, cmdArg, person, login, password);
        Response response = client.send(request);
        if (response != null) {
            console.println(response.getMessage());
        } else {
            console.println("Ошибка связи с сервером при выполнении команды " + cmdName);
        }
    }

    private Person readPersonFromScanner(Scanner scanner) {
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) return null;

        int x = Integer.parseInt(scanner.nextLine().trim());
        float y = Float.parseFloat(scanner.nextLine().trim());
        Coordinates coords = new Coordinates(x, y);

        float height = Float.parseFloat(scanner.nextLine().trim());

        String birthdayStr = scanner.nextLine().trim();
        LocalDateTime birthday = null;
        if (!birthdayStr.isEmpty()) {
            try {
                birthday = LocalDateTime.parse(birthdayStr, DateTimeFormatter.ISO_DATE_TIME);
            } catch (DateTimeParseException e) {
                try {
                    birthday = LocalDateTime.parse(birthdayStr + "T00:00:00", DateTimeFormatter.ISO_DATE_TIME);
                } catch (DateTimeParseException e2) {
                    throw new RuntimeException("Неверный формат даты: " + birthdayStr);
                }
            }
        }

        String hairColorStr = scanner.nextLine().trim();
        Color hairColor = null;
        if (!hairColorStr.isEmpty()) {
            hairColor = Color.valueOf(hairColorStr.toUpperCase());
        }

        String nationalityStr = scanner.nextLine().trim();
        Country nationality = null;
        if (!nationalityStr.isEmpty()) {
            nationality = Country.valueOf(nationalityStr.toUpperCase());
        }

        double locX = Double.parseDouble(scanner.nextLine().trim());
        Double locY = null;
        String yStr = scanner.nextLine().trim();
        if (!yStr.isEmpty()) locY = Double.parseDouble(yStr);
        Integer locZ = null;
        String zStr = scanner.nextLine().trim();
        if (!zStr.isEmpty()) locZ = Integer.parseInt(zStr);
        Location location = new Location(locX, locY, locZ);

        return new Person(0, name, coords, null, height, birthday, hairColor, nationality, location);
    }
}
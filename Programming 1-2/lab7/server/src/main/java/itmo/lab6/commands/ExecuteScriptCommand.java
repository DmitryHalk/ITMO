package itmo.lab6.commands;

import itmo.lab6.Request;
import itmo.lab6.TypeArgs;
import itmo.lab6.Response;
import itmo.lab6.managers.CollectionManager;
import itmo.lab6.managers.CommandManager;
import itmo.lab6.Command;
import itmo.lab6.model.*;
import itmo.lab6.enums.*;


import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.Set;
import java.util.HashSet;

public class ExecuteScriptCommand extends Command {
    private final CommandManager commandManager;
    private static final Set<String> activeScripts = new HashSet<>();

    public ExecuteScriptCommand(CommandManager commandManager) {
        super("execute_script", TypeArgs.STRING);
        this.commandManager = commandManager;
    }

    @Override
    public Response execute(Request request) {
        String fileName = request.getArg();
        String login = request.getLogin();
        String password = request.getPassword();
        if (fileName == null || fileName.isEmpty()) {
            return new Response("Не указано имя файла", false);
        }

        if (activeScripts.contains(fileName)) {
            return new Response("Обнаружена рекурсия! Скрипт " + fileName + " уже выполняется.", false);
        }

        activeScripts.add(fileName);

        StringBuilder result = new StringBuilder();
        try (Scanner scriptScanner = new Scanner(new File(fileName))) {
            while (scriptScanner.hasNextLine()) {
                String line = scriptScanner.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] tokens = line.split(" ", 2);
                String cmdName = tokens[0];
                String arg = tokens.length > 1 ? tokens[1] : null;

                Command cmd = commandManager.getCommand(cmdName);
                if (cmd == null) {
                    result.append("Неизвестная команда в скрипте: ").append(cmdName).append("\n");
                    continue;
                }

                TypeArgs type = cmd.getTypeArg();
                String validatedArg = arg;
                Person person = null;

                try {
                    if (type == TypeArgs.INTEGER) {
                        if (validatedArg == null) {
                            result.append("Ошибка в скрипте: команда ").append(cmdName).append(" требует число\n");
                            continue;
                        }
                        Integer.parseInt(validatedArg);
                    } else if (type == TypeArgs.STRING) {
                        if (validatedArg == null || validatedArg.isEmpty()) {
                            result.append("Ошибка в скрипте: команда ").append(cmdName).append(" требует непустую строку\n");
                            continue;
                        }
                    } else if (type == TypeArgs.PERSON) {
                        person = readPersonFromScanner(scriptScanner);
                        if (person == null) {
                            result.append("Ошибка чтения Person для команды ").append(cmdName).append("\n");
                            continue;
                        }
                    } else if (type == TypeArgs.PERSON_WITH_ID) {
                        if (validatedArg == null) {
                            result.append("Ошибка в скрипте: команда ").append(cmdName).append(" требует id\n");
                            continue;
                        }
                        try {
                            Integer.parseInt(validatedArg);
                        } catch (NumberFormatException e) {
                            result.append("Ошибка в скрипте: id команды ").append(cmdName).append(" должен быть целым числом\n");
                            continue;
                        }
                        person = readPersonFromScanner(scriptScanner);
                        if (person == null) {
                            result.append("Ошибка чтения Person для команды ").append(cmdName).append("\n");
                            continue;
                        }
                    }
                } catch (Exception e) {
                    result.append("Ошибка обработки команды ").append(cmdName).append(": ").append(e.getMessage()).append("\n");
                    continue;
                }


                Request internalRequest = new Request(cmdName, validatedArg, person, login, password);
                try {
                    Response response = cmd.execute(internalRequest);
                    result.append(response.getMessage()).append("\n");
                } catch (Exception e) {
                    result.append("Ошибка выполнения команды ").append(cmdName).append(": ").append(e.getMessage()).append("\n");
                }
            }
        } catch (FileNotFoundException e) {
            return new Response("Файл скрипта не найден: " + fileName, false);
        } finally {
            activeScripts.remove(fileName);
        }

        return new Response(result.toString(), true);
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

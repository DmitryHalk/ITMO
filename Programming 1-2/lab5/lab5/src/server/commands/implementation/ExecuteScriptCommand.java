package server.commands.implementation;

import server.common.Interfaces.Command;
import client.Console.Console;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.HashSet;

public class ExecuteScriptCommand implements Command {
    private Console console;
    private String fileName;
    private final Map<String, Command> commands;
    private static final Set<String> scriptsInProcess = new HashSet<>();

    public ExecuteScriptCommand(Map<String, Command> commands, Console console) {
        this.commands = commands;
        this.console = console;
    }

    @Override
    public void execute(String arg) {
        fileName = arg;
        if (fileName.equals("exit")) {
            console.println("Операция отменена");
            return;
        }

        if (scriptsInProcess.contains(fileName)) {
            console.printError("Обнаружена рекурсия! Скрипт " + fileName + " уже выполняется.");
            return;
        }

        scriptsInProcess.add(fileName);

        try(Scanner fileScanner = new Scanner(new File(fileName))) {
            console.selectFileScanner(fileScanner);
            while (console.isCanReadln()) {
                String line = console.readln();
                if (line == null) break;
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] tokens = line.split(" ");
                String cmdName = tokens[0];
                Command cmd = commands.get(cmdName);
                if (cmd == null) {
                    console.println("Неизвестная команда в скрипте: " + cmdName);
                    continue;
                }

                try {
                    if (tokens.length > 1) {
                        cmd.execute(tokens[1]);
                    } else {
                        cmd.execute();
                    }
                } catch (ExitCommand.ExitException e) {
                    throw e;
                } catch (Exception e) {
                    console.printError("Ошибка при выполнении скрипта: " + e.getMessage());
                }
            }
        } catch (FileNotFoundException e) {

        }finally {
            scriptsInProcess.remove(fileName);
            console.selectConsoleScanner();
        }

    }

    @Override
    public void execute() {
        console.println("Имя файла не может быть пустым");
    }
}

package server.commands.implementation;

import server.common.Interfaces.Command;
import client.Console.Console;
import server.managers.DumpManager;
//import itmo.DumpManager;

public class SaveCommand implements Command {
    private Console console;
    private DumpManager dm;

    public SaveCommand(Console console, DumpManager dm) {
        this.console = console;
        this.dm = dm;
    }

    @Override
    public void execute() {
        if (dm.saveToFile()) {
            console.println("Коллекция сохранена");
        } else {
            console.println("Error: Не удалось сохранить коллекцию");
        }
    }
}

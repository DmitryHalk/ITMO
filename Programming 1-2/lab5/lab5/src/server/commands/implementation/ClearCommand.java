package server.commands.implementation;

import server.managers.CollectionManager;
import server.common.Interfaces.Command;
import client.Console.Console;

public class ClearCommand implements Command {
    private CollectionManager cm;
    private Console console;

    public ClearCommand(Console console, CollectionManager cm){
        this.cm = cm;
        this.console = console;
    }

    @Override
    public void execute() {
        if (cm.getSize() == 0) {
            console.println("Колеекция пуста");
        } else {cm.clear(); console.println("Коллекция очищена");}

    }
}

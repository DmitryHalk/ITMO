package server.commands.implementation;

import server.managers.CollectionManager;
import server.common.Interfaces.Command;
import client.Console.Console;

public class InfoCommand implements Command {
    private Console console;
    private CollectionManager cm;

    public InfoCommand(Console console, CollectionManager cm){
        this.console = console;
        this.cm = cm;
    }

    @Override
    public void execute(){
        console.println("Type: " + cm.getType());
        console.println("Size: " + cm.getSize());

    }
}

package itmo.commands;

import itmo.CollectionManager;
import itmo.Command;
import itmo.Console;
import itmo.Person;

import java.util.HashSet;

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

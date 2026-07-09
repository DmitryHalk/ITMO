package itmo.commands;

import itmo.CollectionManager;
import itmo.Command;
import itmo.Console;
import itmo.Person;

import java.util.HashSet;

public class ShowCommand implements Command {
    private Console console;
    private CollectionManager cm;

    public ShowCommand(Console console, CollectionManager cm){
        this.console = console;
        this.cm = cm;
    }

    @Override
    public void execute() {
        if (cm.getSize() == 0) {console.println("Коллекция пуста");}
        else {
            for (Person p : cm.getSet()) {
                console.println(p.toString());
            }
        }
    }
}

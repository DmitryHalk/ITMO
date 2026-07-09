package server.commands.implementation;

import server.managers.CollectionManager;
import server.common.Interfaces.Command;
import client.Console.Console;
import server.common.Clases.Person;

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

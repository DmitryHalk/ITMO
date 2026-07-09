package server.commands.implementation;

import server.managers.CollectionManager;
import server.common.Interfaces.Command;
import client.Console.Console;
import server.common.Clases.Person;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PrintAscendingCommand implements Command {
    private Console console;
    private CollectionManager cm;

    public PrintAscendingCommand(Console console, CollectionManager cm) {
        this.console = console;
        this.cm = cm;
    }

    @Override
    public void execute() {
        var collection = cm.getSet();
        if (collection.isEmpty()) {
            console.println("Коллекция пуста");
            return;
        }

        List<Person> sortedList = new ArrayList<>(collection);
        Collections.sort(sortedList);

        console.println("Элементы коллекции в порядке возрастания: ");
         for (Person p: sortedList) {
             console.println(p.toString());
         }
    }
}

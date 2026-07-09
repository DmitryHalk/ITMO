package server.commands.implementation;

import server.managers.CollectionManager;
import server.common.Interfaces.Command;
import client.Console.Console;
import server.common.Clases.Person;

public class FilterCommand implements Command {
    private Console console;
    private CollectionManager cm;

    public FilterCommand(Console console, CollectionManager cm) {
        this.console = console;
        this.cm = cm;
    }

    @Override
    public void execute(String arg) {
        int c = 0;
        for (Person p : cm.getSet()) {
            if (p.getName().startsWith(arg)) {
                console.println(p.toString());
                c += 1;
            }
        }
        if (c == 0) console.println("Объектов с именем начинающихся на \"" + arg + "\" не найдено");
        else console.println("Выведены все элементы начинающиеся с " + arg );
    }

    @Override
    public void execute(){
        console.println("Поле не может быть пустым");
    }
}

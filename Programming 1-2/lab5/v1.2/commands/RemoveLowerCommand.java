package itmo.commands;

import itmo.CollectionManager;
import itmo.Command;
import itmo.Console;
import itmo.Person;

import java.util.HashSet;

public class RemoveLowerCommand implements Command {
   private Console console;
   private CollectionManager cm;

    public RemoveLowerCommand(Console console, CollectionManager cm) {
        this.console = console;
        this.cm = cm;
    }


    @Override
    public void execute(String arg) {
        int id = Integer.parseInt(arg);
        if (cm.getContainsId(id)) {
            HashSet<Integer> toRemove = new HashSet<>();
            for (Person p: cm.getSet()) {
                if (p.getId() < id) toRemove.add(p.getId());
            }
            int c = 0;
            for (Integer i : toRemove) {
                cm.removeById(i);
                c += 1;
            }
            console.println("Удаление завершено, удалено " + c + " объектов");
        } else {
            console.println("Объект не найден");
        }
    }

    @Override
    public void execute() {
        console.println("Не введён id");
    }
}

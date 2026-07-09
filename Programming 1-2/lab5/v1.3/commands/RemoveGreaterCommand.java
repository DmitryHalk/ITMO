package itmo.commands;

import itmo.*;

import java.util.HashSet;

public class RemoveGreaterCommand implements Command {
    private Console console;
    private CollectionManager cm;

    public RemoveGreaterCommand(Console console, CollectionManager cm) {
        this.console = console;
        this.cm = cm;
    }

    @Override
    public void execute() {
        try{
            Person p = Ask.askPerson(console);
            HashSet<Person> toRemove = new HashSet<>();
            for (Person i : cm.getSet()) {
                if (p.compareTo(i) < 0) {
                    toRemove.add(i);
                }
            }
            int c = 0;
            for (Person i : toRemove) {
                cm.removeById(i.getId());
                c += 1;
            }
            console.println("Удаление завершено, удалено " + c + " объектов");
        } catch (Ask.AskBreak e) {}
    }
}

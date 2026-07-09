package itmo.commands;

import itmo.*;

public class AddIfMinCommand implements Command {
    private Console console;
    private CollectionManager cm;

    public AddIfMinCommand(Console console, CollectionManager cm) {
        this.console = console;
        this.cm = cm;
    }



    @Override
    public void execute() {
        try {
            Person p = Ask.askPerson(console);
            if (cm.getMin() == null) {cm.add(p);
            return;
            }
            if (p.compareTo(cm.getMin()) < 0) {
                cm.add(p);
                console.println("Объект создан");
            } else console.println("Объект больше минимального");
        } catch (Ask.AskBreak e) {}


    }
}

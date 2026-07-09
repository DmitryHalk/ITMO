package itmo.commands;

import itmo.Ask;
import itmo.CollectionManager;
import itmo.Command;
import itmo.Console;
import itmo.Person;

public class UpdateIDCommand implements Command {
    private Console console;
    private CollectionManager cm;

    public UpdateIDCommand(Console console, CollectionManager cm) {
        this.console = console;
        this.cm = cm;
    }

    @Override
    public void execute(String arg) {
        try{
            int id = Integer.parseInt(arg);
            if (!cm.getContainsId(id)) {
                console.println("id не найден");
                return;
            }
            Person p = Ask.askPerson(console);
            cm.add(p, id);
        } catch (Ask.AskBreak e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void execute(){
        console.println("Не введён id");
    }
}

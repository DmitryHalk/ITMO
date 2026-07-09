package itmo.commands;

import itmo.CollectionManager;
import itmo.Command;
import itmo.Console;
import itmo.Ask;

public class RemoveByIDCommand implements Command {
    private Console console;
    private  CollectionManager cm;


    public RemoveByIDCommand(Console console, CollectionManager cm) {
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
            cm.removeById(id);
            console.println("Объект с id = " + id + " удалён");
        }catch (Exception e) {
            console.println("Error: " + e.getMessage());
        }
    }

    @Override
    public void execute(){
        console.println("Не введён id");
    }
}

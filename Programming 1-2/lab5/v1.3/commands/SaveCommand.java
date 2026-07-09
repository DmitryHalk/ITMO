package itmo.commands;

import itmo.CollectionManager;
import itmo.Command;
import itmo.Console;

public class SaveCommand implements Command {
    private Console console;
    private CollectionManager cm;

    public SaveCommand(Console console, CollectionManager cm) {
        this.console = console;
        this.cm = cm;
    }

    @Override
    public void execute() {
        if (cm.saveToFile()) {
            console.println("Коллекция сохранена");
        } else {
            console.println("Error: Не удалось сохранить коллекцию");
        }
    }
}

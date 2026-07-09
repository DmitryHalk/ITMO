package itmo.commands;

import itmo.Command;
import itmo.Console;
import itmo.Person;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class HelpCommand implements Command {
    private Map<String, Command> commands;
    private Console console;


    public HelpCommand(Console console, Map<String, Command> commands) {
        this.commands = commands;
        this.console = console;
    }

    @Override
    public void execute(){
        console.println("Список доступных команд:");
        for (String name : commands.keySet()){
            console.println(" " + name);
        }
    }
}

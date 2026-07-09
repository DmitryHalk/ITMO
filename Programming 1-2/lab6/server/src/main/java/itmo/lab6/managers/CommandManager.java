package itmo.lab6.managers;

import itmo.lab6.TypeArgs;
import itmo.lab6.Command;

import java.util.HashMap;

public class CommandManager {
    private final HashMap<String, Command> commands = new HashMap<>();

    public Command getCommand(String name) {
        return commands.get(name);
    }

    public void register(String name, Command command) {
        commands.put(name, command);
    }

    public HashMap<String, Command> getCommands() {
        return commands;
    }

    public HashMap<String, TypeArgs> getCommandAttribute(){
        HashMap<String, TypeArgs> map = new HashMap<>();
        for (Command cmd : commands.values()) {
            map.put(cmd.getName(), cmd.getTypeArg());
        }
        return map;
    }
}

package itmo.lab6.commands;

import itmo.lab6.Request;
import itmo.lab6.TypeArgs;
import itmo.lab6.Response;
import itmo.lab6.managers.CommandManager;
import itmo.lab6.Command;

import java.util.stream.Collectors;


public class HelpCommand extends Command {
    private final CommandManager cm;


    public HelpCommand(CommandManager cm) {
        super("help", TypeArgs.NONE);
        this.cm = cm;
    }

    @Override
    public Response execute(Request request){

        String res = cm.getCommands().keySet().stream().map(name -> " - " + name).collect(Collectors.joining("\n", "Список доступных команд:\n", ""));

        return new Response(res, true);
    }
}

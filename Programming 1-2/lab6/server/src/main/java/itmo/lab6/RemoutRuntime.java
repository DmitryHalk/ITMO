package itmo.lab6;


import java.util.HashMap;

import itmo.lab6.Request;
import itmo.lab6.Response;
import itmo.lab6.model.Person;
import itmo.lab6.Command;
import itmo.lab6.interfaces.Runtime;
import itmo.lab6.TypeArgs;
import itmo.lab6.managers.*;
import itmo.lab6.commands.*;

public class RemoutRuntime implements Runtime {
    private CollectionManager cm;
    private CommandManager commands;
    private DumpManager dm;

    public RemoutRuntime(CollectionManager cm, DumpManager dm) {
        this.dm = dm;
        this.cm = cm;
        this.commands = new CommandManager();
        registerCommand();
    }

    @Override
    public void run() {
    }

    public Response execute(Request request){
        String name = request.getName();
        Command command = commands.getCommand(name);

        if (command == null) return new Response("Неизвестная команда: " + name, false);
        try {
            return command.execute(request);
        } catch (Exception e) {
            return new Response("Ошибка выполнения команды: " + name, false);
        }
    }

    public void registerCommand() {
        commands.register("help", new HelpCommand(commands));
        commands.register("info", new InfoCommand(cm));
        commands.register("show", new ShowCommand(cm));
        commands.register("add", new AddCommand(cm));
        commands.register("update_id", new UpdateIDCommand(cm));
        commands.register("remove_by_id", new RemoveByIDCommand(cm));
        commands.register("clear", new ClearCommand(cm));
        commands.register("add_if_min", new AddIfMinCommand(cm));
        commands.register("remove_greater", new RemoveGreaterCommand(cm));
        commands.register("remove_lower", new RemoveLowerCommand(cm));
        commands.register("filter_starts_with_name", new FilterCommand(cm));
        commands.register("print_ascending", new PrintAscendingCommand(cm));
        commands.register("print_descending", new PrintDescendingCommand(cm));
//        commands.register("save", new SaveCommand(dm)); // Убрать
        commands.register("execute_script", new ExecuteScriptCommand(commands));
    }


    public HashMap<String, TypeArgs> getCommandsAttribute() {
        return commands.getCommandAttribute();
    }
}

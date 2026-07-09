package itmo;

import itmo.commands.*;
import java.util.*;

public class Invoker {
    private CollectionManager collectionManager = new CollectionManager();
    private Console console = new MainConsole();
    private Person p;

    public Invoker(Console console){
        this.console = console;
    }

    public void run(){

    Map<String, Command> commands = new HashMap<>();
    commands.put("help", new HelpCommand(console, commands));
    commands.put("info", new InfoCommand(console, collectionManager));
    commands.put("show", new ShowCommand(console, collectionManager));
    commands.put("add", new AddCommand(console, collectionManager));
    commands.put("update_id", new UpdateIDCommand(console, collectionManager));
    commands.put("remove_by_id", new RemoveByIDCommand(console, collectionManager));
    commands.put("clear", new ClearCommand(console, collectionManager));
    commands.put("save", new SaveCommand());
    commands.put("execute_script", new ExecuteScriptCommand());
    commands.put("exit", new ExitCommand());
    commands.put("add_if_min", new AddIfMinCommand(console, collectionManager));
    commands.put("remove_greater", new RemoveGreaterCommand(console, collectionManager));
    commands.put("remove_lower", new RemoveLowerCommand(console, collectionManager));
    commands.put("filter_starts_with_name", new FilterCommand());
    commands.put("print_ascending", new PrintAscendingCommand(console, collectionManager));
    commands.put("print_descending", new PrintDescendingCommand(console, collectionManager));






    Scanner sc = new Scanner(System.in);
    while (sc.hasNextLine()) {
        String line = sc.nextLine().trim();
        if (line.isEmpty()) continue;
        String[] tokens = line.split(" ");
        Command command = commands.get(tokens[0]);
        if (command == null) { console.println("Неизвестная команда"); continue; }
        try {
            String args = tokens.length > 1 ? tokens[1] : null;
            if (args == null) { command.execute(); continue;}
            command.execute(tokens[1]);
        } catch (ExitCommand.ExitException e) {
            console.println("Выход из программы...");
            break;
        } catch (Exception e) {
            console.println("Error: " + e.getMessage());
        }

    }

    }

}

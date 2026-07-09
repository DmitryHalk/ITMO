package itmo.lab6;

import java.util.Set;

import itmo.lab6.managers.*;
import itmo.lab6.commands.*;

public class RemoutRuntime {
    private final CollectionManager cm;
    private final PersonDao personDao;
    private final UserManager um;
    private final CommandManager commands;

    // Команды, не требующие авторизации
    private static final Set<String> PUBLIC_COMMANDS =
            Set.of("register", "login", "get_commands");

    public RemoutRuntime(CollectionManager cm, PersonDao personDao, UserManager um) {
        this.cm = cm;
        this.personDao = personDao;
        this.um = um;
        this.commands = new CommandManager();
        registerCommands();
    }

    public Response execute(Request request) {
        String name = request.getName();
        Command command = commands.getCommand(name);
        if (command == null) return new Response("Неизвестная команда: " + name, false);

        // Проверка авторизации для всех команд, кроме публичных
        if (!PUBLIC_COMMANDS.contains(name)) {
            String login = request.getLogin();
            String password = request.getPassword();
            if (login == null || password == null)
                return new Response("Требуется авторизация", false);
            if (!um.authenticate(login, password))
                return new Response("Неверный логин или пароль", false);
        }

        try {
            return command.execute(request);
        } catch (Exception e) {
            return new Response("Ошибка выполнения команды: " + e.getMessage(), false);
        }
    }

    private void registerCommands() {
        // Публичные команды (без авторизации)
        commands.register("register", new RegisterCommand(um));
        commands.register("login",    new LoginCommand(um));

        // Команды чтения (только авторизованные)
        commands.register("help",                     new HelpCommand(commands));
        commands.register("info",                     new InfoCommand(cm));
        commands.register("show",                     new ShowCommand(cm));
        commands.register("filter_starts_with_name",  new FilterCommand(cm));
        commands.register("print_ascending",          new PrintAscendingCommand(cm));
        commands.register("print_descending",         new PrintDescendingCommand(cm));
        commands.register("execute_script",           new ExecuteScriptCommand(commands));

        // Команды изменения коллекции (авторизованные + проверка владельца)
        commands.register("add",            new AddCommand(cm, personDao));
        commands.register("update_id",      new UpdateIDCommand(cm, personDao));
        commands.register("remove_by_id",   new RemoveByIDCommand(cm, personDao));
        commands.register("clear",          new ClearCommand(cm, personDao));
        commands.register("add_if_min",     new AddIfMinCommand(cm, personDao));
        commands.register("remove_greater", new RemoveGreaterCommand(cm, personDao));
        commands.register("remove_lower",   new RemoveLowerCommand(cm, personDao));
    }

    public java.util.HashMap<String, TypeArgs> getCommandsAttribute() {
        return commands.getCommandAttribute();
    }
}

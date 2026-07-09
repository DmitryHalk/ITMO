package itmo.lab6.commands;

import itmo.lab6.*;
import itmo.lab6.managers.UserManager;

public class RegisterCommand extends Command {
    private final UserManager um;

    public RegisterCommand(UserManager um) {
        super("register", TypeArgs.NONE);
        this.um = um;
    }

    @Override
    public Response execute(Request request) {
        String login = request.getLogin();
        String password = request.getPassword();
        if (login == null || login.isBlank())
            return new Response("Логин не может быть пустым", false);
        if (password == null || password.isEmpty())
            return new Response("Пароль не может быть пустым", false);
        if (um.register(login, password))
            return new Response("Пользователь '" + login + "' зарегистрирован", true);
        else
            return new Response("Логин '" + login + "' уже занят", false);
    }
}

package itmo.lab6.commands;

import itmo.lab6.*;
import itmo.lab6.managers.UserManager;

public class LoginCommand extends Command {
    private final UserManager um;

    public LoginCommand(UserManager um) {
        super("login", TypeArgs.NONE);
        this.um = um;
    }

    @Override
    public Response execute(Request request) {
        String login = request.getLogin();
        String password = request.getPassword();
        if (um.authenticate(login, password))
            return new Response("Добро пожаловать, " + login + "!", true);
        else
            return new Response("Неверный логин или пароль", false);
    }
}

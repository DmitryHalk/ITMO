package itmo.commands;

import itmo.Command;

public class ExitCommand implements Command {
    public static  class ExitException extends RuntimeException {}

    @Override
    public void execute() {
        throw new ExitException();
    }
}

package itmo.lab6.commands;

import itmo.lab6.Request;
import itmo.lab6.TypeArgs;
import itmo.lab6.Command;
import itmo.lab6.Response;
import itmo.lab6.managers.DumpManager;

public class SaveCommand extends Command {
    private DumpManager dm;

    public SaveCommand(DumpManager dm) {
        super("save", TypeArgs.NONE);
        this.dm = dm;
    }

    @Override
    public Response execute(Request request) {
        if (dm.saveToFile()) {
            return new Response("Коллекция сохранена", true);
        } else {
            return new Response("Error: Не удалось сохранить коллекцию", false);
        }
    }
}

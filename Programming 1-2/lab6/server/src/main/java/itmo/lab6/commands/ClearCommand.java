package itmo.lab6.commands;

import itmo.lab6.Request;
import itmo.lab6.TypeArgs;
import itmo.lab6.Response;
import itmo.lab6.managers.CollectionManager;
import itmo.lab6.Command;

public class ClearCommand extends Command {
    private final CollectionManager cm;

    public ClearCommand(CollectionManager cm){
        super("clear", TypeArgs.NONE);
        this.cm = cm;
    }

    @Override
    public Response execute(Request request) {
        if (cm.getSize() == 0) {
            return new Response("Коллекция пуста", true);
        } else {cm.clear(); return new Response("Коллекция очищена", true);}

    }
}

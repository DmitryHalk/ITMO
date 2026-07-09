package itmo.lab6.commands;

import itmo.lab6.Request;
import itmo.lab6.TypeArgs;
import itmo.lab6.Command;
import itmo.lab6.Response;
import itmo.lab6.managers.CollectionManager;

public class InfoCommand extends Command {
    private CollectionManager cm;

    public InfoCommand(CollectionManager cm){
        super("info", TypeArgs.NONE);
        this.cm = cm;
    }

    @Override
    public Response execute(Request request){
        String result = "Type: " + cm.getType() + "\nSize: " + cm.getSize();
        return new Response(result, true);
    }
}

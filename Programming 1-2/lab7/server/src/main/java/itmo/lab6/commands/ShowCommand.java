package itmo.lab6.commands;

import itmo.lab6.Request;
import itmo.lab6.TypeArgs;
import itmo.lab6.Command;
import itmo.lab6.Response;
import itmo.lab6.managers.CollectionManager;
import itmo.lab6.model.Person;

import java.util.Comparator;
import java.util.stream.Collectors;

public class ShowCommand extends Command {
    private final CollectionManager cm;

    public ShowCommand(CollectionManager cm){
        super("show", TypeArgs.NONE);
        this.cm = cm;
    }

    @Override
    public Response execute(Request request) {
        if (cm.getSize() == 0) {return new Response("Коллекция пуста", true);}
        else {
            String res = cm.getSet().stream().sorted(Comparator.comparing(Person::getName)).map(Person::toString).collect(Collectors.joining("\n"));
            return new Response(res, true);
        }
    }
}


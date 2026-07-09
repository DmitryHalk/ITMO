package itmo.lab6.commands;

import itmo.lab6.Request;
import itmo.lab6.TypeArgs;
import itmo.lab6.Command;
import itmo.lab6.Response;
import itmo.lab6.managers.CollectionManager;
import itmo.lab6.model.Person;

import java.util.Comparator;
import java.util.HashMap;
import java.util.stream.Collectors;

public class FilterCommand extends Command {
    private final CollectionManager cm;

    public FilterCommand(CollectionManager cm) {
        super("filter_starts_with_name", TypeArgs.NONE);
        this.cm = cm;
    }

    @Override
    public Response execute(Request request) {

        String res = cm.getSet().stream().filter(p -> p.getName().startsWith(request.getArg())).sorted(Comparator.comparing(Person::getName)).map(Person::toString).collect(Collectors.joining("\n"));

        if (res.isEmpty()) {
            return new Response("Объектов с именем, начинающихся на \"" + request.getArg() + "\", не найдено", true);
        } else {
            return new Response(res + "\nВыведены все элементы, начинающиеся с " + request.getArg(), true);
        }
    }
}

package itmo.lab6.commands;

import itmo.lab6.Request;
import itmo.lab6.TypeArgs;
import itmo.lab6.Command;
import itmo.lab6.Response;
import itmo.lab6.managers.CollectionManager;
import itmo.lab6.model.Person;
import java.util.Comparator;
import java.util.stream.Collectors;

public class PrintDescendingCommand extends Command {
    private final CollectionManager cm;

    public PrintDescendingCommand(CollectionManager cm) {
        super("print_descending", TypeArgs.NONE);
        this.cm = cm;
    }

    @Override
    public Response execute(Request request) {

        if (cm.getSet().isEmpty()) {
            return new Response("Коллекция пуста", true);
        }

        String res = cm.getSet().stream().sorted(Comparator.reverseOrder()).map(Person::toString).collect(Collectors.joining("\n", "Элементы коллекции в порядке убывания:\n", ""));

        return new Response(res, true);

    }
}

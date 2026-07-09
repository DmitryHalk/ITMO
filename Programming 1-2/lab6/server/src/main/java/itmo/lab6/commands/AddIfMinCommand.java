package itmo.lab6.commands;

import itmo.lab6.Request;
import itmo.lab6.TypeArgs;
import itmo.lab6.Response;
import itmo.lab6.managers.CollectionManager;
import itmo.lab6.Command;
import itmo.lab6.model.Person;

import java.util.Comparator;

public class AddIfMinCommand extends Command {
    private final CollectionManager cm;

    public AddIfMinCommand(CollectionManager cm) {
        super("add_if_min", TypeArgs.PERSON);
        this.cm = cm;
    }


    @Override
    public Response execute(Request request) {
        try {
            Person p = request.getEntity();
            if (p == null) return new Response("Не передан объект для добавления", false);

            Person minPerson = cm.getSet().stream().min(Comparator.naturalOrder()).orElse(null);

            if (minPerson == null) {
                cm.add(p);
                return new Response("Объект добавлен", true);
            }

            if (p.compareTo(minPerson) < 0) {
                cm.add(p);
                return new Response("Объект создан", true);
            } else {
                return new Response("Объект больше минимального", false);
            }
        } catch (Exception e) {
            return new Response("Error: " + e.getMessage(), false);
        }
    }
}

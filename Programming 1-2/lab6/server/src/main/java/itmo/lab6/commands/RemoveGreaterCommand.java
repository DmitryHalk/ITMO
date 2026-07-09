package itmo.lab6.commands;

import itmo.lab6.Request;
import itmo.lab6.model.Person;
import itmo.lab6.TypeArgs;
import itmo.lab6.Command;
import itmo.lab6.Response;
import itmo.lab6.managers.CollectionManager;

import java.util.Set;
import java.util.stream.Collectors;

public class RemoveGreaterCommand extends Command {
    private final CollectionManager cm;

    public RemoveGreaterCommand(CollectionManager cm) {
        super("remove_greater", TypeArgs.PERSON);
        this.cm = cm;
    }

    @Override
    public Response execute(Request request) {
        try{
            Person p = request.getEntity();

            if (p == null) {
                return new Response("Не передан объект для сравнения", false);
            }

            Set<Person> toRemove = cm.getSet().stream().filter(i -> p.compareTo(i) < 0).collect(Collectors.toSet());
            toRemove.forEach(i -> cm.removeById(i.getId()));

            return new Response("Удаление завершено, удалено " + toRemove.size() + " объектов", true);

        } catch (Exception e) {
            return new Response("Ошибка при выполнении команды: " + e.getMessage(), false);
        }
    }
}

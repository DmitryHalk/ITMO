package itmo.lab6.commands;

import itmo.lab6.*;
import itmo.lab6.managers.CollectionManager;
import itmo.lab6.managers.PersonDao;
import itmo.lab6.model.Person;

import java.util.List;
import java.util.stream.Collectors;

public class RemoveLowerCommand extends Command {
    private final CollectionManager cm;
    private final PersonDao personDao;

    public RemoveLowerCommand(CollectionManager cm, PersonDao personDao) {
        super("remove_lower", TypeArgs.PERSON);
        this.cm = cm;
        this.personDao = personDao;
    }

    @Override
    public Response execute(Request request) {
        try {
            Person ref = request.getEntity();
            if (ref == null) return new Response("Не передан объект", false);
            String owner = request.getLogin();

            List<Person> toRemove = cm.getSet().stream()
                    .filter(p -> owner.equals(p.getOwnerLogin()) && p.compareTo(ref) < 0)
                    .collect(Collectors.toList());

            int removed = 0;
            for (Person p : toRemove) {
                if (personDao.deleteById(p.getId(), owner)) {
                    cm.removeById(p.getId(), owner);
                    removed++;
                }
            }
            return new Response("Удалено " + removed + " объектов", true);
        } catch (Exception e) {
            return new Response("Ошибка: " + e.getMessage(), false);
        }
    }
}

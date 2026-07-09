package itmo.lab6.commands;

import itmo.lab6.*;
import itmo.lab6.managers.CollectionManager;
import itmo.lab6.managers.PersonDao;
import itmo.lab6.model.Person;

import java.util.Date;

public class AddIfMinCommand extends Command {
    private final CollectionManager cm;
    private final PersonDao personDao;

    public AddIfMinCommand(CollectionManager cm, PersonDao personDao) {
        super("add_if_min", TypeArgs.PERSON);
        this.cm = cm;
        this.personDao = personDao;
    }

    @Override
    public Response execute(Request request) {
        try {
            Person p = request.getEntity();
            if (p == null) return new Response("Не передан объект", false);

            String owner = request.getLogin();

            Person min = cm.getMin();
            if (min != null && p.compareTo(min) >= 0) {
                return new Response("Объект не является минимальным, не добавлен", false);
            }

            p.setCreationDate(new Date());
            p.setOwnerLogin(owner);

            int generatedId = personDao.insert(p);
            if (generatedId < 0) return new Response("Ошибка сохранения в БД", false);

            p.setId(generatedId);
            cm.addWithId(p);
            return new Response("Объект добавлен с id=" + generatedId, true);
        } catch (Exception e) {
            return new Response("Ошибка: " + e.getMessage(), false);
        }
    }
}

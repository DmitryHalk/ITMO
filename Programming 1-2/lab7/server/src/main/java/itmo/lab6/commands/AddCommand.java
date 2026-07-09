package itmo.lab6.commands;

import itmo.lab6.*;
import itmo.lab6.managers.CollectionManager;
import itmo.lab6.managers.PersonDao;
import itmo.lab6.model.Person;

import java.util.Date;

public class AddCommand extends Command {
    private final CollectionManager cm;
    private final PersonDao personDao;

    public AddCommand(CollectionManager cm, PersonDao personDao) {
        super("add", TypeArgs.PERSON);
        this.cm = cm;
        this.personDao = personDao;
    }

    @Override
    public Response execute(Request request) {
        try {
            Person p = request.getEntity();
            if (p == null) return new Response("Не передан объект", false);

            String owner = request.getLogin();
            p.setCreationDate(new Date());
            p.setOwnerLogin(owner);

            // Сначала БД, потом память
            int generatedId = personDao.insert(p);
            if (generatedId < 0) {
                return new Response("Ошибка сохранения в БД", false);
            }

            p.setId(generatedId);
            cm.addWithId(p);
            return new Response("Объект добавлен с id=" + generatedId, true);
        } catch (Exception e) {
            return new Response("Ошибка: " + e.getMessage(), false);
        }
    }
}

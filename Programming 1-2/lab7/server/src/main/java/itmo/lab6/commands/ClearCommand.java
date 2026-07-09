package itmo.lab6.commands;

import itmo.lab6.*;
import itmo.lab6.managers.CollectionManager;
import itmo.lab6.managers.PersonDao;

public class ClearCommand extends Command {
    private final CollectionManager cm;
    private final PersonDao personDao;

    public ClearCommand(CollectionManager cm, PersonDao personDao) {
        super("clear", TypeArgs.NONE);
        this.cm = cm;
        this.personDao = personDao;
    }

    @Override
    public Response execute(Request request) {
        String owner = request.getLogin();

        int dbDeleted = personDao.deleteAllByOwner(owner);
        if (dbDeleted < 0) {
            return new Response("Ошибка удаления объектов из БД", false);
        }

        int memDeleted = cm.clearByOwner(owner);
        return new Response("Удалено " + memDeleted + " ваших объектов", true);
    }
}

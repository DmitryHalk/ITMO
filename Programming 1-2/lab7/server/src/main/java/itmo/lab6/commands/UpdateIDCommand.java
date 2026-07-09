package itmo.lab6.commands;

import itmo.lab6.*;
import itmo.lab6.managers.CollectionManager;
import itmo.lab6.managers.PersonDao;
import itmo.lab6.model.Person;

public class UpdateIDCommand extends Command {
    private final CollectionManager cm;
    private final PersonDao personDao;

    public UpdateIDCommand(CollectionManager cm, PersonDao personDao) {
        super("update_id", TypeArgs.PERSON_WITH_ID);
        this.cm = cm;
        this.personDao = personDao;
    }

    @Override
    public Response execute(Request request) {
        String arg = request.getArg();
        if (arg == null || arg.isEmpty()) return new Response("Не указан id", false);
        try {
            int id = Integer.parseInt(arg);
            String owner = request.getLogin();

            if (!cm.containsId(id)) return new Response("id не найден", false);

            Person existing = cm.findById(id);
            if (!owner.equals(existing.getOwnerLogin())) {
                return new Response("Нет прав: объект принадлежит другому пользователю", false);
            }

            Person p = request.getEntity();
            if (p == null) return new Response("Не передан объект", false);

            boolean dbOk = personDao.update(id, p, owner);
            if (!dbOk) return new Response("Ошибка обновления в БД", false);

            cm.updateById(id, p, owner);
            return new Response("Объект с id=" + id + " обновлён", true);
        } catch (NumberFormatException e) {
            return new Response("id должен быть целым числом", false);
        } catch (Exception e) {
            return new Response("Ошибка: " + e.getMessage(), false);
        }
    }
}

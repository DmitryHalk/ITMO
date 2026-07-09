package itmo.lab6.commands;

import itmo.lab6.*;
import itmo.lab6.managers.CollectionManager;
import itmo.lab6.managers.PersonDao;
import itmo.lab6.model.Person;

public class RemoveByIDCommand extends Command {
    private final CollectionManager cm;
    private final PersonDao personDao;

    public RemoveByIDCommand(CollectionManager cm, PersonDao personDao) {
        super("remove_by_id", TypeArgs.INTEGER);
        this.cm = cm;
        this.personDao = personDao;
    }

    @Override
    public Response execute(Request request) {
        try {
            int id = Integer.parseInt(request.getArg());
            String owner = request.getLogin();

            if (!cm.containsId(id)) return new Response("id не найден", false);

            Person p = cm.findById(id);
            if (!owner.equals(p.getOwnerLogin())) {
                return new Response("Нет прав: объект принадлежит другому пользователю", false);
            }

            boolean dbOk = personDao.deleteById(id, owner);
            if (!dbOk) return new Response("Ошибка удаления из БД", false);

            cm.removeById(id, owner);
            return new Response("Объект с id=" + id + " удалён", true);
        } catch (NumberFormatException e) {
            return new Response("id должен быть целым числом", false);
        } catch (Exception e) {
            return new Response("Ошибка: " + e.getMessage(), false);
        }
    }
}

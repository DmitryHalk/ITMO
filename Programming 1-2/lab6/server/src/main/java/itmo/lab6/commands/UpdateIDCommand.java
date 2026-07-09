package itmo.lab6.commands;

import itmo.lab6.TypeArgs;
import itmo.lab6.Command;
import itmo.lab6.Response;
import itmo.lab6.managers.CollectionManager;
import itmo.lab6.model.Person;
import itmo.lab6.Request;

public class UpdateIDCommand extends Command {
    private final CollectionManager cm;

    public UpdateIDCommand(CollectionManager cm) {
        super("update_id", TypeArgs.PERSON_WITH_ID);
        this.cm = cm;
    }

    @Override
    public Response execute(Request request) {
        String arg = request.getArg();
        if (arg.isEmpty() || arg == null) return new Response("Не указан id", false);
        try{
            int id = Integer.parseInt(request.getArg());
            if (!cm.getContainsId(id)) {
                return new Response("id не найден", false);
            }
            Person p = request.getEntity();
            cm.add(p, id);
            return new Response("Объект с id " + id + " обновлён", true);
        } catch (NumberFormatException e) {
            return new Response("id должно быть целым числом", false);
        } catch (Exception e) {
            return new Response("Ошибка при обновлении: " + e.getMessage(), false);
        }
    }
}

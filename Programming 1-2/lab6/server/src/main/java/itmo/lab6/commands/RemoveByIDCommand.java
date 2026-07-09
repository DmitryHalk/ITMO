package itmo.lab6.commands;

import itmo.lab6.Request;
import itmo.lab6.TypeArgs;
import itmo.lab6.Command;
import itmo.lab6.Response;
import itmo.lab6.managers.CollectionManager;

public class RemoveByIDCommand extends Command {
    private  CollectionManager cm;


    public RemoveByIDCommand(CollectionManager cm) {
        super("remove_by_id", TypeArgs.INTEGER);
        this.cm = cm;
    }

    @Override
    public Response execute(Request request) {
        try{
            int id = Integer.parseInt(request.getArg());
            if (!cm.getContainsId(id)) {
                return new Response("id не найден", false);
            }
            cm.removeById(id);
            return new Response("Объект с id = " + id + " удалён", true);
        }catch (NumberFormatException e) {
            return new Response("Error: id должен быть целым числом", false);
        }catch (Exception e) {
            return new Response("Error: " + e.getMessage(), false);
        }
    }
}

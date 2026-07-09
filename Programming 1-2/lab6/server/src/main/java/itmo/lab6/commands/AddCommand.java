package itmo.lab6.commands;

import itmo.lab6.Request;
import itmo.lab6.TypeArgs;
import itmo.lab6.Response;
import itmo.lab6.managers.CollectionManager;
import itmo.lab6.Command;



public class AddCommand extends Command {
   private final CollectionManager cm;


   public AddCommand(CollectionManager cm){
       super("add", TypeArgs.PERSON);
       this.cm = cm;
   }

   @Override
   public Response execute(Request request) {
       try{
           cm.add(request.getEntity());
           return new Response("Объект создан", true);
       } catch (Exception e) {
           return new Response("Ошибка" + e.getMessage(), false);
       }
   }

}

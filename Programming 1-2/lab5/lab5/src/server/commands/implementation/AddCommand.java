package server.commands.implementation;

import server.managers.CollectionManager;
import server.common.Interfaces.Command;
import client.Console.Console;
import server.common.Clases.Person;
import client.Console.Ask;

public class AddCommand implements Command {
   private final CollectionManager cm;
   private Console console;


   public AddCommand(Console console, CollectionManager cm){
       this.cm = cm;
       this.console = console;
   }

   @Override
   public void execute() {
       try{
           Person p = Ask.askPerson(console);
           cm.add(p);
           console.println("Объект создан");
       } catch (Ask.AskBreak e) {
           console.println("Действие отменено");
       } catch (Exception e) {
           console.println("Ошибка" + e.getMessage());
       }



   }
}

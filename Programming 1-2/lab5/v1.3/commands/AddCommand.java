package itmo.commands;

import itmo.CollectionManager;
import itmo.Command;
import itmo.Console;
import itmo.Person;
import java.util.Random;
import itmo.Ask;

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

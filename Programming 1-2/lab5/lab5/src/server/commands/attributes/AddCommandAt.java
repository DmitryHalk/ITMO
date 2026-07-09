package server.commands.attributes;

import client.Console.Ask;
import client.Console.Console;
import server.common.Clases.Person;
import server.common.Interfaces.Command;
import server.managers.CollectionManager;

public class AddCommandAt {
   private final CollectionManager cm;
   private Console console;


   public AddCommandAt(Console console, CollectionManager cm){
       this.cm = cm;
       this.console = console;
   }

}

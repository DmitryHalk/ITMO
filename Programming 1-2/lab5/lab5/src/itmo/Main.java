package itmo;

import client.Console.Console;
import client.Console.MainConsole;
import client.Invoker;
import server.managers.CollectionManager;
import server.managers.DumpManager;

public class Main {
    public static void main(String[] args) {

        String fileName = System.getenv("COLLECTION_FILE");
        CollectionManager cm = new CollectionManager();
        DumpManager dm = new DumpManager(fileName, cm);
        if (fileName != null) {
            dm.loadFromFile();
        } else {
            System.out.println("Переменная окружения COLLECTION_FILE не задана. Работаем без файла.");
        }

        Console console = new MainConsole();
        Invoker invoker = new Invoker(console, cm);

        invoker.run();


    }
}
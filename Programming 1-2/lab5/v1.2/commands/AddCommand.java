package itmo;

import client.LocalRuntime;
import server.RemoutRuntime;
import server.managers.CollectionManager;
import server.managers.DumpManager;

public class Main {
    public static void main(String[] args) {
        // 1. Загружаем коллекцию из файла (как в пятой лабе)
        String fileName = System.getenv("COLLECTION_FILE");
        CollectionManager collectionManager = new CollectionManager();
        DumpManager dumpManager = new DumpManager(fileName, collectionManager);
        if (fileName != null) {
            dumpManager.loadFromFile();
        } else {
            System.out.println("Переменная окружения COLLECTION_FILE не задана. Работаем без файла.");
        }

        // 2. Создаём серверную часть (имитация)
        RemoutRuntime server = new RemoutRuntime(collectionManager);
        server.run(); // выведет сообщение о готовности

        // 3. Создаём клиента и передаём ему сервер
        LocalRuntime client = new LocalRuntime(server);
        client.run(); // запускаем интерактивный режим

        // 4. После завершения клиента можно сохранить коллекцию
        System.out.println("Сохранение коллекции...");
        dumpManager.saveToFile();
    }
}
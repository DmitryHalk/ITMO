package itmo.lab6;

import itmo.lab6.managers.*;
import java.util.Scanner;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {

        logger.info("Сервер запускается...");

        String fileName = System.getenv("COLLECTION_FILE");
        CollectionManager collectionManager = new CollectionManager();
        DumpManager dumpManager = new DumpManager(fileName, collectionManager);

        if (fileName != null) {
            logger.info("Загрузка коллекции из файла: " + fileName);
            dumpManager.loadFromFile();
        } else {
            logger.warning("Переменная окружения COLLECTION_FILE не задана. Работаем без файла.");
        }

        RemoutRuntime runtime = new RemoutRuntime(collectionManager, dumpManager);
        NetworkServer server = new NetworkServer(runtime, 8080);


        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Сервер завершает работу, сохраняем коллекцию...");
            dumpManager.saveToFile();
        }));


        Thread consoleThread = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String line = scanner.nextLine().trim();
                if ("save".equals(line)) {
                    dumpManager.saveToFile();
                    logger.info("Коллекция сохранена по команде сервера.");
                } else if ("exit".equals(line)) {
                    logger.info("Завершение работы сервера...");
                    dumpManager.saveToFile();
                    System.exit(0);
                }
            }
        });
        consoleThread.setDaemon(true);
        consoleThread.start();
        logger.info("Сервер запущен и готов принимать подключения.");
        server.run();
    }
}
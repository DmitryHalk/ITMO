package itmo.lab6;

import itmo.lab6.managers.*;

import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        logger.info("Сервер запускается...");

        // Параметры подключения к БД
        // Читаем из переменных окружения; по умолчанию — настройки кафедрального сервера
        String dbHost = getEnv("DB_HOST", "pg");
        String dbName = getEnv("DB_NAME", "studs");
        String dbUser = getEnv("DB_USER", System.getProperty("user.name"));
        String dbPass = getEnv("DB_PASS", "");
        String dbSchema = getEnv("DB_SCHEMA", dbUser);
        int serverPort = Integer.parseInt(getEnv("SERVER_PORT", "8080"));

        String dbUrl = "jdbc:postgresql://" + dbHost + "/" + dbName
                + "?currentSchema=" + dbSchema;
        logger.info("Подключение к БД: " + dbUrl + " (user=" + dbUser + ")");

        ConnectionProvider connectionProvider = new ConnectionProvider(dbUrl, dbUser, dbPass);
        SchemaInitializer schemaInitializer = new SchemaInitializer(connectionProvider);
        schemaInitializer.initSchema();

        UserDao userDao = new UserDao(connectionProvider);
        PersonDao personDao = new PersonDao(connectionProvider);
        UserManager um = new UserManager(userDao);
        CollectionManager cm = new CollectionManager();

        // Загрузка коллекции из БД в память
        logger.info("Загрузка коллекции из БД...");
        cm.loadFromDatabase(personDao.findAll());
        logger.info("Загружено " + cm.getSize() + " объектов");

        RemoutRuntime runtime = new RemoutRuntime(cm, personDao, um);
        NetworkServer server = new NetworkServer(runtime, serverPort);

        // Обработчик завершения
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Сервер завершает работу...");
            server.shutdown();
        }));

        // Поток для команд с консоли сервера
        Thread consoleThread = new Thread(() -> {
            java.util.Scanner scanner = new java.util.Scanner(System.in);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if ("exit".equals(line)) {
                    logger.info("Завершение по команде 'exit'");
                    System.exit(0);
                } else if ("info".equals(line)) {
                    logger.info("Объектов в коллекции: " + cm.getSize());
                }
            }
        });
        consoleThread.setDaemon(true);
        consoleThread.start();

        logger.info("Сервер готов. Ожидание подключений на порту " + serverPort);
        server.run();
    }

    private static String getEnv(String name, String defaultValue) {
        String val = System.getenv(name);
        return (val != null && !val.isBlank()) ? val : defaultValue;
    }
}

package itmo;

public class Main {
    public static void main(String[] args) {

        String fileName = System.getenv("COLLECTION_FILE");
        CollectionManager cm = new CollectionManager(fileName);

        if (fileName != null) {
            cm.loadFromFile();
        } else {
            System.out.println("Переменная окружения COLLECTION_FILE не задана. Работаем без файла.");
        }

        Console console = new MainConsole();
        Invoker invoker = new Invoker(console);

        invoker.run();


    }
}
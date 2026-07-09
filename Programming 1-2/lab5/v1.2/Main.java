package itmo;

public class Main {
    public static void main(String[] args) {
        CollectionManager cm = new CollectionManager();
        Console console = new MainConsole();
        Invoker invoker = new Invoker(console);

        invoker.run();


    }
}
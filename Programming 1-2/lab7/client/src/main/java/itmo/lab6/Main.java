package itmo.lab6;

public class Main {
    public static void main(String[] args) {
        NetworkClient client = new NetworkClient("localhost", 8080);
        LocalRuntime runtime = new LocalRuntime(client);
        runtime.run();
    }
}
package itmo.lab6;

import itmo.lab6.console.*;
import itmo.lab6.model.Person;
import itmo.lab6.interfaces.Runtime;

import java.util.*;
import java.lang.InterruptedException;



public class LocalRuntime implements Runtime {
    private final Console console = new MainConsole();
    private final NetworkClient client;
    private HashMap<String, TypeArgs> commandTypes;

    public LocalRuntime(NetworkClient client) {
        this.client = client;
    }

    @Override
    public void run() {
        while (!client.connect()) {
            console.println("Не удалось подключиться к серверу. Повторить? (y/n)");
            String answer = console.readln().trim().toLowerCase();
            if (!answer.equals("y")) {
                console.println("Работа клиента завершена.");
                return;
            }
        }

        Response resp = client.send(new Request("get_commands", null, null));
        if (resp != null && resp.isStatus()) {
            commandTypes = (HashMap<String, TypeArgs>) resp.getArgs();
        } else {
            console.printError("Не удалось получить список команд от сервера");
            return;
        }

        console.println("Клиент запущен. Введите команду:");

        ScriptExecutor scriptExecutor = new ScriptExecutor(console, client, commandTypes);

        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.isEmpty()) continue;

            String[] tokens = line.split(" ");
            String name = tokens[0];
            String arg = tokens.length > 1 ? tokens[1] : null;

            if (name.equals("exit")) {
                console.println("Завершение работы клиента...");
                break;
            }

            if (name.equals("execute_script")) {
                scriptExecutor.execute(arg);
                continue;
            }

            TypeArgs type = commandTypes.get(name);
            if (type == null) {
                console.println("Неизвестная команда");
                continue;
            }

            if (type == TypeArgs.INTEGER) {
                if (arg == null) {
                    console.println("Ошибка: требуется целое число");
                    continue;
                }
                try {
                    Integer.parseInt(arg);
                } catch (NumberFormatException e) {
                    console.println("Ошибка: аргумент должен быть целым числом");
                    continue;
                }
            } else if (type == TypeArgs.STRING) {
                if (arg == null || arg.isEmpty()) {
                    console.println("Ошибка: требуется непустая строка");
                    continue;
                }
            }

            Person person = null;
            Request request;

            if (type == TypeArgs.PERSON_WITH_ID) {
                try {
                    person = Ask.askPerson(console);
                } catch (Ask.AskBreak e) {
                    console.println("Ввод отменён.");
                    continue;
                }
                if (person == null) continue;
                request = new Request(name, arg, person);
            } else if (type == TypeArgs.PERSON) {
                try {
                    person = Ask.askPerson(console);
                } catch (Ask.AskBreak e) {
                    console.println("Ввод отменён.");
                    continue;
                }
                if (person == null) continue;
                request = new Request(name, arg, person);
            } else {
                request = new Request(name, arg, null);
            }

            if (!client.isConnected()) {
                console.println("Соединение потеряно. Попытка переподключения...");
                if (!client.connect()) {
                    console.println("Не удалось восстановить соединение. Команда не отправлена.");
                    continue;
                }
                console.println("Соединение восстановлено.");
            }

            Response response = client.send(request);
            List<Integer> time = List.of(2, 5, 8, 10, 20, 30);
            for (int attempt = 0; response == null && attempt < time.size(); attempt++) {
                console.println("Ошибка связи. Ожидание " + time.get(attempt) + " секунд...");
                try {
                    Thread.sleep(time.get(attempt) * 1000L);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    break;
                }
                client.close();
                if (client.connect()) {
                    console.println("Повторная отправка...");
                    response = client.send(request);
                } else {
                    console.println("Не удалось переподключиться.");
                }
            }
            
            if (response == null) {
                console.println("Не удалось выполнить команду после всех попыток.");
            }
            console.println(response.getMessage());
        }
        client.close();
    }
}
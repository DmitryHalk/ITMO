package itmo.lab6;

import itmo.lab6.console.*;
import itmo.lab6.model.Person;
import itmo.lab6.interfaces.Runtime;

import java.util.*;

public class LocalRuntime implements Runtime {
    private final Console console = new MainConsole();
    private final NetworkClient client;
    private HashMap<String, TypeArgs> commandTypes;

    // Учётные данные текущего пользователя
    private String currentLogin;
    private String currentPassword;

    public LocalRuntime(NetworkClient client) {
        this.client = client;
    }

    @Override
    public void run() {
        // Подключение к серверу
        while (!client.connect()) {
            console.println("Не удалось подключиться к серверу. Повторить? (y/n)");
            String answer = console.readln().trim().toLowerCase();
            if (!answer.equals("y")) {
                console.println("Работа клиента завершена.");
                return;
            }
        }

        // Получение списка команд
        Response resp = client.send(new Request("get_commands", null, null));
        if (resp != null && resp.isStatus()) {
            commandTypes = (HashMap<String, TypeArgs>) resp.getArgs();
        } else {
            console.printError("Не удалось получить список команд от сервера");
            return;
        }

        // ── Аутентификация ──
        if (!authLoop()) {
            console.println("Работа завершена.");
            client.close();
            return;
        }

        console.println("Клиент запущен. Введите команду (help — список команд, exit — выход):");
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
                scriptExecutor.setCredentials(currentLogin, currentPassword);
                scriptExecutor.execute(arg);
                continue;
            }

            TypeArgs type = commandTypes.get(name);
            if (type == null) {
                console.println("Неизвестная команда. Введите help для списка команд.");
                continue;
            }

            // Валидация аргументов
            if (type == TypeArgs.INTEGER) {
                if (arg == null) { console.println("Ошибка: требуется целое число"); continue; }
                try { Integer.parseInt(arg); }
                catch (NumberFormatException e) { console.println("Ошибка: аргумент должен быть целым числом"); continue; }
            } else if (type == TypeArgs.STRING) {
                if (arg == null || arg.isEmpty()) { console.println("Ошибка: требуется непустая строка"); continue; }
            }

            Person person = null;
            if (type == TypeArgs.PERSON || type == TypeArgs.PERSON_WITH_ID) {
                try {
                    person = Ask.askPerson(console);
                } catch (Ask.AskBreak e) {
                    console.println("Ввод отменён.");
                    continue;
                }
                if (person == null) continue;
            }

            // Формируем запрос с учётными данными
            Request request = new Request(name, arg, person, currentLogin, currentPassword);

            Response response = sendWithRetry(request);
            if (response == null) {
                console.println("Не удалось выполнить команду после всех попыток.");
            } else {
                console.println(response.getMessage());
            }
        }
        client.close();
    }

    // ──────────────────────────────────────────────
    //  Цикл авторизации: register или login
    // ──────────────────────────────────────────────
    private boolean authLoop() {
        while (true) {
            console.println("Введите 'register' для регистрации или 'login' для входа:");
            String choice = console.readln().trim().toLowerCase();

            if (choice.equals("register") || choice.equals("login")) {
                console.println("Логин: ");
                String login = console.readln().trim();
                console.println("Пароль: ");
                String password = console.readln().trim();

                if (login.isEmpty() || password.isEmpty()) {
                    console.println("Логин и пароль не могут быть пустыми.");
                    continue;
                }

                Request req = new Request(choice, null, null, login, password);
                Response resp = sendWithRetry(req);

                if (resp != null && resp.isStatus()) {
                    currentLogin = login;
                    currentPassword = password;
                    console.println(resp.getMessage());
                    return true;
                } else {
                    console.println(resp != null ? resp.getMessage() : "Нет ответа от сервера.");
                }
            } else if (choice.equals("exit")) {
                return false;
            } else {
                console.println("Введите 'register', 'login' или 'exit'.");
            }
        }
    }

    private Response sendWithRetry(Request request) {
        if (!client.isConnected()) {
            if (!client.connect()) {
                console.println("Нет подключения к серверу.");
                return null;
            }
        }

        Response response = client.send(request);
        List<Integer> delays = List.of(2, 5, 8, 10, 20, 30);
        for (int attempt = 0; response == null && attempt < delays.size(); attempt++) {
            console.println("Ошибка связи. Повтор через " + delays.get(attempt) + " сек...");
            try { Thread.sleep(delays.get(attempt) * 1000L); }
            catch (InterruptedException ie) { Thread.currentThread().interrupt(); break; }
            client.close();
            if (client.connect()) response = client.send(request);
        }
        return response;
    }
}

package itmo.lab6;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;
import java.util.logging.Logger;

/**
 * Многопоточный сетевой сервер.
 *
 * Архитектура потоков:
 *  1. CachedThreadPool  — читает запросы от каждого клиента
 *  2. new Thread        — обрабатывает каждый запрос
 *  3. FixedThreadPool   — отправляет ответы
 */
public class NetworkServer {
    private static final Logger logger = Logger.getLogger(NetworkServer.class.getName());

    private final RemoutRuntime runtime;
    private final int port;

    // 1) Cached thread pool — для чтения запросов (по одному потоку на клиента)
    private final ExecutorService readPool = Executors.newCachedThreadPool();
    // 3) Fixed thread pool — для отправки ответов
    private final ExecutorService sendPool = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors() * 2);

    public NetworkServer(RemoutRuntime runtime, int port) {
        this.runtime = runtime;
        this.port = port;
    }

    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("Сервер запущен на порту " + port);
            while (!Thread.currentThread().isInterrupted()) {
                Socket client = serverSocket.accept();
                logger.info("Подключился клиент: " + client.getInetAddress());
                // 1) Для каждого клиента — задача в CachedThreadPool
                readPool.submit(() -> handleClient(client));
            }
        } catch (IOException e) {
            logger.severe("Ошибка сервера: " + e.getMessage());
        }
    }

    /** Обрабатывает все запросы одного клиента в потоке из CachedThreadPool. */
    private void handleClient(Socket client) {
        try (DataInputStream dis = new DataInputStream(client.getInputStream());
             DataOutputStream dos = new DataOutputStream(client.getOutputStream())) {

            while (!client.isClosed()) {
                // Читаем запрос
                int length;
                try {
                    length = dis.readInt();
                } catch (EOFException e) {
                    break; // клиент отключился
                }
                byte[] data = new byte[length];
                dis.readFully(data);

                Request request;
                try (ObjectInputStream ois = new ObjectInputStream(
                        new ByteArrayInputStream(data))) {
                    request = (Request) ois.readObject();
                }
                logger.info("Запрос от " + request.getLogin() + ": " + request.getName());

                // 2) Обрабатываем запрос в новом потоке (java.lang.Thread)
                CompletableFuture<Response> futureResponse = new CompletableFuture<>();
                Thread processingThread = new Thread(() -> {
                    Response response;
                    if ("get_commands".equals(request.getName())) {
                        response = new Response("ok", true, runtime.getCommandsAttribute());
                    } else {
                        response = runtime.execute(request);
                    }
                    futureResponse.complete(response);
                });
                processingThread.setName("processor-" + request.getName());
                processingThread.start();

                // Получаем результат и передаём в FixedThreadPool для отправки
                Response response = futureResponse.get();

                // 3) Отправляем ответ через FixedThreadPool
                // Используем Future, чтобы дождаться отправки перед чтением следующего запроса
                Future<?> sendFuture = sendPool.submit(() -> {
                    try {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                            oos.writeObject(response);
                        }
                        byte[] respData = baos.toByteArray();
                        synchronized (dos) {
                            dos.writeInt(respData.length);
                            dos.write(respData);
                            dos.flush();
                        }
                    } catch (IOException e) {
                        logger.severe("Ошибка отправки ответа: " + e.getMessage());
                    }
                });
                sendFuture.get(); // дождаться отправки перед чтением следующего запроса
            }
        } catch (EOFException e) {
            logger.info("Клиент отключился");
        } catch (Exception e) {
            logger.warning("Ошибка клиента: " + e.getMessage());
        } finally {
            try { client.close(); } catch (IOException ignored) {}
            logger.info("Клиент отсоединён: " + client.getInetAddress());
        }
    }

    public void shutdown() {
        readPool.shutdown();
        sendPool.shutdown();
    }
}

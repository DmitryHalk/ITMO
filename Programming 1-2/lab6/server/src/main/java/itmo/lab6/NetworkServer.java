package itmo.lab6;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.*;

public class NetworkServer {
    private final RemoutRuntime runtime;
    private final int port;
    private final Logger logger = Logger.getLogger(NetworkServer.class.getName());

    public NetworkServer(RemoutRuntime runtime, int port) {
        this.runtime = runtime;
        this.port = port;
    }

    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("Сервер запущен на порту " + port);
            while (true) {
                Socket client = serverSocket.accept();
                logger.info("Подключился клиент: " + client.getInetAddress());
                handleClient(client);
            }
        } catch (IOException e) {
            logger.severe("Ошибка сервера: " + e.getMessage());
        }
    }

    private void handleClient(Socket client) {
        try (DataInputStream dis = new DataInputStream(client.getInputStream());
             DataOutputStream dos = new DataOutputStream(client.getOutputStream())) {

            while (true) { // ploho
                int length = dis.readInt();
                byte[] data = new byte[length];
                dis.readFully(data);

                ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
                Request request = (Request) ois.readObject();
                logger.info("Получена команда: " + request.getName());

                Response response;
                if ("get_commands".equals(request.getName())) {
                    response = new Response("ok", true, runtime.getCommandsAttribute());
                } else {
                    response = runtime.execute(request);
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(response);
                oos.flush();
                byte[] respData = baos.toByteArray();

                dos.writeInt(respData.length);
                dos.write(respData);
                dos.flush();
            }
        } catch (EOFException e) {
            logger.info("Клиент отключился");
        } catch (Exception e) {
            logger.severe("Ошибка обработки клиента: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try { client.close(); } catch (IOException ignored) {}
        }
    }
}
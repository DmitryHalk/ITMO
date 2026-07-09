package itmo.lab6;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.channels.UnresolvedAddressException;
import java.nio.channels.UnsupportedAddressTypeException;

public class NetworkClient {
    private final String host;
    private final int port;
    private SocketChannel channel;
    private static final int CONNECTION_TIMEOUT_MS = 5000;

    public NetworkClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public boolean connect() {
        try {
            channel = SocketChannel.open();
            channel.configureBlocking(false);
            channel.connect(new InetSocketAddress(host, port));

            long start = System.currentTimeMillis();
            while (!channel.finishConnect()) {
                if (System.currentTimeMillis() - start > CONNECTION_TIMEOUT_MS) {
                    channel.close();
                    return false;
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return false;
                }
            }
            return true;
        } catch (UnresolvedAddressException | UnsupportedAddressTypeException | IOException e) {
            System.err.println("Ошибка подключения: " + e.getMessage());
            return false;
        }
    }

    public Response send(Request request) {
        if (channel == null || !channel.isConnected()) {
            System.err.println("Нет соединения с сервером.\nПереподключение...");
            return null;
        }

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(request);
            oos.flush();
            byte[] requestData = baos.toByteArray();

            ByteBuffer lengthBuf = ByteBuffer.allocate(4).putInt(requestData.length).flip();
            while (lengthBuf.hasRemaining()) {
                channel.write(lengthBuf);
            }
            channel.write(ByteBuffer.wrap(requestData));

            ByteBuffer lenBuf = ByteBuffer.allocate(4);
            while (lenBuf.hasRemaining()) {
                int read = channel.read(lenBuf);
                if (read == -1) {
                    System.err.println("Сервер закрыл соединение.");
                    return null;
                }
            }
            lenBuf.flip();
            int length = lenBuf.getInt();
            if (length <= 0 || length > 10_000_000) {
                System.err.println("Некорректная длина ответа: " + length);
                return null;
            }

            ByteBuffer dataBuf = ByteBuffer.allocate(length);
            while (dataBuf.hasRemaining()) {
                int read = channel.read(dataBuf);
                if (read == -1) {
                    System.err.println("Сервер закрыл соединение при передаче данных.");
                    return null;
                }
            }
            dataBuf.flip();
            byte[] responseData = new byte[length];
            dataBuf.get(responseData);

            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(responseData));
            return (Response) ois.readObject();

        } catch (Exception e) {
            System.err.println("Ошибка отправки/получения: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public void close() {
        try {
            if (channel != null) channel.close();
        } catch (IOException ignored) {}
    }

    public boolean isConnected() {
        return channel != null && channel.isConnected();
    }
}
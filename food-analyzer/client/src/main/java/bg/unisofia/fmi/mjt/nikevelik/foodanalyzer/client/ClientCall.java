package bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class ClientCall implements AutoCloseable {
    private static final int BUFFER_SIZE = 8192;
    private static final char MESSAGE_END_SERVER_DELIMITER = '\0';

    private SocketChannel channel;

    public ClientCall(String host, int port) throws IOException {
        channel = SocketChannel.open();
        channel.connect(new InetSocketAddress(host, port));
    }

    public void sendRequest(String message) throws IOException {
        ByteBuffer buffer = ByteBuffer.wrap((message).getBytes(StandardCharsets.UTF_8));
        while (buffer.hasRemaining()) {
            channel.write(buffer);
        }
    }

    public String readResponse() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        StringBuilder response = new StringBuilder();

        while (true) {
            int bytesRead = channel.read(buffer);
            if (bytesRead == -1) {
                return null;
            }
            buffer.flip();
            while (buffer.hasRemaining()) {
                char ch = (char) buffer.get();
                if (ch == MESSAGE_END_SERVER_DELIMITER) {
                    buffer.compact();
                    return response.toString();
                }
                response.append(ch);
            }
            buffer.clear();
        }
    }

    @Override
    public void close() throws IOException {
        if (channel != null && channel.isOpen()) {
            channel.close();
        }
    }
}

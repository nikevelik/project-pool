package bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.server.handling;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;
import java.util.logging.Level;

import bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.server.command.Command;
import bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.server.command.CommandProvider;
import bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.server.command.exception.CommandNotFoundException;
import bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service.HybridService;

public class KeyHandlerImpl implements KeyHandler {

    private static final Logger LOGGER = Logger.getLogger(KeyHandlerImpl.class.getName());

    private final CommandProvider commandProvider;
    private final ExecutorService workers;

    public KeyHandlerImpl(HybridService hybridService, ExecutorService workers) {
        this.commandProvider = new CommandProvider(hybridService);
        this.workers = workers;
    }

    @Override
    public void handleKeyRead(SelectionKey key) {
        SocketChannel channel = (SocketChannel) key.channel();
        try {
            readFromClient(key, channel);
        } catch (IOException e) {
            handleExceptionOnChannel(key, channel, "reading", e);
        }
    }

    private void readFromClient(SelectionKey key, SocketChannel channel) throws IOException {
        BufferPair buffers = (BufferPair) key.attachment();
        synchronized (buffers) {
            int bytesRead = channel.read(buffers.first());
            if (bytesRead < 0) {
                closeChannel(channel);
                return;
            }

            key.interestOps(0);
            String input = extractInputFromBuffer(buffers.first());
            LOGGER.info("Received from client: " + input);

            String[] commandParts = parseCommand(input);
            processCommand(commandParts, key, buffers);
        }
    }

    private String extractInputFromBuffer(java.nio.ByteBuffer buffer) {
        buffer.flip();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    private String[] parseCommand(String input) {
        String[] pieces = input.trim().split("\\s+", 2);
        String commandName = pieces[0];
        String argument = (pieces.length > 1) ? pieces[1] : null;
        return new String[]{commandName, argument};
    }

    private void processCommand(String[] commandParts, SelectionKey key, BufferPair buffers) {
        String commandName = commandParts[0];
        String argument = commandParts[1];

        try {
            Command command = commandProvider.createCommand(commandName);
            workers.submit(() -> executeCommandAndWriteResult(command, argument, key, buffers));
        } catch (CommandNotFoundException e) {
            writeErrorToClient("[ERROR]: Command not found - " + e.getMessage(), key, buffers);
            LOGGER.log(Level.WARNING, e, () -> "Command not found: " + e.getMessage());
        }
    }

    private void executeCommandAndWriteResult(Command command, String argument, SelectionKey key, BufferPair buffers) {
        command.execute(argument)
                .thenAccept(result -> writeResultToClient(result, key, buffers))
                .exceptionally(e -> {
                    LOGGER.log(Level.WARNING, e, () -> "Unknown error occurred during command execution: " + e.getMessage());
                    writeErrorToClient("[ERROR]: " + e.getMessage(), key, buffers);
                    return null;
                });
    }

    private void writeResultToClient(String result, SelectionKey key, BufferPair buffers) {
        String response = result + "\0";
        writeToClient(response, key, buffers);
    }

    private void writeErrorToClient(String error, SelectionKey key, BufferPair buffers) {
        writeToClient(error + "\0", key, buffers);
    }

    private void writeToClient(String message, SelectionKey key, BufferPair buffers) {
        byte[] outputBytes = message.getBytes(StandardCharsets.UTF_8);
        synchronized (buffers) {
            buffers.second().put(outputBytes);
            key.interestOps(SelectionKey.OP_WRITE);
            warnSelector(key);
        }
    }

    private void warnSelector(SelectionKey key) {
        if (key.selector() != null) {
            key.selector().wakeup();
        }
    }

    @Override
    public void handleKeyWrite(SelectionKey key) {
        SocketChannel channel = (SocketChannel) key.channel();
        try {
            writeToClientChannel(key, channel);
        } catch (IOException e) {
            handleExceptionOnChannel(key, channel, "writing", e);
        }
    }

    private void writeToClientChannel(SelectionKey key, SocketChannel channel) throws IOException {
        BufferPair buffers = (BufferPair) key.attachment();
        synchronized (buffers) {
            buffers.second().flip();
            channel.write(buffers.second());

            if (!buffers.second().hasRemaining()) {
                prepareForNextRead(key, buffers);
            }
        }
    }

    private void prepareForNextRead(SelectionKey key, BufferPair buffers) {
        key.interestOps(SelectionKey.OP_READ);
        LOGGER.info("Sent response to client");
        buffers.first().clear();
        buffers.second().clear();
    }

    // General resource cleanup and error logging
    private void handleExceptionOnChannel(SelectionKey key, SocketChannel channel, String operation, IOException e) {
        LOGGER.log(Level.WARNING, e, () -> "Error while " + operation + " on channel: " + e.getMessage());
        closeChannel(channel);
        key.cancel();
    }

    private void closeChannel(SocketChannel channel) {
        try {
            channel.close();
        } catch (IOException ex) {
            LOGGER.log(Level.WARNING, ex, () -> "Error closing channel: " + ex.getMessage());
        }
    }
}

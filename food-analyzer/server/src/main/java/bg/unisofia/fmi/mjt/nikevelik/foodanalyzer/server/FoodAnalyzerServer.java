package bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.*;

import bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.server.handling.BufferPair;
import bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.server.handling.KeyHandler;
import bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.server.handling.KeyHandlerImpl;
import bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service.HybridService;

public class FoodAnalyzerServer {

    private static final Logger LOGGER = Logger.getLogger(FoodAnalyzerServer.class.getName());

    private static final String HOST = "localhost";
    private static final int FIRST_BUFFER_SIZE = 256;
    private static final int SECOND_BUFFER_SIZE = 8192;

    private final KeyHandler keyHandler;
    private final ExecutorService workerPool;
    private final int port;
    private volatile boolean isServerWorking;
    private Selector selector;

    public static void main(String[] args) throws Exception {
        new FoodAnalyzerServer(System.getenv("USDA_API_KEY"), 8080).start();
    }

    public FoodAnalyzerServer(String apiKey, int port) {
        this.workerPool = Executors.newFixedThreadPool(Math.max(2, Runtime.getRuntime().availableProcessors()));
        HybridService hybridService = HybridService.defaultHybridService(apiKey);
        this.keyHandler = new KeyHandlerImpl(hybridService, workerPool);
        this.port = port;
    }

    public void start() throws IOException {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            selector = Selector.open();
            configureServerSocketChannel(serverSocketChannel);
            isServerWorking = true;
            LOGGER.info("The server started!");
            try {
                run();
            } finally {
                stop();
            }
        }
    }

    private void configureServerSocketChannel(ServerSocketChannel ssc) throws IOException {
        ssc.bind(new InetSocketAddress(HOST, port));
        ssc.configureBlocking(false);
        ssc.register(selector, SelectionKey.OP_ACCEPT);
    }

    private void run() throws IOException {
        while (isServerWorking) {
            int readyChannels = selector.select();
            if (readyChannels > 0) {
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                processSelectedKeys(selectedKeys);
            }
        }
    }

    private void processSelectedKeys(Set<SelectionKey> keys) {
        Iterator<SelectionKey> keyIterator = keys.iterator();
        while (keyIterator.hasNext()) {
            SelectionKey key = keyIterator.next();
            keyIterator.remove();
            if (key.isValid()) {
                if (key.isReadable()) {
                    keyHandler.handleKeyRead(key);
                } else if (key.isWritable()) {
                    keyHandler.handleKeyWrite(key);
                } else if (key.isAcceptable()) {
                    acceptKey(key);
                }
            }
        }
    }

    private void acceptKey(SelectionKey key) {
        try {
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
            SocketChannel client = serverSocketChannel.accept();
            if (client != null) {
                client.configureBlocking(false);
                BufferPair buffers = new BufferPair(ByteBuffer.allocate(FIRST_BUFFER_SIZE), ByteBuffer.allocate(SECOND_BUFFER_SIZE));
                client.register(key.selector(), SelectionKey.OP_READ).attach(buffers);
                LOGGER.info("New client connected");
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, e, ()->"Failed to accept new connection: " + e.getMessage());
        }
    }

    public void stop() {
        LOGGER.info("Stopping...");
        isServerWorking = false;
        if (selector != null) {
            selector.wakeup();
        }
        workerPool.shutdown();
        cleanUpWorkers();
        cleanUpSelector();
    }

    private void cleanUpWorkers() {
        try {
            if (!workerPool.awaitTermination(3, TimeUnit.SECONDS)) {
                workerPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            workerPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private void cleanUpSelector() {
        if (selector != null && selector.isOpen()) {
            closeAllChannels();
            try {
                selector.close();
            } catch (IOException ex) {
                LOGGER.log(Level.WARNING, ex, ()->"Failed to close selector: " + ex.getMessage());
            }
        }
    }

    private void closeAllChannels() {
        Set<SelectionKey> keys = selector.keys();
        for (SelectionKey key : keys) {
            try {
                key.channel().close();
            } catch (IOException ex) {
                LOGGER.log(Level.WARNING, ex, ()->"Failed to close channel: " + ex.getMessage());
            }
        }
    }
}

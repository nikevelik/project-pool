package bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.server.handling;

import java.nio.channels.SelectionKey;

public interface KeyHandler {
    void handleKeyRead(SelectionKey key);
    void handleKeyWrite(SelectionKey key);
}

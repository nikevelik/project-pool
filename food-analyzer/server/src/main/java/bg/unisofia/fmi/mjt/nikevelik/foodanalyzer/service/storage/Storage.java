package bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service.storage;

import java.util.concurrent.CompletableFuture;


public interface Storage<T> {
    CompletableFuture<T> get(String key);
    CompletableFuture<Void> put(String key, T value);
}

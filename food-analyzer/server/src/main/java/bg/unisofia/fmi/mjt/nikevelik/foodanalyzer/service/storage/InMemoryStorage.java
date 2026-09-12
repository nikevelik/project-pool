package bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service.storage;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;


public class InMemoryStorage<T> implements Storage<T>{

    private final ConcurrentHashMap<String, T> map = new ConcurrentHashMap<>();

    public InMemoryStorage(){ }

    @Override
    public CompletableFuture<T> get(String key) {
        return CompletableFuture.completedFuture(map.get(key));
    }

    @Override
    public CompletableFuture<Void> put(String key, T value){
        if(key == null) {
            return CompletableFuture.completedFuture(null);
        }
        map.put(key, value);
        return CompletableFuture.completedFuture(null);
    }
}

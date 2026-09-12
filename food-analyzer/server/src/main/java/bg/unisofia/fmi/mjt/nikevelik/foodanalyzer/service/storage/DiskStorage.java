// package bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service.storage;
//
// import java.util.concurrent.CompletableFuture;
// import java.util.concurrent.ConcurrentHashMap;
//
// public class DiskStorage<T> implements Storage<T>{
//
//     // needs to be thread-safe
//     // throws only custom-defined unchecked exceptions
//     // handles all caching logic
//     private final FileMap<T> map = new FileMap<>();
//
//     public DiskStorage(){ }
//
//     @Override
//     public CompletableFuture<T> get(String key) {
//         return CompletableFuture.completedFuture(map.get(key));
//     }
//
//     @Override
//     public CompletableFuture<Void> put(String key, T value){
//         if(key == null){
//             return CompletableFuture.completedFuture(null);
//         }
//         map.put(key, value);
//         return CompletableFuture.completedFuture(null);
//     }
// }

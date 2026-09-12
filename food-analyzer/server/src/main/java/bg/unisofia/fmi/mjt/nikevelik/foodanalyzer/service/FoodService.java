package bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service;

import java.util.concurrent.CompletableFuture;

public interface FoodService {
    CompletableFuture<String> search(String query);
    CompletableFuture<String> get(String id);
}

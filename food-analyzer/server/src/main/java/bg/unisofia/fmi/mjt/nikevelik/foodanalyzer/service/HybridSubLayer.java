package bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service;

import static java.util.concurrent.CompletableFuture.completedFuture;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service.api.FoodApi;
import bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service.api.http.HttpSenderImpl;
import bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service.parsing.FoodParser;
import bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service.parsing.Food;
import bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service.parsing.FoodParserImpl;
import bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service.parsing.SearchResultEntry;
import bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service.storage.InMemoryStorage;
import bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service.storage.Storage;

public class HybridSubLayer {

    private final FoodService api;
    private final Storage<List<SearchResultEntry>> queryCache;
    private final Storage<Food> foodCache;
    private final Storage<String> barcodeCache;
    private final FoodParser parser;

    public HybridSubLayer(FoodService api, Storage<List<SearchResultEntry>> queryCache, Storage<Food> foodCache, Storage<String> barcodeCache, FoodParser parser){
        this.api = api;
        this.queryCache = queryCache;
        this.foodCache = foodCache;
        this.barcodeCache = barcodeCache;
        this.parser = parser;
    }

    public static HybridSubLayer defaulHybridSubLayer(String apiKey) {
        return new HybridSubLayer(new FoodApi(apiKey, new HttpSenderImpl()), new InMemoryStorage<>(), new InMemoryStorage<>(), new InMemoryStorage<>(), new FoodParserImpl());
    }

    public CompletableFuture<Food> getByBarcode(String barcode) {
        return barcodeCache.get(barcode)
                .thenCompose(id -> {
                    return id != null ? foodCache.get(id) : completedFuture(null);
                });
    }

    public CompletableFuture<List<SearchResultEntry>> search(String query) {
        return queryCache.get(query)
                .thenCompose(fromCache ->
                        fromCache != null
                                ? completedFuture(fromCache)
                                : api.search(query).thenCompose(apiResponseJson -> {
                                    List<SearchResultEntry> data = parser.parseSearch(apiResponseJson);
                                    data.forEach(entry -> {
                                        barcodeCache.put(entry.barcode(), entry.id());
                                    });
                                    queryCache.put(query, data);
                                    return completedFuture(data);
                                })
                );
    }

    public CompletableFuture<Food> get(String id) {


        return foodCache.get(id)
                .thenCompose(fromCache ->
                        fromCache != null
                                ? completedFuture(fromCache)
                                : api.get(id).thenCompose(fromApi -> {
                            Food food = parser.parseFood(fromApi);
                            foodCache.put(id, food);
                            return completedFuture(food);
                        })
                );
    }
}

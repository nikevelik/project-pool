package bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service.api;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

import bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service.FoodService;
import bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service.api.http.HttpSender;

public class FoodApi implements FoodService {

    private final String apiKey;
    private final HttpSender sender;
    private final String BASE_URL;
    static private final String DEFAULT_URL = "https://api.nal.usda.gov/fdc/v1";
    static private final boolean REQUIRE_ALL_WORDS = true;
    static private final int PAGE_SIZE = 50;

    public FoodApi(String apiKey, HttpSender sender){
        this(apiKey, sender, DEFAULT_URL);
    }

    FoodApi(String apiKey, HttpSender sender, String BASE_URL){
        this.apiKey = apiKey;
        this.sender = sender;
        this.BASE_URL = BASE_URL;
    }

    @Override
    public CompletableFuture<String> search(String query) {
        String url = buildFoodSearchUrl(query);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        return sender.send(request);
    }

    @Override
    public CompletableFuture<String> get(String id) {
        String url = buildGetFoodUrl(id);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        return sender.send(request);
    }

    private String buildFoodSearchUrl(String query, boolean requireAllWords, int pageSize) {
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
        return BASE_URL + "/foods/search?api_key=" + apiKey + "&requireAllWords=" + requireAllWords +
                "&pageSize=" + pageSize + "&query=" + encodedQuery;
    }

    private String buildFoodSearchUrl(String query) {
        return buildFoodSearchUrl(query, REQUIRE_ALL_WORDS, PAGE_SIZE);
    }

    private String buildGetFoodUrl(String id){
        return BASE_URL + "/food/" + id + "?api_key="+apiKey;
    }
}

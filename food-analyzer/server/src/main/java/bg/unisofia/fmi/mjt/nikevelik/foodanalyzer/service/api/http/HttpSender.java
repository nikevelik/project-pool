package bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service.api.http;

import java.net.http.HttpRequest;
import java.util.concurrent.CompletableFuture;

public interface HttpSender {
    CompletableFuture<String> send(HttpRequest request);
}

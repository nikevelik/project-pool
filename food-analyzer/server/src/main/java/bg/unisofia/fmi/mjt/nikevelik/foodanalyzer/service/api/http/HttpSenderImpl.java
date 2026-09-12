package bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service.api.http;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

import bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service.api.http.exception.HttpException;
import bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service.api.http.exception.StatusCodeNot200HttpException;

public class HttpSenderImpl implements HttpSender{
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Override
    public CompletableFuture<String> send(HttpRequest request) {
        return httpClient
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .handle((response, e) -> {
                    if(e instanceof Exception)
                        throw new HttpException("Failed to send api/http request", e);
                    if(response.statusCode() != 200)
                        throw new StatusCodeNot200HttpException("api/http request resolved with unexpected code:" + response.statusCode());
                    return response.body();
                });
    }
}

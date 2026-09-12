package bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service.api.http;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.net.http.HttpRequest;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.*;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

import bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service.api.http.exception.HttpException;
import bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service.api.http.exception.StatusCodeNot200HttpException;


class HttpSenderImplIT{

    WireMockServer wireMockServer;
    HttpSenderImpl sender;
    String baseUrl;

    @BeforeEach
    void setupClass() {
        wireMockServer = new WireMockServer(WireMockConfiguration.options().dynamicPort());
        wireMockServer.start();
        baseUrl = "http://localhost:" + wireMockServer.port();
        sender = new HttpSenderImpl();
    }

    @Test
    void when200OkWithBody_shouldReturnExpectedString() throws Exception {
        String path = "/test1";
        String body = "Hello world!";

        wireMockServer.stubFor(get(urlEqualTo(path))
                .willReturn(aResponse().withStatus(200).withBody(body)));

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + path))
                .GET()
                .build();

        String response = sender.send(req).get(2, TimeUnit.SECONDS);
        assertEquals(body, response);
    }

    @Test
    void when200OkWithEmptyBody_shouldReturnEmptyString() throws Exception {
        String path = "/empty";

        wireMockServer.stubFor(get(urlEqualTo(path))
                .willReturn(aResponse().withStatus(200).withBody("")));

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + path))
                .GET()
                .build();

        String response = sender.send(req).get(2, TimeUnit.SECONDS);
        assertEquals("", response);
    }

    @Test
    void whenMultiple200OkRequests_shouldReturnExpectedStringsForEach() throws Exception {
        String path1 = "/first";
        String path2 = "/second";
        String resp1 = "first!";
        String resp2 = "second!";
        wireMockServer.stubFor(get(urlEqualTo(path1))
                .willReturn(aResponse().withStatus(200).withBody(resp1)));
        wireMockServer.stubFor(get(urlEqualTo(path2))
                .willReturn(aResponse().withStatus(200).withBody(resp2)));

        HttpRequest req1 = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + path1))
                .GET().build();
        HttpRequest req2 = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + path2))
                .GET().build();

        String r1 = sender.send(req1).get(2, TimeUnit.SECONDS);
        String r2 = sender.send(req2).get(2, TimeUnit.SECONDS);

        assertEquals(resp1, r1);
        assertEquals(resp2, r2);
    }

    @Test
    void when2xxButNot200_shouldThrowHttpCodeNot200Exception() {
        String path = "/201";
        wireMockServer.stubFor(get(urlEqualTo(path))
                .willReturn(aResponse().withStatus(201).withBody("created")));

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + path))
                .GET().build();

        CompletableFuture<String> future = sender.send(req);
        ExecutionException ex = assertThrows(ExecutionException.class, () -> future.get(2, TimeUnit.SECONDS));
        assertInstanceOf(StatusCodeNot200HttpException.class, ex.getCause());
    }

    @Test
    void when4xxError_shouldThrowHttpCodeNot200Exception() {
        String path = "/404";
        wireMockServer.stubFor(get(urlEqualTo(path))
                .willReturn(aResponse().withStatus(404)));

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + path))
                .GET().build();

        CompletableFuture<String> future = sender.send(req);
        ExecutionException ex = assertThrows(ExecutionException.class, () -> future.get(2, TimeUnit.SECONDS));
        assertInstanceOf(StatusCodeNot200HttpException.class, ex.getCause());
    }

    @Test
    void when5xxError_shouldThrowHttpCodeNot200Exception() {
        String path = "/500";
        wireMockServer.stubFor(get(urlEqualTo(path))
                .willReturn(aResponse().withStatus(500)));

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + path))
                .GET().build();

        CompletableFuture<String> future = sender.send(req);
        ExecutionException ex = assertThrows(ExecutionException.class, () -> future.get(2, TimeUnit.SECONDS));
        assertInstanceOf(StatusCodeNot200HttpException.class, ex.getCause());
    }

    @Test
    void whenConnectionRefused_shouldThrowException(){
        WireMockServer tmp = new WireMockServer(WireMockConfiguration.options());
        tmp.start();
        int unusedPort = tmp.port();
        tmp.stop();
        String badBaseUrl = "http://localhost:" + unusedPort;
        HttpSenderImpl badSender = new HttpSenderImpl();

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(badBaseUrl + "/notfound"))
                .GET().build();

        CompletableFuture<String> future = badSender.send(req);
        ExecutionException ex = assertThrows(ExecutionException.class, () -> future.get(2, TimeUnit.SECONDS));
        assertInstanceOf(HttpException.class, ex.getCause());
    }

    @Test
    void whenRequestTimesOut_shouldThrowRuntimeException() {
        String path = "/timeout";

        wireMockServer.stubFor(get(urlEqualTo(path))
                .willReturn(aResponse().withStatus(200)
                        .withFixedDelay(2000)
                        .withBody("slow response")));

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + path))
                .timeout(Duration.ofMillis(1000))
                .GET().build();

        CompletableFuture<String> future = sender.send(req);

        ExecutionException ex = assertThrows(ExecutionException.class, () -> future.get(2, TimeUnit.SECONDS));
        assertInstanceOf(HttpException.class, ex.getCause());
    }
}

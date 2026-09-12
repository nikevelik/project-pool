package bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service.api.http.HttpSender;

class FoodApiTest {

    static final String API_KEY = "test-api-key";
    HttpSender mockSender;
    FoodApi foodApi;

    @BeforeEach
    void setUp() {
        mockSender = mock(HttpSender.class);
        foodApi = new FoodApi(API_KEY, mockSender);
    }

    @Test
    void searchFood_shouldReturnCorrectly() {
        String query = "apple pie";
        String expectedResponse = "{\"foods\":[]}";
        CompletableFuture<String> mockResponse = CompletableFuture.completedFuture(expectedResponse);

        when(mockSender.send(any())).thenReturn(mockResponse);

        String actual = foodApi.search(query).join();

        assertEquals(expectedResponse, actual);
    }

    @Test
    void getFood_shouldReturnCorrectly() {
        String id = "12345";
        String expectedResponse = "{\"description\":\"Apple\"}";
        CompletableFuture<String> mockResponse = CompletableFuture.completedFuture(expectedResponse);

        when(mockSender.send(any())).thenReturn(mockResponse);

        String actual = foodApi.get(id).join();

        assertEquals(expectedResponse, actual);
    }

    @Test
    void searchFood_shouldEncodeQuery() {
        String query = "chicken soup & rice";
        String expectedResponse = "{\"foods\":[]}";
        CompletableFuture<String> mockResponse = CompletableFuture.completedFuture(expectedResponse);

        when(mockSender.send(any())).thenReturn(mockResponse);

        foodApi.search(query).join();

        verify(mockSender).send(argThat(req -> {
            String uri = req.uri()
                    .toString();
            return uri.contains("query=chicken%20soup%20%26%20rice") || uri.contains("query=chicken+soup+%26+rice");
        }));
    }
}

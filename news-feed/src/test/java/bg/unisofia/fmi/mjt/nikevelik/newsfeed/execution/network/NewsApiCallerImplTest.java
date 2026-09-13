package bg.unisofia.fmi.mjt.nikevelik.newsfeed.execution.network;

import bg.unisofia.fmi.mjt.nikevelik.newsfeed.execution.network.exception.BadRequestException;
import bg.unisofia.fmi.mjt.nikevelik.newsfeed.execution.network.exception.ClientErrorException;
import bg.unisofia.fmi.mjt.nikevelik.newsfeed.execution.network.exception.ForbiddenException;
import bg.unisofia.fmi.mjt.nikevelik.newsfeed.execution.network.exception.HttpException;
import bg.unisofia.fmi.mjt.nikevelik.newsfeed.execution.network.exception.HttpRequestNotCompletedException;
import bg.unisofia.fmi.mjt.nikevelik.newsfeed.execution.network.exception.NotFoundException;
import bg.unisofia.fmi.mjt.nikevelik.newsfeed.execution.network.exception.ServerErrorException;
import bg.unisofia.fmi.mjt.nikevelik.newsfeed.execution.network.exception.UnauthorizedException;
import bg.unisofia.fmi.mjt.nikevelik.newsfeed.structures.request.enums.Category;
import bg.unisofia.fmi.mjt.nikevelik.newsfeed.structures.request.enums.Country;
import bg.unisofia.fmi.mjt.nikevelik.newsfeed.structures.request.SearchRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class NewsApiCallerImplTest {
    private static final String API_KEY = "TEST_KEY";
    private static final int CODE_200 = 200;
    private static final int TEST_CODE_429 = 429;
    private static final int TEST_CODE_301 = 301;
    private static final int VALID_PAGE_NUMBER = 3;
    private static final int VALID_PAGE_SIZE = 15;
    private HttpClient httpClient;
    private NewsApiCallerImpl apiCaller;

    @BeforeEach
    void setUp() {
        httpClient = mock(HttpClient.class);
        apiCaller = new NewsApiCallerImpl(API_KEY, httpClient);
    }

    @Test
    void callExecutesCorrectly() throws Exception {
        SearchRequest request = new SearchRequest(
            "TEST_QUERY", Category.SPORTS, Country.US, VALID_PAGE_NUMBER,
            VALID_PAGE_SIZE);

        HttpResponse<String> response = mock(HttpResponse.class);
        when(response.statusCode()).thenReturn(CODE_200);
        when(response.body()).thenReturn("{\"status\":\"ok\"}");

        when(httpClient.send(any(HttpRequest.class),
            any(HttpResponse.BodyHandler.class)))
            .thenReturn(response);

        String result = apiCaller.call(request);

        assertEquals("{\"status\":\"ok\"}", result,
            "the result returned upon call did not match the expected one");

        verify(httpClient, times(1)).send(
            any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
    }

    static Stream<Arguments> nonSuccessfulCodesAndExceptions() {
        return Stream.of(
            Arguments.of(NewsApiCallerImpl.CODE_400, BadRequestException.class),
            Arguments.of(NewsApiCallerImpl.CODE_401, UnauthorizedException.class),
            Arguments.of(NewsApiCallerImpl.CODE_403, ForbiddenException.class),
            Arguments.of(NewsApiCallerImpl.CODE_404, NotFoundException.class),
            Arguments.of(TEST_CODE_429, ClientErrorException.class),
            Arguments.of(NewsApiCallerImpl.CODE_500, ServerErrorException.class),
            Arguments.of(TEST_CODE_301, HttpException.class)
        );
    }

    @ParameterizedTest
    @MethodSource("nonSuccessfulCodesAndExceptions")
    void callWhenNonSuccessfulStatusCodeThrowsSpecificException(int statusCode, Class<? extends Exception> exception)
        throws Exception {
        SearchRequest request = new SearchRequest(null, Category.HEALTH, null, null, null);

        HttpResponse<String> response = mock(HttpResponse.class);
        when(response.statusCode()).thenReturn(statusCode);
        when(response.body()).thenReturn("error");

        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
            .thenReturn(response);

        assertThrows(exception, () -> apiCaller.call(request),
            "The caller did not throw the expected exception");
    }

    static Stream<Arguments> httpClientPossibleExceptions() {
        return Stream.of(
            Arguments.of(new IOException("IO failed")),
            Arguments.of(new InterruptedException("Interrupted"))
        );
    }

    @ParameterizedTest
    @MethodSource("httpClientPossibleExceptions")
    void callWhenHttpClientThrowsThrowsException(Exception thrown) throws Exception {
        SearchRequest request = new SearchRequest("", null,
            Country.BG, null, null);

        when(httpClient.send(any(HttpRequest.class),
            any(HttpResponse.BodyHandler.class)))
            .thenThrow(thrown);

        assertThrows(HttpRequestNotCompletedException.class,
            () -> apiCaller.call(request),
            "The caller did not throw the desired exception");
    }

}

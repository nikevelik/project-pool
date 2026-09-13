package bg.unisofia.fmi.mjt.nikevelik.newsfeed.execution.network;

import bg.unisofia.fmi.mjt.nikevelik.newsfeed.execution.network.exception.BadRequestException;
import bg.unisofia.fmi.mjt.nikevelik.newsfeed.execution.network.exception.ClientErrorException;
import bg.unisofia.fmi.mjt.nikevelik.newsfeed.execution.network.exception.ForbiddenException;
import bg.unisofia.fmi.mjt.nikevelik.newsfeed.execution.network.exception.HttpException;
import bg.unisofia.fmi.mjt.nikevelik.newsfeed.execution.network.exception.HttpRequestNotCompletedException;
import bg.unisofia.fmi.mjt.nikevelik.newsfeed.execution.network.exception.NotFoundException;
import bg.unisofia.fmi.mjt.nikevelik.newsfeed.execution.network.exception.ServerErrorException;
import bg.unisofia.fmi.mjt.nikevelik.newsfeed.execution.network.exception.UnauthorizedException;
import bg.unisofia.fmi.mjt.nikevelik.newsfeed.structures.request.SearchRequest;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

/****
 * Implementation of the {@link NewsApiCaller} interface for making HTTP requests to the NewsAPI service.
 * <p>
 * Responsible for building the request URL based on the provided {@link SearchRequest},
 * sending the HTTP request, handling the response, and mapping HTTP status codes to specific exceptions.
 */
public class NewsApiCallerImpl implements NewsApiCaller {

    /**
     * The parameter name for category in the request URL.
     */
    private static final String CATEGORY = "category";

    /**
     * The parameter name representing the search query.
     */
    private static final String QUERY = "q";

    /**
     * The parameter name for specifying the country.
     */
    private static final String COUNTRY = "country";

    /**
     * The parameter name for page number.
     */
    private static final String PAGE_NUMBER = "page";

    /**
     * The parameter name for page size.
     */
    private static final String PAGE_SIZE = "pageSize";

    /**
     * The parameter name for the API key.
     */
    private static final String API_KEY_PARAM = "apiKey";

    /**
     * HTTP status code for Bad Request.
     */
    static final int CODE_400 = 400;

    /**
     * HTTP status code for Unauthorized.
     */
    static final int CODE_401 = 401;

    /**
     * HTTP status code for Forbidden.
     */
    static final int CODE_403 = 403;

    /**
     * HTTP status code for Internal Server Error.
     */
    static final int CODE_500 = 500;

    /**
     * HTTP status code for Success (OK).
     */
    static final int CODE_200 = 200;

    /**
     * HTTP status code for Not Found.
     */
    static final int CODE_404 = 404;

    /**
     * The API key used for authenticating requests to the NewsAPI.
     */
    private final String apiKey;

    /**
     * An {@link HttpClient} instance used to send HTTP requests.
     */
    private final HttpClient httpClient;

    /**
     * The base URL for the NewsAPI endpoint.
     */
    private static final String BASE_URL = "https://newsapi.org/v2/top-headlines";

    /**
     * Constructs a new {@code ApiCallerImpl} with the given API key.
     *
     * @param apiKey the API key for authentication.
     */
    public NewsApiCallerImpl(final String apiKey) {
        this(apiKey, HttpClient.newHttpClient());
    }

    /**
     * Constructs a new {@code ApiCallerImpl} with the given API key and HTTP client.
     *
     * @param apiKey     the API key for authentication.
     * @param httpClient the HTTP client to use for sending requests.
     */
    NewsApiCallerImpl(final String apiKey, HttpClient httpClient) {
        this.apiKey = apiKey;
        this.httpClient = httpClient;
    }

    /**
     * Builds the URL for the search request using its parameters.
     *
     * @param request the {@link SearchRequest} containing all search criteria.
     * @return the complete URL as a String to be used in the HTTP request.
     */
    private String buildSearchUrl(SearchRequest request) {
        StringBuilder url = new StringBuilder(BASE_URL + "?" + API_KEY_PARAM + "=" + apiKey);

        if (request.query() != null && !request.query().isEmpty()) {
            url.append("&").append(QUERY).append("=").append(encode(request.query()));
        }
        if (request.category() != null) {
            url.append("&").append(CATEGORY).append("=")
                .append(encode(request.category().toString()));
        }
        if (request.country() != null) {
            url.append("&").append(COUNTRY).append("=")
                .append(encode(request.country().toString()));
        }
        if (request.pageNumber() != null) {
            url.append("&").append(PAGE_NUMBER).append("=").append(request.pageNumber());
        }
        if (request.pageSize() != null) {
            url.append("&").append(PAGE_SIZE).append("=").append(request.pageSize());
        }

        return url.toString();
    }

    /**
     * Tries to send the provided HTTP request, handling IO and interruption.
     *
     * @param request the {@link HttpRequest} to send.
     * @return the {@link HttpResponse} from the call.
     * @throws HttpRequestNotCompletedException if the request fails to complete due to IO or thread interruption.
     */
    private HttpResponse<String> tryHttpSend(HttpRequest request)
        throws HttpRequestNotCompletedException {
        try {
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new HttpRequestNotCompletedException(
                "Failed to send api/http request: " + e.getMessage(), e
            );
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new HttpRequestNotCompletedException(
                "The thread running this request was interrupted: "
                    + e.getMessage(), e
            );
        }
    }

    /**
     * Validates the HTTP status code, mapping known error codes to specific exceptions.
     *
     * @param status the HTTP status code.
     * @throws BadRequestException   if status is 400.
     * @throws UnauthorizedException if status is 401.
     * @throws ForbiddenException    if status is 403.
     * @throws NotFoundException     if status is 404.
     * @throws ClientErrorException  for any 4xx client errors not explicitly handled.
     * @throws ServerErrorException  for any 5xx server errors.
     * @throws HttpException         for all other status codes not indicating success.
     */
    private void validateStatusCode(int status) throws HttpException {
        if (status == CODE_400) {
            throw new BadRequestException("Bad Request (400): " + status);
        } else if (status == CODE_401) {
            throw new UnauthorizedException("Unauthorized (401): " + status);
        } else if (status == CODE_403) {
            throw new ForbiddenException("Forbidden (403): " + status);
        } else if (status == CODE_404) {
            throw new NotFoundException("Not Found (404): " + status);
        } else if (status >= CODE_400 && status < CODE_500) {
            throw new ClientErrorException(
                "Client error (" + status + "): " + status
            );
        } else if (status >= CODE_500) {
            throw new ServerErrorException(
                "Server error (" + status + "): " + status
            );
        } else if (status != CODE_200) {
            throw new HttpException("Unexpected HTTP status code: " + status);
        }
    }

    /**
     * Encodes a string to UTF-8 for use as a URL parameter.
     *
     * @param field the string to encode.
     * @return the URL-encoded representation of the string.
     */
    private String encode(String field) {
        return URLEncoder.encode(field, StandardCharsets.UTF_8);
    }

    /**
     * Executes a search API call using the provided search request.
     *
     * @param searchRequest the {@link SearchRequest} containing the API call parameters.
     * @return the raw response body as a String.
     * @throws HttpException if an error occurs during the network call or response processing.
     */
    @Override
    public String call(SearchRequest searchRequest) throws HttpException {
        String url = buildSearchUrl(searchRequest);
        HttpRequest httpRequest = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .GET()
            .build();
        HttpResponse<String> response = tryHttpSend(httpRequest);
        validateStatusCode(response.statusCode());
        return response.body();
    }
}

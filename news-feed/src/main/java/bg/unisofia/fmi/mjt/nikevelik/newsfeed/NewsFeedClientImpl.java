package bg.unisofia.fmi.mjt.nikevelik.newsfeed;

import bg.unisofia.fmi.mjt.nikevelik.newsfeed.exception.InvalidApiKeyException;
import bg.unisofia.fmi.mjt.nikevelik.newsfeed.exception.InvalidSearchRequestException;
import bg.unisofia.fmi.mjt.nikevelik.newsfeed.exception.SearchException;
import bg.unisofia.fmi.mjt.nikevelik.newsfeed.execution.network.NewsApiCaller;
import bg.unisofia.fmi.mjt.nikevelik.newsfeed.execution.network.NewsApiCallerImpl;
import bg.unisofia.fmi.mjt.nikevelik.newsfeed.execution.parsing.ResultParser;
import bg.unisofia.fmi.mjt.nikevelik.newsfeed.execution.parsing.ResultParserImpl;
import bg.unisofia.fmi.mjt.nikevelik.newsfeed.structures.request.SearchRequest;
import bg.unisofia.fmi.mjt.nikevelik.newsfeed.structures.result.SearchResult;

/****
 * Implementation of the {@link NewsFeedClient} interface for interacting with the API.
 */
public class NewsFeedClientImpl implements NewsFeedClient {

    private final NewsApiCaller newsApiCaller;
    private final ResultParser parser;

    /**
     * Creates a {@link NewsFeedClientImpl} using an API key read from a system environment variable.
     *
     * @param variableName The name of the environment variable with the API key.
     * @return a new {@link NewsFeedClientImpl} instance
     * @throws SearchException   if the API key is invalid or missing
     * @throws SecurityException if access to environment variables is denied by the security manager
     */
    public static NewsFeedClientImpl createWithApiKeyFromSystemEnvironment(String variableName) throws SearchException {
        return new NewsFeedClientImpl(System.getenv(variableName));
    }

    /**
     * Constructs a new {@code ClientImpl} using the provided API key.
     *
     * @param apiKey the API key to be used with the API
     * @throws InvalidApiKeyException if the API key is null or blank
     */
    public NewsFeedClientImpl(final String apiKey) throws InvalidApiKeyException {
        validateApiKey(apiKey);
        this.newsApiCaller = new NewsApiCallerImpl(apiKey);
        this.parser = new ResultParserImpl();
    }

    /**
     * Constructs a new {@code ClientImpl} with the given {@link NewsApiCaller} and {@link ResultParser}.
     * Primarily intended for testing or dependency injection.
     *
     * @param newsApiCaller the {@link NewsApiCaller} instance
     * @param parser        the {@link ResultParser} instance
     */
    NewsFeedClientImpl(final NewsApiCaller newsApiCaller, final ResultParser parser) {
        this.newsApiCaller = newsApiCaller;
        this.parser = parser;
    }

    /**
     * Executes a search using the given {@link SearchRequest}.
     *
     * @param searchRequest the search request parameters
     * @return the search result of type {@link SearchResult}
     * @throws SearchException               if the search fails for any reason
     * @throws InvalidSearchRequestException if the search request is invalid
     */
    @Override
    public SearchResult search(final SearchRequest searchRequest)
        throws SearchException {
        validateSearchRequest(searchRequest);
        String httpJsonResponse = newsApiCaller.call(searchRequest);
        return parser.parse(httpJsonResponse);
    }

    /**
     * Validates the provided {@link SearchRequest}.
     *
     * @param searchRequest the search request to validate
     * @throws InvalidSearchRequestException if the request is null, insufficient or contains invalid pagination
     */
    private void validateSearchRequest(SearchRequest searchRequest)
        throws InvalidSearchRequestException {
        if (searchRequest == null) {
            throw new InvalidSearchRequestException(
                "search request object cannot be null"
            );
        }
        if (isSearchRequestInsufficient(searchRequest)) {
            throw new InvalidSearchRequestException(
                "search request must provide at least one of query, category, country"
            );
        }
        if (searchRequest.pageNumber() != null && searchRequest.pageNumber() <= 0) {
            throw new InvalidSearchRequestException(
                "page number must be positive number"
            );
        }
        if (searchRequest.pageSize() != null && searchRequest.pageSize() <= 0) {
            throw new InvalidSearchRequestException("page size must be positive number");
        }
    }

    /**
     * Checks if the search request does not provide either query, category, or country.
     *
     * @param searchRequest the search request to check
     * @return true if none of the required fields are provided, otherwise false
     */
    private boolean isSearchRequestInsufficient(SearchRequest searchRequest) {
        return (searchRequest.query() == null || searchRequest.query().isBlank()) &&
            searchRequest.category() == null &&
            searchRequest.country() == null;
    }

    /**
     * Validates the given API key.
     *
     * @param apiKey the API key to validate
     * @throws InvalidApiKeyException if the API key is null or blank
     */
    private void validateApiKey(String apiKey) throws InvalidApiKeyException {
        if (apiKey == null) {
            throw new InvalidApiKeyException("api key cannot be null.");
        }
        if (apiKey.isBlank()) {
            throw new InvalidApiKeyException("api key cannot be blank.");
        }
    }

}

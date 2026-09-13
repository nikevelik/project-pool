package bg.unisofia.fmi.mjt.nikevelik.newsfeed;

import bg.unisofia.fmi.mjt.nikevelik.newsfeed.exception.SearchException;
import bg.unisofia.fmi.mjt.nikevelik.newsfeed.structures.request.SearchRequest;
import bg.unisofia.fmi.mjt.nikevelik.newsfeed.structures.request.enums.Category;
import bg.unisofia.fmi.mjt.nikevelik.newsfeed.structures.request.enums.Country;
import bg.unisofia.fmi.mjt.nikevelik.newsfeed.structures.result.SearchResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LiveTest {

    private static final int SEARCH_PAGE_SIZE = 10;
    private static final int SEARCH_PAGE_NUMBER = 1;
    private static final String API_KEY_ENV_VAR = "NEWS_FEED_API_KEY";
    private static final String SEARCH_QUERY = "bitcoin";
    private static final SearchRequest TEST_REQUEST = new SearchRequest
        .Builder()
        .query(SEARCH_QUERY)
        .country(Country.US)
        .category(Category.BUSINESS)
        .pageNumber(SEARCH_PAGE_NUMBER)
        .pageSize(SEARCH_PAGE_SIZE)
        .build();

    @Test
    void smokeTest() {
        assertDoesNotThrow(() -> {
            NewsFeedClient newsFeedClient = NewsFeedClientImpl.createWithApiKeyFromSystemEnvironment(API_KEY_ENV_VAR);
            SearchResult result = newsFeedClient.search(TEST_REQUEST);
            assertNotNull(result);
        });
    }

    @Test
    void testResultNotNull() throws SearchException {
        NewsFeedClient newsFeedClient = NewsFeedClientImpl.createWithApiKeyFromSystemEnvironment(API_KEY_ENV_VAR);
        SearchResult result = newsFeedClient.search(TEST_REQUEST);
        assertNotNull(result);
    }
}

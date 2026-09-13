package bg.unisofia.fmi.mjt.nikevelik.newsfeed;

import bg.unisofia.fmi.mjt.nikevelik.newsfeed.exception.InvalidApiKeyException;
import bg.unisofia.fmi.mjt.nikevelik.newsfeed.exception.InvalidSearchRequestException;
import bg.unisofia.fmi.mjt.nikevelik.newsfeed.exception.SearchException;
import bg.unisofia.fmi.mjt.nikevelik.newsfeed.execution.network.NewsApiCaller;
import bg.unisofia.fmi.mjt.nikevelik.newsfeed.execution.parsing.ResultParser;
import bg.unisofia.fmi.mjt.nikevelik.newsfeed.structures.request.SearchRequest;
import bg.unisofia.fmi.mjt.nikevelik.newsfeed.structures.request.enums.Category;
import bg.unisofia.fmi.mjt.nikevelik.newsfeed.structures.request.enums.Country;
import bg.unisofia.fmi.mjt.nikevelik.newsfeed.structures.result.SearchResult;
import bg.unisofia.fmi.mjt.nikevelik.newsfeed.structures.result.article.Article;
import bg.unisofia.fmi.mjt.nikevelik.newsfeed.structures.result.article.ArticleSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class NewsFeedClientImplTest {

    private static final int SEARCH_PAGE_SIZE_POSITIVE = 10;
    private static final int SEARCH_PAGE_NUMBER_POSITIVE = 12;
    private static final int SEARCH_PAGE_NUMBER_NEGATIVE = -7;
    private static final int SEARCH_PAGE_SIZE_NEGATIVE = -8;
    private static final SearchResult TEST_RESULT_POJO = new SearchResult(
        "TEST_STATUS",
        1,
        List.of(
            new Article(
                new ArticleSource("TEST_ID", "TEST_SOURCE_NAME"),
                "TEST_AUTHOR",
                "TEST_TITLE",
                "TEST_DESCRIPTION",
                "https://test.url/article",
                "https://test.url/image.jpg",
                "2024-06-24T12:34:56Z",
                "TEST_CONTENT"
            )
        )
    );

    private static final SearchRequest TEST_REQUEST =
        new SearchRequest.Builder()
            .query("TEST")
            .category(null)
            .country(null)
            .pageNumber(null)
            .pageSize(null)
            .build();

    private static final String TEST_RESULT_JSON = "{\"TEST\": 0}";

    @Mock
    private NewsApiCaller newsApiCaller;

    @Mock
    private ResultParser parser;

    @InjectMocks
    private NewsFeedClientImpl client;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        client = new NewsFeedClientImpl(newsApiCaller, parser);
    }

    @Test
    void searchShouldDelegate() throws SearchException {

        when(newsApiCaller.call(TEST_REQUEST)).thenReturn(TEST_RESULT_JSON);
        when(parser.parse(TEST_RESULT_JSON)).thenReturn(TEST_RESULT_POJO);

        SearchResult actual = client.search(TEST_REQUEST);
        assertEquals(TEST_RESULT_POJO, actual,
            "object returned by client does not match expected POJO");

        verify(newsApiCaller).call(TEST_REQUEST);
        verify(parser).parse(TEST_RESULT_JSON);
    }

    static Stream<Arguments> insufficientSearchRequestCases() {
        return Stream.of(
            Arguments.of(null, null, null),
            Arguments.of("", null, null),
            Arguments.of("   ", null, null)
        );
    }

    static Stream<Arguments> sufficientSearchRequestCases() {
        return Stream.of(
            Arguments.of("q", null, null),
            Arguments.of(null, Category.BUSINESS, null),
            Arguments.of(null, null, Country.US),
            Arguments.of("q", Category.BUSINESS, null),
            Arguments.of("q", null, Country.US),
            Arguments.of(null, Category.BUSINESS, Country.US),
            Arguments.of("q", Category.BUSINESS, Country.US)
        );
    }

    static Stream<Arguments> invalidRequests() {
        return Stream.of(
            Arguments.of((SearchRequest) null),
            Arguments.of(
                new SearchRequest.Builder()
                    .query("TEST-QUERY")
                    .category(null)
                    .country(null)
                    .pageNumber(null)
                    .pageSize(SEARCH_PAGE_SIZE_NEGATIVE)
                    .build()
            ),
            Arguments.of(
                new SearchRequest.Builder()
                    .query("TEST-QUERY")
                    .category(null)
                    .country(null)
                    .pageNumber(SEARCH_PAGE_NUMBER_NEGATIVE)
                    .pageSize(null)
                    .build()
            )
        );
    }

    @ParameterizedTest
    @MethodSource("insufficientSearchRequestCases")
    void searchWhenInsufficientThrowsException(
        String query, Category category, Country country) {
        SearchRequest insufficientRequest = new SearchRequest.Builder()
            .query(query)
            .category(category)
            .country(country)
            .build();
        assertThrows(InvalidSearchRequestException.class,
            () -> client.search(insufficientRequest),
            "The client did not throw expected exception");
    }

    @ParameterizedTest
    @MethodSource("sufficientSearchRequestCases")
    void searchWhenSufficientDoesNotThrowException(
        String query, Category category, Country country) {
        SearchRequest insufficientRequest = new SearchRequest.Builder()
            .query(query)
            .category(category)
            .country(country)
            .build();
        assertDoesNotThrow(() -> client.search(insufficientRequest),
            "The client threw exception unexpectedly");
    }

    @Test
    void searchWhenPageParamsNotNullShouldNotThrow() {
        final SearchRequest nullPagePatamsRequest =
            new SearchRequest.Builder()
                .query("TEST-QUERY")
                .category(null)
                .country(null)
                .pageNumber(SEARCH_PAGE_NUMBER_POSITIVE)
                .pageSize(SEARCH_PAGE_SIZE_POSITIVE)
                .build();

        assertDoesNotThrow(() -> client.search(nullPagePatamsRequest),
            "The client threw exception unexpectedly");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   ", "\t", "\n"})
    void constructorWhenApiKeyNullOrEmptyOrBlankShouldThrow(String apiKey) {
        assertThrows(
            InvalidApiKeyException.class, () -> new NewsFeedClientImpl(apiKey),
            "The client did not throw exception of the desired type"
        );
    }

    @ParameterizedTest(name = "{index} => request={0}")
    @MethodSource("invalidRequests")
    void searchWhenInvalidRequestShouldThrowException(SearchRequest request) {
        assertThrows(InvalidSearchRequestException.class,
            () -> client.search(request),
            "The client did not throw exception of the desired type");
    }

}

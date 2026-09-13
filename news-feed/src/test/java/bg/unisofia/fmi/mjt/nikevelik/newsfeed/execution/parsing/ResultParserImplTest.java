package bg.unisofia.fmi.mjt.nikevelik.newsfeed.execution.parsing;

import bg.unisofia.fmi.mjt.nikevelik.newsfeed.execution.parsing.exception.EmptyInputParsingException;
import bg.unisofia.fmi.mjt.nikevelik.newsfeed.execution.parsing.exception.InvalidInvariantException;
import bg.unisofia.fmi.mjt.nikevelik.newsfeed.execution.parsing.exception.InvalidJsonFormatException;
import bg.unisofia.fmi.mjt.nikevelik.newsfeed.structures.result.SearchResult;
import bg.unisofia.fmi.mjt.nikevelik.newsfeed.structures.result.article.Article;
import bg.unisofia.fmi.mjt.nikevelik.newsfeed.structures.result.article.ArticleSource;
import bg.unisofia.fmi.mjt.nikevelik.newsfeed.execution.parsing.exception.ParsingException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ResultParserImplTest {

    private static final SearchResult CORRECT_PARSING_RESULT_TEST =
        new SearchResult(
            "ok",
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
    private static final String CORRECT_TEST_INPUT_JSON = """
        {
          "status": "ok",
          "totalResults": 1,
          "articles": [
            {
              "source": {
                "id": "TEST_ID",
                "name": "TEST_SOURCE_NAME"
              },
              "author": "TEST_AUTHOR",
              "title": "TEST_TITLE",
              "description": "TEST_DESCRIPTION",
              "url": "https://test.url/article",
              "urlToImage": "https://test.url/image.jpg",
              "publishedAt": "2024-06-24T12:34:56Z",
              "content": "TEST_CONTENT"
            }
          ]
        }
        """;
    private final ResultParser parser = new ResultParserImpl();

    static Stream<Arguments> invalidInvariants() {
        return Stream.of(
            Arguments.of("""
                {
                }
                """),
            Arguments.of("""
                {
                  "status": "NOT_OK_VALUE"
                }
                """),
            Arguments.of("""
                {
                    "status": "ok"
                }
                """),
            Arguments.of("""
                {
                    "status": "ok",
                    "totalResults": 2
                }
                """)
        );
    }

    @Test
    void parseGetsCorrectResult() throws ParsingException {
        SearchResult result = parser.parse(CORRECT_TEST_INPUT_JSON);
        assertEquals(CORRECT_PARSING_RESULT_TEST, result,
            "the result does not match the correct parsing result");
    }

    @Test
    void parseWhenInvalidJsonThrowsParsingException() {
        String invalidJson = "{not valid JSON}";
        assertThrows(InvalidJsonFormatException.class,
            () -> parser.parse(invalidJson),
            "the parser did not throw the desired exception");
    }

    @ParameterizedTest
    @MethodSource("invalidInvariants")
    void parseWhenMissingFieldsThrowsParsingException(String jsonStringInput) {

        assertThrows(InvalidInvariantException.class,
            () -> parser.parse(jsonStringInput),
            "the parser did not throw the desired exception");
    }

    @ParameterizedTest
    @NullAndEmptySource
    void parseWhenNullInputThrowsParsingException(String jsonStringInput) {
        assertThrows(EmptyInputParsingException.class,
            () -> parser.parse(jsonStringInput),
            "the parser did not throw the desired exception");
    }
}

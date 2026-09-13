package bg.unisofia.fmi.mjt.nikevelik.newsfeed.execution.parsing;

import bg.unisofia.fmi.mjt.nikevelik.newsfeed.execution.parsing.exception.EmptyInputParsingException;
import bg.unisofia.fmi.mjt.nikevelik.newsfeed.execution.parsing.exception.InvalidInvariantException;
import bg.unisofia.fmi.mjt.nikevelik.newsfeed.execution.parsing.exception.InvalidJsonFormatException;
import bg.unisofia.fmi.mjt.nikevelik.newsfeed.execution.parsing.exception.ParsingException;
import bg.unisofia.fmi.mjt.nikevelik.newsfeed.structures.result.SearchResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/****
 * Implementation of the {@link ResultParser} interface responsible for parsing
 * JSON responses into {@link SearchResult} objects.
 * <p>
 * The parser validates the response input, deserializes the JSON, and verifies
 * the result invariants.
 */
public class ResultParserImpl implements ResultParser {

    /**
     * The status value indicating a successful search result ("ok").
     */
    private static final String OK_STATUS_VALUE = "ok";

    /**
     * The Jackson {@link ObjectMapper} used for JSON parsing.
     */
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * Parses the provided JSON response string into a structured
     * {@link SearchResult} object,
     * validating both the input and result invariants.
     *
     * @param jsonResponse the JSON response string to parse
     * @return the parsed {@link SearchResult}
     * @throws ParsingException if the input is blank, the JSON is invalid,
     *                          or the result is inconsistent
     */
    @Override
    public SearchResult parse(String jsonResponse) throws ParsingException {
        validateInput(jsonResponse);
        SearchResult searchResult = tryParse(jsonResponse);
        validateSearchResult(searchResult);
        return searchResult;
    }

    /**
     * Validates the input JSON string.
     *
     * @param jsonResponse the JSON response string to check
     * @throws EmptyInputParsingException if the input is {@code null} or blank
     */
    private void validateInput(String jsonResponse)
        throws EmptyInputParsingException {
        if (jsonResponse == null || jsonResponse.isBlank()) {
            throw new EmptyInputParsingException(
                "Input JSON response is null or blank"
            );
        }
    }

    /**
     * Tries to deserialize the JSON into a {@link SearchResult}.
     *
     * @param jsonResponse the JSON string to parse
     * @return the parsed {@link SearchResult}
     * @throws InvalidJsonFormatException if JSON is invalid or cannot be parsed
     */
    private SearchResult tryParse(String jsonResponse)
        throws InvalidJsonFormatException {
        try {
            return OBJECT_MAPPER.readValue(jsonResponse, SearchResult.class);
        } catch (JsonProcessingException e) {
            throw new InvalidJsonFormatException(
                "Invalid JSON format: " + e.getMessage(), e
            );
        }
    }

    /**
     * Validates the invariants of the parsed {@link SearchResult}.
     *
     * @param searchResult the result object to validate
     * @throws InvalidInvariantException if the result is inconsistent or missing fields
     */
    private void validateSearchResult(SearchResult searchResult)
        throws InvalidInvariantException {
        if (!OK_STATUS_VALUE.equals(searchResult.status())) {
            throw new InvalidInvariantException(
                "The json provided does not contain a marker for OK state"
            );
        }
        if (searchResult.totalResults() == null) {
            throw new InvalidInvariantException(
                "The json provided does not contain expected totalResults field"
            );
        }
        if (searchResult.articles() == null) {
            throw new InvalidInvariantException(
                "The json provided does not contain expected articles field"
            );
        }
    }
}

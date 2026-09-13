package bg.unisofia.fmi.mjt.nikevelik.newsfeed.execution.parsing;

import bg.unisofia.fmi.mjt.nikevelik.newsfeed.execution.parsing.exception.ParsingException;
import bg.unisofia.fmi.mjt.nikevelik.newsfeed.structures.result.SearchResult;

/****
 * An interface representing a parser for parsing raw JSON API responses into structured {@link SearchResult} objects.
 */
public interface ResultParser {

    /**
     * Parses the specified JSON response string into a {@link SearchResult} object.
     *
     * @param jsonResponse the raw JSON response string to parse
     * @return the parsed {@link SearchResult} object
     * @throws ParsingException if the response cannot be parsed successfully
     */
    SearchResult parse(String jsonResponse) throws ParsingException;
}

package bg.unisofia.fmi.mjt.nikevelik.newsfeed;

import bg.unisofia.fmi.mjt.nikevelik.newsfeed.exception.SearchException;
import bg.unisofia.fmi.mjt.nikevelik.newsfeed.structures.request.SearchRequest;
import bg.unisofia.fmi.mjt.nikevelik.newsfeed.structures.result.SearchResult;

/****
 * Represents a client capable of executing search operations against a news feed API.
 */
public interface NewsFeedClient {

    /**
     * Searches for news articles using the provided search criteria.
     *
     * @param searchRequest the search request containing parameters for the query
     * @return a {@link SearchResult} representing the search outcome
     * @throws SearchException if an error occurs during search execution or result parsing
     */
    SearchResult search(SearchRequest searchRequest) throws SearchException;

}

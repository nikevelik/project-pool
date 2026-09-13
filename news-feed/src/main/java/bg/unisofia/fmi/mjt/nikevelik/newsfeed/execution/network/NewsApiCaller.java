package bg.unisofia.fmi.mjt.nikevelik.newsfeed.execution.network;

import bg.unisofia.fmi.mjt.nikevelik.newsfeed.execution.network.exception.HttpException;
import bg.unisofia.fmi.mjt.nikevelik.newsfeed.structures.request.SearchRequest;

/****
 * Interface representing a generic API caller that executes network requests.
 */
public interface NewsApiCaller {
    /**
     * Executes a network call using the provided {@link SearchRequest}.
     *
     * @param request the search request containing all necessary data for the API call
     * @return the response from the API as a {@code String}
     * @throws HttpException if an error occurs during the network call
     */
    String call(SearchRequest request) throws HttpException;
}

package bg.unisofia.fmi.mjt.nikevelik.newsfeed.structures.result;

import bg.unisofia.fmi.mjt.nikevelik.newsfeed.structures.result.article.Article;

import java.util.List;

/****
 Represents the result of a search query, including status, total number of results, and a list of articles.
 @param status the status of the search response
 @param totalResults the total number of results found
 @param articles the list of articles resulting from the search
 */
public record SearchResult(
    String status,
    Integer totalResults,
    List<Article> articles
) {
}

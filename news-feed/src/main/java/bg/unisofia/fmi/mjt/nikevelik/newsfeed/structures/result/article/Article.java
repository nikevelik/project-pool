package bg.unisofia.fmi.mjt.nikevelik.newsfeed.structures.result.article;

/****
 * Represents a news article, containing metadata and content details.
 *
 * @param source the source information of the article
 * @param author the author of the article
 * @param title the title of the article
 * @param description the article's description or summary
 * @param url the URL to the full article
 * @param urlToImage the URL to the article's image
 * @param publishedAt the publication date and time of the article
 * @param content the full content of the article
 */
public record Article(
    ArticleSource source,
    String author,
    String title,
    String description,
    String url,
    String urlToImage,
    String publishedAt,
    String content
) {
}

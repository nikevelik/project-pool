//package bg.unisofia.fmi.mjt.nikevelik.newsfeed;
//
//import bg.unisofia.fmi.mjt.nikevelik.newsfeed.exception.SearchException;
//import bg.unisofia.fmi.mjt.nikevelik.newsfeed.structures.request.SearchRequest;
//import bg.unisofia.fmi.mjt.nikevelik.newsfeed.structures.result.SearchResult;
//import bg.unisofia.fmi.mjt.nikevelik.newsfeed.structures.result.article.Article;
//
//import java.util.Iterator;
//
//public class Main {
//    public static void main(String[] args) throws SearchException {
//        Client client = ClientImpl.createWithApiKeyFromSystemEnvironment("NEWS_FEED_API_KEY");
//        SearchRequest request = new SearchRequest.Builder()
//            .query("trump")
//            .country(null)
//            .category(null)
//            .pageNumber(2)
//            .pageSize(5)
//            .build();
//        SearchResult result = client.search(request);
//        System.out.println("STATUS: " + result.status());
//        System.out.println("TOTAL: " + result.totalResults());
//        Iterator<Article> article = result.articles().iterator();
//        while (article.hasNext()) {
//            Article current = article.next();
//            article.remove();
//            System.out.println("-------");
//            System.out.println("ARTICLE");
//            System.out.println("-------");
//            System.out.println(current);
//        }
//    }
//}

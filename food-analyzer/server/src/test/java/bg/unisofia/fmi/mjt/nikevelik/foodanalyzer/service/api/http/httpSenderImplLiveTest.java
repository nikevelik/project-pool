package bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service.api.http;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class httpSenderImplLiveTest {

    static final String API_KEY = System.getenv("USDA_API_KEY");
    HttpSenderImpl sender;

    @BeforeEach
    void setUp() {
        sender = new HttpSenderImpl();
    }

    @Test
    void testUsdaFoodSearchEndpoint() throws Exception {
        assertNotNull(API_KEY, "USDA_API_KEY env variable must be set for LIVE tests.");
        String searchUrl = "https://api.nal.usda.gov/fdc/v1/foods/search/" +
                "?query=raffaello%20treat&requireAllWords=true&api_key=" + API_KEY + "&pageSize=3";



        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(searchUrl))
                .GET()
                .build();

        String response = sender.send(req).get(10, TimeUnit.SECONDS);

        assertNotNull(response);

        assertTrue(response.contains("\"totalHits\":"));
        assertTrue(response.contains("\"currentPage\":"));
        assertTrue(response.contains("\"foodSearchCriteria\":"));
        assertTrue(response.contains("\"foods\":"));

        assertTrue(response.contains("RAFFAELLO"));
        assertTrue(response.contains("ALMOND COCONUT TREAT"));
        assertTrue(response.contains("\"fdcId\":"));
        assertTrue(response.contains("\"description\":"));
        assertTrue(response.contains("\"dataType\":\"Branded\""));
        assertTrue(response.contains("\"brandOwner\":\"Ferrero U.S.A., Incorporated\""));
        assertTrue(response.contains("\"foodNutrients\":"));

    }

    @Test
    void testUsdaFoodDetailsEndpoint() throws Exception {
        String foodDetailsUrl = "https://api.nal.usda.gov/fdc/v1/food/415269?api_key=" + API_KEY;

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(foodDetailsUrl))
                .GET()
                .build();

        String response = sender.send(req).get(10, TimeUnit.SECONDS);

        assertNotNull(response);

        assertTrue(response.contains("\"fdcId\":415269"));
        assertTrue(response.contains("\"description\":\"RAFFAELLO, ALMOND COCONUT TREAT\""));
        assertTrue(response.contains("\"dataType\":\"Branded\""));
        assertTrue(response.contains("\"brandOwner\":\"Ferrero U.S.A., Incorporated\""));

        assertTrue(response.contains("\"foodNutrients\":"));
        assertTrue(response.contains("\"ingredients\":"));
        assertTrue(response.contains("\"labelNutrients\":"));

        assertTrue(response.contains("\"servingSize\":"));
        assertTrue(response.contains("\"householdServingFullText\":\"3 PIECES\""));
        assertTrue(response.contains("\"gtinUpc\":\"009800146130\""));

        assertTrue(response.contains("\"name\":\"Protein\""));
        assertTrue(response.contains("\"name\":\"Total lipid (fat)\""));
        assertTrue(response.contains("\"name\":\"Energy\""));
        assertTrue(response.contains("\"calories\":"));
    }
}

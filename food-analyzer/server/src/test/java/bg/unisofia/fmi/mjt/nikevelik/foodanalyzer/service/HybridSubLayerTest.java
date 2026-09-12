package bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service.api.FoodApi;
import bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service.api.http.HttpSenderImpl;
import bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service.parsing.Food;
import bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service.parsing.FoodParser;
import bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service.parsing.FoodParserImpl;
import bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service.parsing.SearchResultEntry;
import bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service.storage.InMemoryStorage;
import bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service.storage.Storage;

@ExtendWith(MockitoExtension.class)
class HybridSubLayerTest {

    static final String QUERY = "QUERY-TEST";
    static final String API_RESPONSE = "API-RESPONSE-TEST";
    static final String BARCODE = "BARCODE-TEST";
    static final String ID = "ID-TEST";
    static final String DESCRIPTION = "DESCRIPTION-TEST";
    static final String ENERGY = "ENERGY-TEST";
    static final String FIBER = "FIBER-TEST";
    static final String PROTEIN = "PROTEIN-TEST";
    static final String FATS = "FATS-TEST";
    static final String CARBS = "CARBS-TEST";
    static final SearchResultEntry TEST_ENTRY = new SearchResultEntry(ID, BARCODE, DESCRIPTION);
    static final List<SearchResultEntry> TEST_LIST = List.of(TEST_ENTRY);
    static final Food TEST_FOOD = new Food(ID, ENERGY, PROTEIN, FATS, CARBS, FIBER);
    @Mock
    FoodService api;

    @Mock
    Storage<List<SearchResultEntry>> queryCache;

    @Mock
    Storage<Food> foodCache;

    @Mock
    Storage<String> barcodeCache;

    @Mock
    FoodParser parser;

    HybridSubLayer service;


    @BeforeEach
    void setUp() {
        service = new HybridSubLayer(api, queryCache, foodCache, barcodeCache, parser);
    }

    @Test
    void search_whenNotInCache_shouldSearchInApiAndCacheIt() {
        when(queryCache.get(QUERY)).thenReturn(CompletableFuture.completedFuture(null));
        when(api.search(QUERY)).thenReturn(CompletableFuture.completedFuture(API_RESPONSE));
        when(parser.parseSearch(API_RESPONSE)).thenReturn(TEST_LIST);

        assertEquals(TEST_LIST, service.search(QUERY).join());

        verify(barcodeCache).put(BARCODE, ID);
        verify(queryCache).put(QUERY, TEST_LIST);
    }

    @Test
    void search_whenCached_shouldGetFromCache() {
        when(queryCache.get(QUERY)).thenReturn(CompletableFuture.completedFuture(TEST_LIST));

        assertEquals(TEST_LIST, service.search(QUERY).join());

        verify(api, never()).search(any());
        verify(parser, never()).parseSearch(any());
        verify(barcodeCache, never()).put(any(), any());
        verify(queryCache, never()).put(any(), any());
    }

    @Test
    void get_whenNotInCache_shouldGetFromApiAndCacheIt() {
        when(foodCache.get(ID)).thenReturn(CompletableFuture.completedFuture(null));
        when(api.get(ID)).thenReturn(CompletableFuture.completedFuture(API_RESPONSE));
        when(parser.parseFood(API_RESPONSE)).thenReturn(TEST_FOOD);

        Food result = service.get(ID).join();
        assertEquals(TEST_FOOD, result);

        verify(foodCache).put(ID, TEST_FOOD);
        verify(api).get(ID);
        verify(parser).parseFood(API_RESPONSE);
    }

    @Test
    void get_whenCached_shouldGetFromCache() {
        when(foodCache.get(ID)).thenReturn(CompletableFuture.completedFuture(TEST_FOOD));

        assertEquals(TEST_FOOD, service.get(ID).join());

        verify(api, never()).get(any());
        verify(parser, never()).parseFood(any());
        verify(foodCache, never()).put(any(), any());
    }

    @Test
    void getByBarcode_whenNotInCache_shouldReturnMessage() {
        when(barcodeCache.get(BARCODE)).thenReturn(CompletableFuture.completedFuture(null));

        assertNull(service.getByBarcode(BARCODE).join());

        verify(foodCache, never()).get(any());
    }

    @Test
    void getByBarcode_whenCached_shouldGetFromCache() {

        when(barcodeCache.get(BARCODE)).thenReturn(CompletableFuture.completedFuture(ID));
        when(foodCache.get(ID)).thenReturn(CompletableFuture.completedFuture(TEST_FOOD));

        Food result = service.getByBarcode(BARCODE).join();
        assertEquals(TEST_FOOD, result);

        verify(foodCache).get(ID);
    }
    @Test
    void testDefaultHybridSubLayer_returnsNonNull() {
        HybridSubLayer layer = HybridSubLayer.defaulHybridSubLayer("my-key");
        assertNotNull(layer, "HybridSubLayer instance should not be null.");
    }
}

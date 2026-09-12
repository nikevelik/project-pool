package bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service.parsing.Food;
import bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service.parsing.SearchResultEntry;

@ExtendWith(MockitoExtension.class)
class HybridServiceTest {

    @Mock
    HybridSubLayer subLayer;

    @InjectMocks
    HybridService service;

    static String QUERY = "apple";
    static String ID = "12341234";
    static String BARCODE = "123412341234";
    static List<SearchResultEntry> SEARCH_LIST = List.of(new SearchResultEntry(ID, BARCODE, "APPLEPAPLE"));
    static Food FOOD_ITEM = new Food("appple apple apple", "99kcal", "0g", "0g", "20g", "5g");

    @Test
    void search_shouldReturnStringList(){
        when(subLayer.search(QUERY)).thenReturn(CompletableFuture.completedFuture(SEARCH_LIST));
        assertEquals(SearchResultEntry.listToString(SEARCH_LIST), service.search(QUERY).join());
        verify(subLayer).search(QUERY);
    }

    @Test
    void get_shouldReturnFoodString(){
        when(subLayer.get(ID)).thenReturn(CompletableFuture.completedFuture(FOOD_ITEM));
        assertEquals(FOOD_ITEM.toString(), service.get(ID).join());
        verify(subLayer).get(ID);
    }

    @Test
    void getByBarcode_shouldReturnFoodString(){
        when(subLayer.getByBarcode(BARCODE)).thenReturn(CompletableFuture.completedFuture(FOOD_ITEM));
        assertEquals(FOOD_ITEM.toString(), service.getByBarcode(BARCODE).join());
        verify(subLayer).getByBarcode(BARCODE);
    }

    @Test
    void getByBarcode_whenSublayerProvidesNull_shouldReturnInfoMessage(){
        when(subLayer.getByBarcode(BARCODE)).thenReturn(CompletableFuture.completedFuture(null));
        assertEquals(HybridService.NO_FOOD_FOR_THIS_BARCODE_AVAILABLE_IN_STORAGE, service.getByBarcode(BARCODE).join());
        verify(subLayer).getByBarcode(BARCODE);
    }

}

package bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service.parsing;

import java.util.List;

public interface FoodParser {
    List<SearchResultEntry> parseSearch(String json);
    Food parseFood(String json);
}

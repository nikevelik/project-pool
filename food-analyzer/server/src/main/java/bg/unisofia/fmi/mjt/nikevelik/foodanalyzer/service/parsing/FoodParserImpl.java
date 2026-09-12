package bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service.parsing;
import static bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service.parsing.FoodConstants.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FoodParserImpl implements FoodParser {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static final String FOODS = "foods";

    @Override
    public List<SearchResultEntry> parseSearch(String json) {
        try {
            JsonNode foods = OBJECT_MAPPER.readTree(json).get(FOODS);
            if(foods == null){
                throw new BadJsonInputException("The input provided does not contain required `" + FOODS + "` field for foods parsing");
            }
            if(!foods.isArray()){
                throw new BadJsonInputException("The field `" + FOODS + "` is not an array");
            }
            return getSearchResultEntryList(foods);
        } catch (IOException e) {
            throw new BadJsonInputException("The string provided could not be processed as a JSON Node", e);
        }
    }

    @Override
    public Food parseFood(String json){
        try {
            JsonNode root = OBJECT_MAPPER.readTree(json);
            String ingredients = pathAsText(root, INGREDIENTS);
            JsonNode nutrients = root.get(FOOD_NUTRIENTS);
            if(nutrients == null){
                throw new BadJsonInputException("The input provided does not contain required `" + FOOD_NUTRIENTS + "` field for foods parsing");
            }
            if(!nutrients.isArray()){
                throw new BadJsonInputException("The field `" + FOOD_NUTRIENTS + "` is not an array");
            }
            Map<String, String> nutrientValues = parseNutrientValues(nutrients);
            return Food.fromNutrientValuesMap(ingredients, nutrientValues);
        }catch (IOException e){
            throw new BadJsonInputException("The input provided could not be processed as a JSON Node", e);
        }
    }

    private List<SearchResultEntry> getSearchResultEntryList(JsonNode foods) {
        List<SearchResultEntry> results = new ArrayList<>();
        for (JsonNode food : foods) {
            String id = pathAsText(food, FDC_ID);
            String barcode = pathAsText(food, GTIN_UPC);
            String description = pathAsText(food, DESCRIPTION);
            results.add(new SearchResultEntry(id, barcode, description));
        }
        return results;
    }

    private Map<String, String> parseNutrientValues(JsonNode nutrients){
        Map<String, String> nutrientValues = new HashMap<>();
        for (JsonNode nutrientNode : nutrients) {
            String number = extractNutrientNumber(nutrientNode);
            if (number != null && NUTRIENT_MAP.get(number) != null){
                nutrientValues.put(NUTRIENT_MAP.get(number), extractNutrientValue(nutrientNode));
            }
        }
        return nutrientValues;
    }

    private String extractNutrientNumber(JsonNode nutrientNode){
        return extractEither(nutrientNode, NUTRIENT_NUMBER_SUBFIELD, NUTRIENT_NUMBER_ONEFIELD);
    }

    private String extractNutrientValue(JsonNode nutrientNode) {
        String amount = pathAsText(nutrientNode, AMOUNT);
        String unit = extractEither(nutrientNode, NUTRIENT_UNIT_NAME, UNIT_NAME);
        return (amount == null || unit == null) ? null : amount + unit;
    }

    private String extractEither(JsonNode source, String firstOpt, String secondOpt){
        return source.at(firstOpt).asText(
                pathAsText(source, secondOpt)
        );
    }

    private String pathAsText(JsonNode source, String path){
        return source.path(path).asText(null);
    }

}

package bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service.parsing;

import static bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service.parsing.FoodConstants.CARBS;
import static bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service.parsing.FoodConstants.ENERGY;
import static bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service.parsing.FoodConstants.FATS;
import static bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service.parsing.FoodConstants.FIBER;
import static bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service.parsing.FoodConstants.PROTEINS;

import java.util.Map;

public record Food(String ingredients, String energy, String protein, String fat, String carbs, String fiber) {
    public static Food fromNutrientValuesMap(String ingredients, Map<String, String> nutrientValues){
        return new Food(ingredients, nutrientValues.get(ENERGY), nutrientValues.get(PROTEINS), nutrientValues.get(FATS), nutrientValues.get(CARBS), nutrientValues.get(FIBER));
    }
    @Override
    public String toString() {
        return String.format(
                """
                Information:
                    Ingredients: %s
                    Energy: %s
                    Protein: %s
                    Fat: %s
                    Carbs: %s
                    Fiber: %s""",
                getOrDefault(ingredients),
                getOrDefault(energy),
                getOrDefault(protein),
                getOrDefault(fat),
                getOrDefault(carbs),
                getOrDefault(fiber)
        );
    }

    private static String getOrDefault(String field){
        return field != null ? field : "N/A";
    }
}

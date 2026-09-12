package bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service.parsing;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FoodTest {

    @Test
    void toString_shouldReturnExpected() {
        Food food = new Food("Sugar, Salt", "100", null, "5", "80", null);
        String expected = String.join(System.lineSeparator(),
                "Information:",
                "    Ingredients: Sugar, Salt",
                "    Energy: 100",
                "    Protein: N/A",
                "    Fat: 5",
                "    Carbs: 80",
                "    Fiber: N/A"
        );
        assertEquals(expected, food.toString());
    }

    @Test
    void fromNutrientValuesMap_shouldReturnExpected() {
        Map<String, String> nutrientMap = new HashMap<>();
        nutrientMap.put(FoodConstants.ENERGY, "120");
        nutrientMap.put(FoodConstants.PROTEINS, "7");
        nutrientMap.put(FoodConstants.FATS, "2");
        nutrientMap.put(FoodConstants.CARBS, "25");
        nutrientMap.put(FoodConstants.FIBER, "1");

        Food food = Food.fromNutrientValuesMap("Egg, Water", nutrientMap);

        assertEquals("Egg, Water", food.ingredients());
        assertEquals("120", food.energy());
        assertEquals("7", food.protein());
        assertEquals("2", food.fat());
        assertEquals("25", food.carbs());
        assertEquals("1", food.fiber());
    }
}

package bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service.parsing;

import java.util.Map;

public final class FoodConstants {
    private FoodConstants() {}

    public static final String FDC_ID = "fdcId";
    public static final String GTIN_UPC = "gtinUpc";
    public static final String DESCRIPTION = "description";
    public static final String INGREDIENTS = "ingredients";

    public static final String FOOD_NUTRIENTS = "foodNutrients";
    public static final String AMOUNT = "amount";
    public static final String UNIT_NAME = "unitName";
    public static final String NUTRIENT_UNIT_NAME = "/nutrient/unitName";
    public static final String NUTRIENT_NUMBER_ONEFIELD = "nutrientNumber";
    public static final String NUTRIENT_NUMBER_SUBFIELD = "/nutrient/number";

    public static final String CARBS = "carbs";
    public static final String FATS = "fats";
    public static final String PROTEINS = "proteins";
    public static final String ENERGY = "energy";
    public static final String FIBER = "fiber";

    public static final Map<String, String> NUTRIENT_MAP = Map.of(
            "208", ENERGY,
            "203", PROTEINS,
            "204", FATS,
            "205", CARBS,
            "291", FIBER
    );

}

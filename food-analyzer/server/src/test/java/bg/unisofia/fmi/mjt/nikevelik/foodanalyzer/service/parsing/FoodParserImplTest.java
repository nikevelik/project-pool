package bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service.parsing;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

class FoodParserImplTest {

    static final String TEST_RESOURCES_FOODPARSER_BADINPUT = "src/test/resources/foodparser/bad_input/";
    static final String TEST_RESOURCES_FOODPARSER_INPUT = "src/test/resources/foodparser/input/";
    static final String TEST_RESOURCES_FOODPARSER_OUTPUT = "src/test/resources/foodparser/output/";
    static final FoodParserImpl parser = new FoodParserImpl();

    static Stream<String> getFoodInputOutputFileNames() {
        return Stream.of(
                "food_branded_1",
                "food_branded_2",
                "food_branded_3_food_nutrient_entry_null_number",
                "food_branded_4_food_nutrient_entry_null_amount",
                "food_branded_5_food_nutrient_entry_null_unit",
                "food_foundation_1",
                "food_foundation_2",
                "food_srlegacy_1",
                "food_srlegacy_2",
                "food_survey_1",
                "food_survey_2"
        );
    }

    static Stream<String> getSearchInputOutputFileNames() {
        return Stream.of(
                "search_apple",
                "search_cheddar_cheese",
                "search_orange",
                "search_raffaello_treat",
                "search_strawberries",
                "search_apple_no_descriptions",
                "search_apple_no_ids_in_foods",
                "search_apple_with_foods_entry_that_is_bad_object"
        );
    }

    static Stream<String> getSearchBadInputs(){
        return Stream.of(
                "search_apple_invalid_json_output.json",
                "search_apple_missing_foods_field.json",
                "search_apple_non_array_foods_field.json"
        );
    }

    static Stream<String> getFoodBadInputs(){
        return Stream.of(
                "food_invalid_json.json",
                "food_missing_nutrients_field.json",
                "food_non_array_nutrients_field.json"

        );
    }

    @ParameterizedTest
    @MethodSource("getFoodInputOutputFileNames")
    void parseFood_whenCorrectInput_shouldMatchOutput(String fileBaseName) throws Exception {
        Path inputPath = Path.of(TEST_RESOURCES_FOODPARSER_INPUT, fileBaseName + ".json");
        Path outputPath = Path.of(TEST_RESOURCES_FOODPARSER_OUTPUT, fileBaseName + ".txt");

        String inputJson = Files.readString(inputPath);
        String expectedOutput = Files.readString(outputPath);

        String actualOutput = formatFood(parser.parseFood(inputJson));
        assertEquals(expectedOutput.trim(), actualOutput.trim());
    }


    @ParameterizedTest
    @MethodSource("getSearchInputOutputFileNames")
    void parseSearch_whenCorrectInput_shouldMatchOutput(String fileBaseName) throws Exception {
        Path inputPath = Path.of(TEST_RESOURCES_FOODPARSER_INPUT, fileBaseName + ".json");
        Path outputPath = Path.of(TEST_RESOURCES_FOODPARSER_OUTPUT, fileBaseName + ".txt");

        String inputJson = Files.readString(inputPath);
        String expectedOutput = Files.readString(outputPath);

        String actualOutput = formatSearchResult(parser.parseSearch(inputJson));
        assertEquals(expectedOutput.trim(), actualOutput.trim());
    }

    @ParameterizedTest
    @MethodSource("getSearchBadInputs")
    void parseSearch_whenUnexpectedInput_shouldThrowBadJSONInputException(String fileName) throws Exception {
        String inputJson = Files.readString(Path.of(TEST_RESOURCES_FOODPARSER_BADINPUT+fileName));
        assertThrows(BadJsonInputException.class, () -> parser.parseSearch(inputJson));
    }

    @ParameterizedTest
    @MethodSource("getFoodBadInputs")
    void parseFood_whenUnexpectedInput_shouldThrowBadJSONInputException(String fileName) throws Exception {
        String inputJson = Files.readString(Path.of(TEST_RESOURCES_FOODPARSER_BADINPUT+fileName));
        assertThrows(BadJsonInputException.class, () -> parser.parseFood(inputJson));
    }

    static String formatSearchEntry(SearchResultEntry searchResultEntry){
        return String.format("Food(id: %s, gtinUpc: %s, description: %s)", formatField(searchResultEntry.id()), formatField(searchResultEntry.barcode()), formatField(searchResultEntry.description()));
    }

    static String formatField(String field){
        return field != null ? field : "N/A";
    }

    static String formatSearchResult(Iterable<? extends SearchResultEntry> entries) {
        StringBuilder sb = new StringBuilder("SearchResult:");
        int i = 1;
        for (SearchResultEntry entry : entries) {
            sb.append(System.lineSeparator())
                    .append(i++)
                    .append(". ")
                    .append(formatSearchEntry(entry));
        }
        return sb.toString();
    }

    static String formatFood(Food food){
        return String.format(
                """
                Information:
                    Ingredients: %s
                    Energy: %s
                    Protein: %s
                    Fat: %s
                    Carbs: %s
                    Fiber: %s
                """,
                formatField(food.ingredients()),
                formatField(food.energy()),
                formatField(food.protein()),
                formatField(food.fat()),
                formatField(food.carbs()),
                formatField(food.fiber())
        );
    }

}

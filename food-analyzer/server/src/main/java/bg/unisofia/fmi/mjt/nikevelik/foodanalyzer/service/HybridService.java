package bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service.api.http.exception.HttpException;
import bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service.parsing.BadJsonInputException;
import bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service.parsing.Food;
import bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service.parsing.SearchResultEntry;

public class HybridService implements FoodServiceWithBarodeGetter{

    private static final String CANNOT_BE_NULL_EMPTY_OR_BLANK_STRING = "cannot be null, empty or blank string";
    private static final String PROVIDED_EXCEEDS_THE_PRE_DEFINED_LIMIT = "provided exceeds the pre-defined limit(";
    private final HybridSubLayer subLayer;
    static final String  NO_FOOD_FOR_THIS_BARCODE_AVAILABLE_IN_STORAGE = "Could not find any data in the cache related to the barcode provided";
    private static final int QUERY_SIZE_LIMIT = 100;
    private static final int ID_SIZE_LIMIT = 10;
    private static final int BARCODE_SIZE_LIMIT = 20;

    public HybridService(HybridSubLayer subLayer){
        this.subLayer = subLayer;
    }

    public static HybridService defaultHybridService(String apiKey){
        validate(apiKey, "Api key " + CANNOT_BE_NULL_EMPTY_OR_BLANK_STRING);
        return new HybridService(HybridSubLayer.defaulHybridSubLayer(apiKey));
    }

    @Override
    public CompletableFuture<String> getByBarcode(String barcode) {
        validate(barcode, "Barcode " + CANNOT_BE_NULL_EMPTY_OR_BLANK_STRING);
        validateStringSize(barcode, BARCODE_SIZE_LIMIT, "Barcode " + PROVIDED_EXCEEDS_THE_PRE_DEFINED_LIMIT + BARCODE_SIZE_LIMIT + ")");
        return subLayer.getByBarcode(barcode).thenApply(this::processFoodFromBarcodeResponse);
    }

    private String processFoodFromBarcodeResponse(Food food) {
        return food != null ? food.toString() : NO_FOOD_FOR_THIS_BARCODE_AVAILABLE_IN_STORAGE;
    }

    @Override
    public CompletableFuture<String> search(String query) {
        validate(query, "Query " + CANNOT_BE_NULL_EMPTY_OR_BLANK_STRING);
        validateStringSize(query, QUERY_SIZE_LIMIT, "Query " + PROVIDED_EXCEEDS_THE_PRE_DEFINED_LIMIT + QUERY_SIZE_LIMIT + ")");
        return subLayer.search(query).thenApply(SearchResultEntry::listToString).exceptionally(this::handleExceptionalSituations);
    }

    @Override
    public CompletableFuture<String> get(String id) {
        validate(id, "ID " + CANNOT_BE_NULL_EMPTY_OR_BLANK_STRING);
        validateStringSize(id, ID_SIZE_LIMIT, "ID " + PROVIDED_EXCEEDS_THE_PRE_DEFINED_LIMIT + ID_SIZE_LIMIT + ")");
        return subLayer.get(id).thenApply(Food::toString).exceptionally(this::handleExceptionalSituations);
    }

    private static void validate(String str, String msg) {
        if (str == null || str.isBlank()) {
            throw new IllegalArgumentException(msg);
        }
    }

    private static void validateStringSize(String str, int maxAllowedSize, String msg){
        if(str.length() > maxAllowedSize){
            throw new IllegalArgumentException(msg);
        }
    }

    private String handleExceptionalSituations(Throwable e){
        if(e.getCause() instanceof HttpException){
            return "Error: request to third party endpoint for the resource was not successful";
        }
        if(e.getCause() instanceof BadJsonInputException){
            return "Error: Third party endpoint returned unexpected type of data, that could not be processed";
        }
        throw new CompletionException(e);
    }

}

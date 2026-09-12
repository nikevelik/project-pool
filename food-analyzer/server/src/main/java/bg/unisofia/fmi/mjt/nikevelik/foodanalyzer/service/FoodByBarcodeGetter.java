package bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service;

import java.util.concurrent.CompletableFuture;

public interface FoodByBarcodeGetter {
    CompletableFuture<String> getByBarcode(String barcode);
}

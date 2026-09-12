package bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.server.command.impl;

import java.util.concurrent.CompletableFuture;

import bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.server.command.Command;
import bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service.HybridService;

public class GetByBarcodeCommand extends Command {

    public GetByBarcodeCommand(HybridService hybridService) {
        super(hybridService);
    }

    @Override
    public CompletableFuture<String> execute(String argument) {
        try {
            return hybridService.getByBarcode(argument);
        }catch (IllegalArgumentException e){
            return CompletableFuture.completedFuture("[Invalid input]: " + e.getMessage());
        }
    }
}

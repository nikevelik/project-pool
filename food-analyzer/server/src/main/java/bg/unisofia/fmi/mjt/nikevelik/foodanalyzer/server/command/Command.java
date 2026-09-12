package bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.server.command;

import java.util.concurrent.CompletableFuture;

import bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service.HybridService;

public abstract class Command {
    protected final HybridService hybridService;
    protected Command(HybridService hybridService){
        this.hybridService = hybridService;
    }
    public abstract CompletableFuture<String> execute(String argument);
}

package bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.server.command.impl;

import static org.mockito.Mockito.when;

import java.util.concurrent.CompletableFuture;

import bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.server.command.AbstractCommandTest;
import bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service.HybridService;

class SearchCommandTest extends AbstractCommandTest {

    @Override
    protected SearchCommand createCommand(HybridService hybridService) {
        return new SearchCommand(hybridService);
    }

    @Override
    protected void stubServiceSuccess(String arg, String result) {
        when(hybridService.search(arg)).thenReturn(CompletableFuture.completedFuture(result));
    }

    @Override
    protected void stubServiceIllegalArgument(String arg, String errorMessage) {
        when(hybridService.search(arg)).thenThrow(new IllegalArgumentException(errorMessage));
    }
}

package bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.server.command.impl;

import static org.mockito.Mockito.when;

import java.util.concurrent.CompletableFuture;

import bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.server.command.AbstractCommandTest;
import bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service.HybridService;

class GetCommandTest extends AbstractCommandTest {

    @Override
    protected GetCommand createCommand(HybridService hybridService) {
        return new GetCommand(hybridService);
    }

    @Override
    protected void stubServiceSuccess(String arg, String result) {
        when(hybridService.get(arg)).thenReturn(CompletableFuture.completedFuture(result));
    }

    @Override
    protected void stubServiceIllegalArgument(String arg, String errorMessage) {
        when(hybridService.get(arg)).thenThrow(new IllegalArgumentException(errorMessage));
    }
}

package bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.server.command.impl;

import static org.mockito.Mockito.when;

import java.util.concurrent.CompletableFuture;

import bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.server.command.AbstractCommandTest;
import bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service.HybridService;

class GetByBarcodeCommandTest extends AbstractCommandTest {

    @Override
    protected GetByBarcodeCommand createCommand(HybridService hybridService) {
        return new GetByBarcodeCommand(hybridService);
    }

    @Override
    protected void stubServiceSuccess(String arg, String result) {
        when(hybridService.getByBarcode(arg)).thenReturn(CompletableFuture.completedFuture(result));
    }

    @Override
    protected void stubServiceIllegalArgument(String arg, String errorMessage) {
        when(hybridService.getByBarcode(arg)).thenThrow(new IllegalArgumentException(errorMessage));
    }
}

package bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.server.command;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CompletableFuture;

import bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.server.command.Command;
import bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service.HybridService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public abstract class AbstractCommandTest {

    protected HybridService hybridService;
    protected Command command;

    @BeforeEach
    void setUp() {
        hybridService = mock(HybridService.class);
        command = createCommand(hybridService);
    }

    /** Subclass provides a concrete Command instance using the mock HybridService */
    protected abstract Command createCommand(HybridService hybridService);

    /** Subclass triggers the proper HybridService method for the tested Command */
    protected abstract void stubServiceSuccess(String arg, String result);
    protected abstract void stubServiceIllegalArgument(String arg, String errorMessage);

    @Test
    void testExecuteReturnsResultFromService() {
        String input = "someArg";
        String expected = "someResult";
        stubServiceSuccess(input, expected);
        assertEquals(expected, command.execute(input).join());
    }

    @Test
    void testExecuteReturnsExceptionMessageOnIllegalArgument() {
        String input = "badArg";
        String error = "fail reason";
        stubServiceIllegalArgument(input, error);
        assertEquals("[Invalid input]: " + error, command.execute(input).join());
    }
}

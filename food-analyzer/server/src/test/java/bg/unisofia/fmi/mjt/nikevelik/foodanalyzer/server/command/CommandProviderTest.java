package bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.server.command;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.server.command.Command;
import bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.server.command.CommandProvider;
import bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.server.command.impl.SearchCommand;
import bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.server.command.impl.GetCommand;
import bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.server.command.impl.GetByBarcodeCommand;
import bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.server.command.exception.CommandNotFoundException;
import bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service.HybridService;
import org.junit.jupiter.api.Test;

class CommandProviderTest {

    private final HybridService hybridService = mock(HybridService.class);

    @Test
    void createCommandReturnsSearchCommand() throws CommandNotFoundException {
        CommandProvider provider = new CommandProvider(hybridService);
        Command cmd = provider.createCommand("get-food");
        assertTrue(cmd instanceof SearchCommand);
    }

    @Test
    void createCommandReturnsGetCommand() throws CommandNotFoundException {
        CommandProvider provider = new CommandProvider(hybridService);
        Command cmd = provider.createCommand("get-food-report");
        assertTrue(cmd instanceof GetCommand);
    }

    @Test
    void createCommandReturnsGetByBarcodeCommand() throws CommandNotFoundException {
        CommandProvider provider = new CommandProvider(hybridService);
        Command cmd = provider.createCommand("get-food-by-barcode");
        assertTrue(cmd instanceof GetByBarcodeCommand);
    }

    @Test
    void createCommandThrowsForUnknownCommand() {
        CommandProvider provider = new CommandProvider(hybridService);
        assertThrows(CommandNotFoundException.class, () -> provider.createCommand("unknown"));
    }
}

package bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.server.command;

import bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.server.command.exception.CommandNotFoundException;
import bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.server.command.impl.GetByBarcodeCommand;
import bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.server.command.impl.GetCommand;
import bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.server.command.impl.SearchCommand;
import bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service.HybridService;

public class CommandProvider {
    private final HybridService hybridService;
    public CommandProvider(HybridService hybridService){
        this.hybridService = hybridService;
    }
    public Command createCommand(String name) throws CommandNotFoundException {
        return switch(name){
            case "get-food" -> new SearchCommand(hybridService);
            case "get-food-report" -> new GetCommand(hybridService);
            case "get-food-by-barcode" -> new GetByBarcodeCommand(hybridService);
            default -> throw new CommandNotFoundException("There is no such command named: \"" + name + "\". Available commands: 1. get-food <query>, 2. get-food-report <fdcId>, 3. get-food-by-barcode <gtinUpc>");
        };
    }
}

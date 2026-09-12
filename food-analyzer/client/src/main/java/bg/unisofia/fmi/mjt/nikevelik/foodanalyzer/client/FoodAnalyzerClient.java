package bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.client;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

public class FoodAnalyzerClient {

    private static final String HOST = "localhost";
    private static final int PORT = 8080;
    private static final String QUIT_COMMAND = "quit";

    public static void main(String[] args) {
        System.out.println("Starting the application.");
        try (ClientCall clientCall = new ClientCall(HOST, PORT);
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connected to the server.");

            while (true) {
                System.out.print("Enter request (or '" + QUIT_COMMAND + "' to exit): ");
                String input = scanner.nextLine().trim();

                if (input.equalsIgnoreCase(QUIT_COMMAND))
                    break;
                if (input.isEmpty())
                    continue;

                clientCall.sendRequest(process(input));
                String response = clientCall.readResponse();

                if (response == null) {
                    System.out.println("Connection closed by server.");
                    break;
                }
                System.out.println("The server replied: <<<" + response.trim() + ">>>");
            }

        } catch (IOException e) {
            System.out.println("Network error. Closing the application");
        }
    }
    private static final String GET_FOOD_BY_BARCODE = "get-food-by-barcode ";
    private static final String FLAG_CODE = "--code=";
    private static final String FLAG_IMG = "--img=";

    private static String process(String commandRaw) {
        commandRaw = commandRaw.trim();

        if (commandRaw.startsWith(GET_FOOD_BY_BARCODE)) {
            String[] parts = commandRaw.substring(GET_FOOD_BY_BARCODE.length()).split("\\s+");
            String code = null;
            String imgPath = null;

            for (String part : parts) {
                if (part.startsWith(FLAG_CODE)) {
                    code = part.substring(FLAG_CODE.length()).trim();
                } else if (part.startsWith(FLAG_IMG)) {
                    imgPath = part.substring(FLAG_IMG.length()).trim();
                }
            }

            if (imgPath != null && !imgPath.isEmpty()) {
                BarcodeDecoder decoder = new BarcodeDecoder(Path.of(imgPath));
                String barcode = decoder.decodeBarcode();

                if (barcode == null || barcode.isEmpty()) {
                    System.out.println("The image provided could not be processed");
                    if (code != null) {
                        return GET_FOOD_BY_BARCODE + code;
                    }
                    return commandRaw;
                }
                return GET_FOOD_BY_BARCODE + barcode;
            }

            if (code != null) {
                return GET_FOOD_BY_BARCODE + code;
            }

            return commandRaw;
        }

        return commandRaw;
    }
}

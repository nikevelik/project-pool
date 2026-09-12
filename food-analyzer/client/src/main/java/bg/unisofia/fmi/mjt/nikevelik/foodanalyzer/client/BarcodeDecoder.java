package bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.client;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public final class BarcodeDecoder {
    private static final String ZXING_API_URL = "https://zxing.org/w/decode";
    private static final String CONTENT_TYPE_FORMAT = "multipart/form-data; boundary=%s";
    private static final String FILE_FIELD = "f";
    private static final String LINE_FEED = "\r\n";
    private static final int BUFFER_SIZE = 4096;

    private final File file;

    public BarcodeDecoder(Path path) {
        if (path == null) {
            throw new IllegalArgumentException("Path must not be null.");
        }
        this.file = path.toFile();
        if (!file.exists() || !file.isFile()) {
            throw new IllegalArgumentException("File must exist and be a regular file: " + file);
        }
    }

    public String decodeBarcode() {
        String boundary = "Boundary-" + System.currentTimeMillis();
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(ZXING_API_URL).openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", String.format(CONTENT_TYPE_FORMAT, boundary));
            connection.setDoOutput(true);
            writeMultipartFile(connection.getOutputStream(), file, boundary);
            int status = connection.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                String response = readResponse(connection.getInputStream());
                return extractDecodedText(response);
            } else {
                System.err.printf("HTTP error: %d %s%n", status, connection.getResponseMessage());
            }
        } catch (IOException e) {
            throw new RuntimeException("Error processing barcode decoding request: " + e.getMessage(), e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }

    // --- Helpers ---

    private static void writeMultipartFile(OutputStream out, File file, String boundary) throws IOException {
        StringBuilder header = new StringBuilder();
        header.append("--").append(boundary).append(LINE_FEED)
                .append(String.format("Content-Disposition: form-data; name=\"%s\"; filename=\"%s\"", FILE_FIELD, file.getName()))
                .append(LINE_FEED)
                .append("Content-Type: application/octet-stream").append(LINE_FEED)
                .append(LINE_FEED);

        out.write(header.toString().getBytes(StandardCharsets.UTF_8));
        try (InputStream fis = new BufferedInputStream(new FileInputStream(file))) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int len;
            while ((len = fis.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
        }
        String footer = LINE_FEED + "--" + boundary + "--" + LINE_FEED;
        out.write(footer.getBytes(StandardCharsets.UTF_8));
        out.flush();
    }

    private static String readResponse(InputStream in) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line).append(LINE_FEED);
            }
            return response.toString();
        }
    }

    private static String extractDecodedText(String html) {
        final String OPEN = "<pre>";
        final String CLOSE = "</pre>";
        int start = html.indexOf(OPEN);
        int end = html.indexOf(CLOSE, start);
        if (start != -1 && end != -1 && start + OPEN.length() < end) {
            return html.substring(start + OPEN.length(), end).trim();
        }
        return null;
    }
}

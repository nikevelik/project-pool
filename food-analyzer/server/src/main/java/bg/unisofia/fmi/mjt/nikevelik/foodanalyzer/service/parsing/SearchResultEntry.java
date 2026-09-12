package bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service.parsing;

import java.util.List;

public record SearchResultEntry(String id, String barcode, String description) {
    @Override
    public String toString(){
        return String.format("Food(id: %s, gtinUpc: %s, description: %s)", getOrDefault(id), getOrDefault(barcode), getOrDefault(description));
    }
    private static String getOrDefault(String field){
        return field != null ? field : "N/A";
    }
    public static String listToString(List<SearchResultEntry> entries) {
        if(entries == null){
            return "SearchResult(0)";
        }
        String searchResult = "SearchResult";
        String size = "(" + entries.size() + ")";
        String ifAnySemiColon = entries.isEmpty() ? "" : ":";
        StringBuilder sb = new StringBuilder(searchResult + size + ifAnySemiColon);
        int i = 1;
        for (SearchResultEntry entry : entries) {
            sb.append(System.lineSeparator())
                    .append(i++)
                    .append(". ")
                    .append(entry);
        }
        return sb.toString();
    }
}

package bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service.parsing;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SearchCommandResultEntryTest {

    @Test
    void toString_shouldReturnExpected() {
        SearchResultEntry entry = new SearchResultEntry(null, "7890", null);
        String expected = "Food(id: N/A, gtinUpc: 7890, description: N/A)";
        assertEquals(expected, entry.toString());
    }

    @Test
    void listToString_whenEmpty_shouldReturnExpected() {
        List<SearchResultEntry> entries = Collections.emptyList();
        String expected = "SearchResult(0)";
        assertEquals(expected, SearchResultEntry.listToString(entries));
    }
    @Test
    void listToString_whenNull_shouldReturnExpected() {
        String expected = "SearchResult(0)";
        assertEquals(expected, SearchResultEntry.listToString(null));
    }

    @Test
    void listToString_shouldReturnExpected() {
        List<SearchResultEntry> entries = Collections.singletonList(
                new SearchResultEntry("id1", "code1", "desc1"));
        String expected = String.join(System.lineSeparator(),
                "SearchResult(1):",
                "1. Food(id: id1, gtinUpc: code1, description: desc1)"
        );
        assertEquals(expected, SearchResultEntry.listToString(entries));
    }
}

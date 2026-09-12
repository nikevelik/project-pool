package bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


abstract class StorageTest {

    Storage<String> cache;

    @BeforeEach
    abstract void setUp();

    @Test
    void get_whenValueStored_shouldRetrieveValue() throws Exception {
        cache.put("name", "john").get();
        assertEquals("john", cache.get("name").get());
    }

    @Test
    void get_whenKeyIsMissing_shouldReturnNull() throws Exception {
        assertNull(cache.get("unknown").get());
    }

    @Test
    void put_whenKeyIsNull_shouldReturnNull() throws Exception {
        assertNull(cache.put(null, "pointless").get());
    }
}

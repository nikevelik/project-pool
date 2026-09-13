package bg.unisofia.fmi.mjt.nikevelik.newsfeed.structures.request.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CategoryTest {
    @Test
    void allEnumConstantsAndGetValueShouldWork() {
        for (Category c : Category.values()) {
            assertNotNull(c.getValue());
            assertEquals(c, Category.valueOf(c.name()));
        }
    }
}

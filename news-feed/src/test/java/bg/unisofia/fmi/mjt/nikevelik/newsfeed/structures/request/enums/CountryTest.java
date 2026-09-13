package bg.unisofia.fmi.mjt.nikevelik.newsfeed.structures.request.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CountryTest {
    @Test
    void allEnumConstantsAndGetCodeShouldWork() {
        for (Country c : Country.values()) {
            assertNotNull(c.getCode(),
                "Country code should not be null");
            assertEquals(c, Country.valueOf(c.name()),
                "valueOf should return correct constant");
        }
    }
}

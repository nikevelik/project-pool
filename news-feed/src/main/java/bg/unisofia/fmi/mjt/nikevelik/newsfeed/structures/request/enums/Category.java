package bg.unisofia.fmi.mjt.nikevelik.newsfeed.structures.request.enums;

/**
 * Enum representing supported news categories for search queries.
 */
public enum Category {
    BUSINESS("business"),
    ENTERTAINMENT("entertainment"),
    GENERAL("general"),
    HEALTH("health"),
    SCIENCE("science"),
    SPORTS("sports"),
    TECHNOLOGY("technology");

    private final String value;

    /**
     * Returns the String value associated with this category.
     */
    Category(final String categoryValue) {
        this.value = categoryValue;
    }

    public String getValue() {
        return value;
    }
}

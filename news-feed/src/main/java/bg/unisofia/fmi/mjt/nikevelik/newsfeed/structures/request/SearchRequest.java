package bg.unisofia.fmi.mjt.nikevelik.newsfeed.structures.request;

import bg.unisofia.fmi.mjt.nikevelik.newsfeed.structures.request.enums.Category;
import bg.unisofia.fmi.mjt.nikevelik.newsfeed.structures.request.enums.Country;

/**
 * Represents a search request for querying articles.
 *
 * @param query      the keywords or phrase to search for
 * @param category   the category to filter by, or null if not specified
 * @param country    the country to filter results by, or null if not specified
 * @param pageNumber the requested page number for paginated results (1-based)
 * @param pageSize   the number of results per page
 */
public record SearchRequest(
    String query,
    Category category,
    Country country,
    Integer pageNumber,
    Integer pageSize
) {
    /**
     * Constructs a {@code SearchRequest} from the given builder.
     *
     * @param builder the builder instance
     */
    public SearchRequest(final Builder builder) {
        this(builder.query, builder.category, builder.country, builder.pageNumber, builder.pageSize);
    }

    /**
     * A builder for constructing immutable {@link SearchRequest} instances.
     */
    public static class Builder {
        private String query;
        private Category category;
        private Country country;
        private Integer pageNumber;
        private Integer pageSize;

        /**
         * Creates a new, empty builder.
         */
        public Builder() {
            // Default constructor.
        }

        public Builder query(final String searchQuery) {
            this.query = searchQuery;
            return this;
        }

        public Builder category(final Category searchCategory) {
            this.category = searchCategory;
            return this;
        }

        public Builder country(final Country searchCountry) {
            this.country = searchCountry;
            return this;
        }

        public Builder pageNumber(final Integer searchPageNumber) {
            this.pageNumber = searchPageNumber;
            return this;
        }

        public Builder pageSize(final Integer searchPageSize) {
            this.pageSize = searchPageSize;
            return this;
        }

        /**
         * Builds and returns a new {@link SearchRequest} with the builder's current parameters.
         *
         * @return a new {@link SearchRequest} instance
         */
        public SearchRequest build() {
            return new SearchRequest(this);
        }
    }
}

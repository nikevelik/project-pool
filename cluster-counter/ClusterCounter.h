#include <vector>
#include <stdexcept>
#include <queue>



/**
 * @namespace ClusterCounter
 * 
 * @brief Provides methods to count clusters of connected 'true' values in a 2D grid.
 * 
 * The `ClusterCounter` class uses breadth-first search (BFS) to find and count clusters of 
 * adjacent 'true' values in a grid. It includes input validation, exception handling for 
 * exceeding queue size, and ensures safe memory usage.
 * 
 * @exception QueueSizeExceededException Thrown when the BFS queue exceeds the maximum size.
 */
namespace clusters{

    /**
    * @class QueueSizeExceededException
    * 
    * @brief Exception thrown when the BFS queue exceeds the maximum allowed size.
    * 
    * This exception is used to handle situations where the queue size during the breadth-first 
    * search exceeds the predefined limit, ensuring that the program handles large grids gracefully.
    */
    class QueueSizeExceededException : public std::runtime_error {
        public:
        explicit QueueSizeExceededException(const std::string& message) : std::runtime_error(message) {}
    };

    /**
    * @class ClusterCounter
    * 
    * @brief Provides methods to count clusters of connected 'true' values in a 2D grid.
    * 
    * The `ClusterCounter` class implements the logic for counting clusters using breadth-first 
    * search (BFS). It provides two methods to count clusters: one that modifies the grid directly 
    * and another that uses an additional visited grid. The class also includes input validation and 
    * exception handling for cases like exceeding the maximum queue size or invalid grid dimensions.
    */
    class ClusterCounter{
    public:

        // @constant deltas_number The number of possible directions (4: up, down, left, right) for BFS traversal.
        static constexpr int deltas_number = 4;

        // @constant row_deltas Directional offsets for horizontal movement (right, left).
        static constexpr int row_deltas[deltas_number] = {1, -1, 0, 0};

        // @constant col_deltas Directional offsets for vertical movement (down, up).
        static constexpr int col_deltas[deltas_number] = {0, 0, 1, -1};

        // @constant MAX_CELLS Maximum allowed number of cells (2^31), used to validate grid size.
        static constexpr long long MAX_CELLS = 2147483648LL;

        // @constant MAX_QUEUE_SIZE Maximum size of the BFS queue to prevent overflow.
        static constexpr size_t MAX_QUEUE_SIZE = 100000;

        /**
        * @brief Counts clusters by modifying the grid directly, marking cells as visited during traversal.
        * 
        * This method iterates through the grid and uses breadth-first search (BFS) to find and mark 
        * all cells belonging to the same cluster. The grid is modified in place to mark visited cells.
        * If the BFS queue exceeds the maximum size, an exception is thrown.
        * 
        * @param grid The grid of boolean values representing cells to be checked for clusters.
        * @return The number of clusters found
        */
        static int count_clusters(std::vector<std::vector<bool>>& grid);
        /**
        * @brief Counts clusters without modifying the original grid, using a separate visited grid.
        * 
        * This method iterates through the grid, using an additional grid to track visited cells. 
        * It performs BFS to explore each cluster and ensures the original grid remains unchanged. 
        * If the BFS queue exceeds the maximum size, an exception is thrown.
        * 
        * @param grid The grid of boolean values representing cells to be checked for clusters.
        * @return The number of clusters found
        */
        static int count_clusters(const std::vector<std::vector<bool>>& grid);

    private: 
        
        ClusterCounter() = delete;

        /**
        * @brief Validates the input grid for proper dimensions and size constraints.
        * 
        * This method checks if the grid is non-empty, if all rows have the same number of columns, 
        * and if the total number of cells does not exceed the maximum allowed limit. It throws 
        * an exception if any validation fails.
        * 
        * @param grid The grid to be validated.
        * @throws std::invalid_argument If the grid is empty, rows have inconsistent column sizes, 
        *         or the grid size exceeds the maximum allowed cells.
        */
        static void validate_input(const std::vector<std::vector<bool>>& grid);

        /**
        * @brief Traverses a cluster and marks all its connected cells as visited in the grid.
        * 
        * This method performs a breadth-first search (BFS) starting from the given cell. All cells 
        * connected to the starting cell (forming a cluster) are marked as visited by setting them to 
        * 'false' in the grid. If the BFS queue exceeds the maximum size, an exception is thrown.
        * 
        * @param grid The grid of boolean values representing the cells to be traversed.
        * @param start_x The row index of the starting cell.
        * @param start_y The column index of the starting cell.
        * @param rows The number of rows in the grid.
        * @param cols The number of columns in the grid.
        */
        static void traverse_cluster(std::vector<std::vector<bool>>& grid, int start_x, int start_y, int rows, int cols);
        /**
        * @brief Traverses a cluster using a separate visited grid to track visited cells.
        * 
        * This method performs a breadth-first search (BFS) starting from the given cell, using a 
        * separate visited grid to track visited cells without modifying the original grid. All cells 
        * connected to the starting cell (forming a cluster) are marked as visited in the visited grid. 
        * If the BFS queue exceeds the maximum size, an exception is thrown.
        * 
        * @param grid The grid of boolean values representing the cells to be traversed.
        * @param visited The visited grid used to track visited cells.
        * @param start_x The row index of the starting cell.
        * @param start_y The column index of the starting cell.
        * @param rows The number of rows in the grid.
        * @param cols The number of columns in the grid.
        */
        static void traverse_cluster(const std::vector<std::vector<bool>>& grid, std::vector<std::vector<bool>>& visited, int start_x, int start_y, int rows, int cols);
    };
}
# ClusterCounter

`ClusterCounter` is a C++ library designed to count clusters of connected `1` values in a 2D grid. The algorithm uses Breadth-First Search (BFS) to traverse the grid and find all clusters of adjacent `1` values. It offers two methods to count clusters: one that modifies the grid directly and another that keeps the original grid intact by using an auxiliary visited grid. The implementation includes input validation, exception handling, and memory safety measures.

## Features

- **Cluster Detection**: Efficiently counts clusters of connected `1` values in a 2D grid.
- **Breadth-First Search (BFS)**: Uses BFS for traversing and detecting clusters.
- **Grid Modification**: Optionally modifies the grid during cluster detection or uses an auxiliary visited grid.
- **Exception Handling**: Includes checks for exceeding queue size and ensures that the grid meets the expected constraints.
- **Memory Safety**: Protects against excessive queue sizes and ensures that grid dimensions are within acceptable limits.

## Classes

### `ClusterCounter`

The main class that provides methods to count clusters of connected `1` values in the grid.

#### Methods:

1. **`static int count_clusters(std::vector<std::vector<bool>>& grid)`**:
   - Counts the clusters by modifying the grid directly, marking cells as visited during traversal.
   - Throws `QueueSizeExceededException` if the BFS queue exceeds the predefined limit.
   
   **Parameters**:
   - `grid`: A 2D grid (vector of vectors) representing boolean values (either `1` or `0`).

   **Returns**:
   - The number of clusters found in the grid.

2. **`static int count_clusters(const std::vector<std::vector<bool>>& grid)`**:
   - Counts clusters without modifying the original grid, using a separate `visited` grid to track visited cells.
   - Throws `QueueSizeExceededException` if the BFS queue exceeds the predefined limit.

   **Parameters**:
   - `grid`: A 2D grid (vector of vectors) representing boolean values (either `1` or `0`).

   **Returns**:
   - The number of clusters found in the grid.

#### Private Methods:
- **`static void validate_input(const std::vector<std::vector<bool>>& grid)`**: 
   - Ensures the grid is non-empty, that all rows have the same number of columns, and that the grid size does not exceed the maximum allowed limit.
   
- **`static void traverse_cluster(std::vector<std::vector<bool>>& grid, int start_x, int start_y, int rows, int cols)`**:
   - Traverses and marks all cells belonging to the same cluster by modifying the grid during BFS.
   
- **`static void traverse_cluster(const std::vector<std::vector<bool>>& grid, std::vector<std::vector<bool>>& visited, int start_x, int start_y, int rows, int cols)`**:
   - Traverses a cluster using a separate `visited` grid to track visited cells without modifying the original grid.

### `QueueSizeExceededException`

An exception class that is thrown when the BFS queue exceeds the maximum allowed size. This ensures that the program handles large grids gracefully and prevents overflow.

#### Constructor:
- **`QueueSizeExceededException(const std::string& message)`**: Initializes the exception with a custom error message.

## Constants

- **`deltas_number`**: The number of possible directions (4: up, down, left, right) for BFS traversal.
- **`row_deltas`**: Directional offsets for horizontal movement (right, left).
- **`col_deltas`**: Directional offsets for vertical movement (down, up).
- **`MAX_CELLS`**: Maximum allowed number of cells in the grid (2^31).
- **`MAX_QUEUE_SIZE`**: Maximum allowed size of the BFS queue to prevent overflow.

## Example Usage

```cpp
#include "ClusterCounter.h"
#include <iostream>

int main() {
    std::vector<std::vector<bool>> grid = {
        {1, 0, 0, 0},
        {0, 1, 1, 0},
        {1, 1, 0, 1},
        {0, 0, 1, 1}
    };

    
    // Count clusters without modifying the grid
    int clusters = clusters::ClusterCounter::count_clusters(const_cast<const std::vector<std::vector<bool>>&>(grid));
    std::cout << "Clusters (without modification): " << clusters << std::endl;

    // Count clusters using the grid modification method
    clusters = clusters::ClusterCounter::count_clusters(grid);
    std::cout << "Clusters (direct modification): " << clusters << std::endl;

    return 0;
}
```

### Example Output:
```
Clusters (direct modification): 3
Clusters (without modification): 3
```
the example is also available in example.cpp

## Exceptions

The following exceptions can be thrown:

- **`QueueSizeExceededException`**: Thrown if the BFS queue exceeds the maximum size (`MAX_QUEUE_SIZE`).
- **`std::invalid_argument`**: Thrown if the grid is empty, has rows of inconsistent sizes, or exceeds the maximum allowed size.

## Testing
A file with tests `test.cpp` is provided in the root directory, demonstrating a variaty of examples with the cluster-counter.
#include<iostream>
#include<vector>
#include"ClusterCounter.h"
#include <cassert>

using namespace clusters;

class ClusterTest {
public:

    // Run the test for a specific grid with timing and result output
    void run_test(const std::string& test_name, std::vector<std::vector<bool>>& grid, int expected_clusters) {
        int clusters = ClusterCounter::count_clusters(grid);
        assert(clusters == expected_clusters);
        std::cout << test_name << " was successful\n";
    }

    // When grid has no rows
    void test_no_rows() {
        std::vector<std::vector<bool>> grid = {}; // Empty grid (no rows)
        try {
            ClusterCounter::count_clusters(grid);
            assert(false && "Exception should have been thrown for no rows");
        } catch (const std::invalid_argument& e) {
            assert(std::string(e.what()) == "std::vector<std::vector<bool>> cannot be empty or contain empty rows.");
        }
        std::cout << "No rows throw assertion was successful" << std::endl;
    }

    // When grid has no columns
    void test_no_columns() {
        std::vector<std::vector<bool>> grid = {{}}; // std::vector<std::vector<bool>> with no columns
        try {
            ClusterCounter::count_clusters(grid);
            assert(false && "Exception should have been thrown for no columns");
        } catch (const std::invalid_argument& e) {
            assert(std::string(e.what()) == "std::vector<std::vector<bool>> cannot be empty or contain empty rows.");
        }
        std::cout << "No columns throw assertion was successful" << std::endl;

    }

    // Very large grid (50,000 x 50,000)
    void test_large_grid_exception() {
        std::vector<std::vector<bool>> grid(50000, std::vector<bool>(50000, 0));  // Too large grid
        try {
            ClusterCounter::count_clusters(grid);
            assert(false && "Exception should have been thrown for large grid size");
        } catch (const std::invalid_argument& e) {
            assert(std::string(e.what()) == "The number of cells exceeds 2^31 (maximum allowed cells).");
        }
        std::cout << "large grid size throw assertion was successful" << std::endl;
    }

    // Irregularly shaped grid (rows of different lengths)
    void test_irregular_shape() {
        std::vector<std::vector<bool>> grid = {
            {1, 1},
            {1, 1, 1},  // Irregular row size
            {1, 1}
        };
        try {
            ClusterCounter::count_clusters(grid);
            assert(false && "Exception should have been thrown for irregular shape");
        } catch (const std::invalid_argument& e) {
            assert(std::string(e.what()) == "All rows in the grid must have the same number of cells.");
        }
        std::cout << "Irregular shape throw assertion was successful" << std::endl;
    }

    // Small grid with manually added clusters (3x3 grid)
    void test_small_grid() {
        std::vector<std::vector<bool>> small_grid = {
            {1, 1, 0},
            {1, 0, 0},
            {0, 0, 1}
        };
        run_test("Small std::vector<std::vector<bool>> (3x3)", small_grid, 2);  // Expected: 2 clusters
    }

    // Large grid (100x100) with predefined clusters
    void test_large_grid_100x100() {
        std::vector<std::vector<bool>> grid(100, std::vector<bool>(100, 0));  // Start with all 0s

        // Define a few clusters manually
        // Cluster 1
        for (int i = 10; i < 15; i++) {
            for (int j = 10; j < 15; j++) {
                grid[i][j] = 1;
            }
        }

        // Cluster 2
        for (int i = 40; i < 45; i++) {
            for (int j = 40; j < 45; j++) {
                grid[i][j] = 1;
            }
        }

        // Cluster 3
        for (int i = 70; i < 75; i++) {
            for (int j = 70; j < 75; j++) {
                grid[i][j] = 1;
            }
        }

        // Expected number of clusters: 3
        run_test("Large std::vector<std::vector<bool>> (100x100)", grid, 3);
    }

    // Very Large grid (500x500) with predefined clusters
    void test_large_grid_500x500() {
        std::vector<std::vector<bool>> grid(500, std::vector<bool>(500, 0));  // Start with all 0s

        // Cluster 1 (top-left corner)
        for (int i = 50; i < 55; i++) {
            for (int j = 50; j < 55; j++) {
                grid[i][j] = 1;
            }
        }

        // Cluster 2 (center of the grid)
        for (int i = 200; i < 205; i++) {
            for (int j = 200; j < 205; j++) {
                grid[i][j] = 1;
            }
        }

        // Cluster 3 (bottom-right corner)
        for (int i = 450; i < 455; i++) {
            for (int j = 450; j < 455; j++) {
                grid[i][j] = 1;
            }
        }

        // Expected number of clusters: 3
        run_test("Very Large std::vector<std::vector<bool>> (500x500)", grid, 3);
    }

    // Extremely Large grid (1000x1000) with predefined clusters
    void test_large_grid_1000x1000() {
        std::vector<std::vector<bool>> grid(1000, std::vector<bool>(1000, 0));  // Start with all 0s

        // Cluster 1 (top-left corner)
        for (int i = 100; i < 105; i++) {
            for (int j = 100; j < 105; j++) {
                grid[i][j] = 1;
            }
        }

        // Cluster 2 (center of the grid)
        for (int i = 450; i < 455; i++) {
            for (int j = 450; j < 455; j++) {
                grid[i][j] = 1;
            }
        }

        // Cluster 3 (bottom-right corner)
        for (int i = 900; i < 905; i++) {
            for (int j = 900; j < 905; j++) {
                grid[i][j] = 1;
            }
        }

        // Expected number of clusters: 3
        run_test("Extremely Large std::vector<std::vector<bool>> (1000x1000)", grid, 3);
    }

    // Sparse std::vector<std::vector<bool>> (50x50 with few clusters)
    void test_sparse_grid() {
        std::vector<std::vector<bool>> sparse_grid(50, std::vector<bool>(50, 0));  // Initially all 0s
        sparse_grid[10][10] = 1;
        sparse_grid[20][20] = 1;
        sparse_grid[30][30] = 1;
        run_test("Sparse std::vector<std::vector<bool>> (50x50)", sparse_grid, 3);  // Expected: 3 clusters
    }

    // std::vector<std::vector<bool>> with no clusters (50x50 with all zeros)
    void test_no_clusters_grid() {
        std::vector<std::vector<bool>> no_clusters_grid(50, std::vector<bool>(50, 0));
        run_test("No Clusters std::vector<std::vector<bool>> (50x50)", no_clusters_grid, 0);  // Expected: 0 clusters
    }

    // Test: std::vector<std::vector<bool>> with all 1s (50x50)
    void test_all_ones_50x50() {
        std::vector<std::vector<bool>> all_ones_grid(50, std::vector<bool>(50, 1));  // Create a 50x50 grid filled with 1s
        run_test("All Ones std::vector<std::vector<bool>> (50x50)", all_ones_grid, 1);  // Expected: 1 cluster
    }

    // Test: std::vector<std::vector<bool>> with all 1s (1000x1000)
    void test_all_ones_1000x1000() {
        std::vector<std::vector<bool>> all_ones_grid(1000, std::vector<bool>(1000, 1));  // Create a 1000x1000 grid filled with 1s
        run_test("All Ones std::vector<std::vector<bool>> (1000x1000)", all_ones_grid, 1);  // Expected: 1 cluster
    }

    // 1x1 grid with 0
    void test_one_by_one_0() {
        std::vector<std::vector<bool>> one_by_one_0 = {
            {0}
        };
        run_test("1x1 std::vector<std::vector<bool>> (0)", one_by_one_0, 0);  // Expected: 0 clusters
    }

    // 1x1 grid with 1
    void test_one_by_one_1() {
        std::vector<std::vector<bool>> one_by_one_1 = {
            {1}
        };
        run_test("1x1 std::vector<std::vector<bool>> (1)", one_by_one_1, 1);  // Expected: 1 cluster
    }

    // Two diagonal clusters (50x50)
    void test_two_diagonal_clusters() {
        std::vector<std::vector<bool>> grid(50, std::vector<bool>(50, 0));  // Start with all 0s
        // Cluster 1: diagonal from top-left to bottom-right
        for (int i = 0; i < 50; i++) {
            grid[i][i] = 1;
        }
        // Cluster 2: diagonal from top-right to bottom-left
        for (int i = 0; i < 50; i++) {
            grid[i][49 - i] = 1;
        }
        run_test("Two Diagonal Clusters (50x50)", grid, 97);  // Expected: 97 clusters
    }

    // Two separate clusters (50x50)
    void test_two_separate_clusters() {
        std::vector<std::vector<bool>> grid(50, std::vector<bool>(50, 0));  // Start with all 0s
        // Cluster 1
        for (int i = 10; i < 15; i++) {
            for (int j = 10; j < 15; j++) {
                grid[i][j] = 1;
            }
        }
        // Cluster 2
        for (int i = 35; i < 40; i++) {
            for (int j = 35; j < 40; j++) {
                grid[i][j] = 1;
            }
        }
        run_test("Two Separate Clusters (50x50)", grid, 2);  // Expected: 2 clusters
    }

    // Checkerboard pattern (50x50)
    void test_checkerboard_pattern() {
        std::vector<std::vector<bool>> grid(50, std::vector<bool>(50, 0));  // Start with all 0s
        // Create a checkerboard pattern
        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 50; j++) {
                if ((i + j) % 2 == 0) {
                    grid[i][j] = 1;
                }
            }
        }
        run_test("Checkerboard Pattern (50x50)", grid, 1250);  // Expected: 1250 clusters
    }

    // Large isolated cluster (50x50)
    void test_large_isolated_cluster() {
        std::vector<std::vector<bool>> grid(50, std::vector<bool>(50, 0));  // Start with all 0s
        // Create a large isolated cluster in the middle of the grid
        for (int i = 20; i < 30; i++) {
            for (int j = 20; j < 30; j++) {
                grid[i][j] = 1;
            }
        }
        run_test("Large Isolated Cluster (50x50)", grid, 1);  // Expected: 1 cluster
    }

    // std::vector<std::vector<bool>> with one row (1x50)
    void test_one_row_grid() {
        std::vector<std::vector<bool>> grid(1, std::vector<bool>(50, 0));
        // Add clusters in the row
        grid[0][10] = 1;
        grid[0][20] = 1;
        grid[0][30] = 1;
        run_test("One Row std::vector<std::vector<bool>> (1x50)", grid, 3);  // Expected: 3 clusters
    }

    // std::vector<std::vector<bool>> with one column (50x1)
    void test_one_column_grid() {
        std::vector<std::vector<bool>> grid(50, std::vector<bool>(1, 0));
        // Add clusters in the column
        grid[10][0] = 1;
        grid[20][0] = 1;
        grid[30][0] = 1;
        run_test("One Column std::vector<std::vector<bool>> (50x1)", grid, 3);  // Expected: 3 clusters
    }

    // Large gap between clusters (50x50)
    void test_large_gap_between_clusters() {
        std::vector<std::vector<bool>> grid(50, std::vector<bool>(50, 0));  // Start with all 0s
        // Cluster 1
        for (int i = 5; i < 10; i++) {
            for (int j = 5; j < 10; j++) {
                grid[i][j] = 1;
            }
        }
        // Cluster 2
        for (int i = 35; i < 40; i++) {
            for (int j = 35; j < 40; j++) {
                grid[i][j] = 1;
            }
        }
        // Cluster 3
        for (int i = 45; i < 50; i++) {
            for (int j = 45; j < 50; j++) {
                grid[i][j] = 1;
            }
        }
        run_test("Large Gap Between Clusters (50x50)", grid, 3);  // Expected: 3 clusters
    }

    // Single column cluster (50x50)
    void test_single_column_cluster() {
        std::vector<std::vector<bool>> grid(50, std::vector<bool>(50, 0));  // Start with all 0s
        // A single column of ones
        for (int i = 0; i < 50; i++) {
            grid[i][0] = 1;
        }
        run_test("Single Column Cluster (50x50)", grid, 1);  // Expected: 1 cluster
    }

    // Spiral cluster pattern (50x50)
    void test_spiral_cluster() {
        std::vector<std::vector<bool>> grid(50, std::vector<bool>(50, 0));  // Start with all 0s
        int x = 0, y = 0;
        for (int i = 0; i < 25; i++) {
            for (int j = i; j < 50 - i; j++) {
                grid[i][j] = 1;
                grid[j][i] = 1;
                grid[49 - i][j] = 1;
                grid[j][49 - i] = 1;
            }
        }
        run_test("Spiral Cluster (50x50)", grid, 1);  // Expected: 1 cluster
    }

    // 20M grod
    void test_large_grid_20_million_random_clusters() {
        // Define the grid size (4000x5000 = 20 million)
        const int rows = 4000;
        const int cols = 5000;

        // Initialize the grid with all zeros
        std::vector<std::vector<bool>> grid(rows, std::vector<bool>(cols, 0));

        // Cluster 1 (Top-left corner)
        for (int i = 100; i < 120; i++) {
            for (int j = 100; j < 120; j++) {
                grid[i][j] = 1;
            }
        }

        // Cluster 2 (Middle of the grid)
        for (int i = 1800; i < 1820; i++) {
            for (int j = 2200; j < 2220; j++) {
                grid[i][j] = 1;
            }
        }

        // Cluster 3 (Bottom-right corner)
        for (int i = 3800; i < 3820; i++) {
            for (int j = 4800; j < 4820; j++) {
                grid[i][j] = 1;
            }
        }

        // Add a few more clusters in random locations:
        grid[1500][2500] = 1;
        grid[1500][2501] = 1;
        grid[1501][2500] = 1;
        
        grid[3000][1000] = 1;
        grid[3001][1000] = 1;
        
        grid[3200][4000] = 1;

        // Total clusters expected: 6
        run_test("20 Million std::vector<std::vector<bool>> (4000x5000) with random clusters", grid, 6);  // Expected: 6 clusters
    }

    // 100M grid
    void test_large_grid_100_million_random_clusters() {
        // Define the grid size (10,000 x 10,000 = 100 million)
        const int rows = 10000;
        const int cols = 10000;

        // Initialize the grid with all zeros
        std::vector<std::vector<bool>> grid(rows, std::vector<bool>(cols, 0));

        // Cluster 1 (Top-left corner)
        for (int i = 500; i < 520; i++) {
            for (int j = 500; j < 520; j++) {
                grid[i][j] = 1;
            }
        }

        // Cluster 2 (Middle of the grid)
        for (int i = 4800; i < 4820; i++) {
            for (int j = 4800; j < 4820; j++) {
                grid[i][j] = 1;
            }
        }

        // Cluster 3 (Bottom-right corner)
        for (int i = 9800; i < 9820; i++) {
            for (int j = 9800; j < 9820; j++) {
                grid[i][j] = 1;
            }
        }

        // Additional clusters at random positions:
        grid[2000][3000] = 1;
        grid[2000][3001] = 1;
        grid[2001][3000] = 1;

        grid[7000][1000] = 1;
        grid[7001][1000] = 1;

        grid[8200][8500] = 1;

        // Total clusters expected: 6
        run_test("100 Million std::vector<std::vector<bool>> (10000x10000) with random clusters", grid, 6);  // Expected: 6 clusters
    }

    // 2B grid
    void test_large_grid_50k_by_40k_random_clusters() {
        // Define the grid size (50,000 x 40,000 = 2 billion cells)
        const int rows = 50000;
        const int cols = 40000;

        // Initialize the grid with all zeros
        std::vector<std::vector<bool>> grid(rows, std::vector<bool>(cols, 0));

        // Cluster 1 (Top-left corner)
        for (int i = 5000; i < 5020; i++) {
            for (int j = 5000; j < 5020; j++) {
                grid[i][j] = 1;
            }
        }

        // Cluster 2 (Near the center of the grid)
        for (int i = 25000; i < 25020; i++) {
            for (int j = 20000; j < 20020; j++) {
                grid[i][j] = 1;
            }
        }

        // Cluster 3 (Near the bottom-right corner)
        for (int i = 49000; i < 49020; i++) {
            for (int j = 39000; j < 39020; j++) {
                grid[i][j] = 1;
            }
        }

        // Additional clusters at random positions
        grid[10000][12000] = 1;
        grid[10001][12000] = 1;
        grid[10000][12001] = 1;

        grid[30000][10000] = 1;
        grid[30001][10000] = 1;

        grid[40000][35000] = 1;

        // Total clusters expected: 6
        run_test("50k x 40k std::vector<std::vector<bool>> with random clusters", grid, 6);  // Expected: 6 clusters
    }





    // Run all tests
    void run_all_tests() {

        test_no_rows();
        test_no_columns();
        test_large_grid_exception();
        test_irregular_shape();

        test_small_grid();
        test_large_grid_100x100();
        test_large_grid_500x500();
        test_large_grid_1000x1000();
        test_sparse_grid();
        test_no_clusters_grid();
        test_one_by_one_0();
        test_one_by_one_1();
        test_two_diagonal_clusters();
        test_two_separate_clusters();
        test_checkerboard_pattern();
        test_large_isolated_cluster();
        test_one_row_grid();
        test_one_column_grid();
        test_large_gap_between_clusters();
        test_single_column_cluster();
        test_spiral_cluster();
        test_all_ones_50x50();
        test_all_ones_1000x1000();
        test_large_grid_20_million_random_clusters();
        test_large_grid_100_million_random_clusters();
        test_large_grid_50k_by_40k_random_clusters();
    
    }
};

int main() {
    ClusterTest tester;
    tester.run_all_tests();

    return 0;
}

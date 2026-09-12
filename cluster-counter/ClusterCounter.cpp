#include "ClusterCounter.h"
#include <iostream>
#include <queue>
#include <stdexcept>


namespace clusters{
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
    int ClusterCounter::count_clusters(std::vector<std::vector<bool>>& grid){
        validate_input(grid);

        const int rows = grid.size();
        const int cols = grid[0].size();
        int result = 0;
        for (int row = 0; row < rows; row++){
            for (int col = 0; col < cols; col++){
                if(grid[row][col]){
                    ClusterCounter::traverse_cluster(grid, row, col, rows, cols);
                    result++;
                }
            }
        }
        return result;
    }
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
    int ClusterCounter::count_clusters(const std::vector<std::vector<bool>>& grid){
        validate_input(grid);

        const int rows = grid.size();
        const int cols = grid[0].size();
        std::vector<std::vector<bool>> visited(rows, std::vector<bool>(cols, false));
        int result = 0;
        for (int row = 0; row < rows; row++){
            for (int col = 0; col < cols; col++){
                if(grid[row][col] && !visited[row][col]){
                    ClusterCounter::traverse_cluster(grid, visited, row, col, rows, cols);
                    result++;
                }
            }
        }
        return result;
    }

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
    void ClusterCounter::validate_input(const std::vector<std::vector<bool>>& grid) {
        const auto rows = grid.size();
        
        if (rows == 0 || grid[0].size() == 0) {
            throw std::invalid_argument("std::vector<std::vector<bool>> cannot be empty or contain empty rows.");
        }
        
        const auto cols = grid[0].size();
        
        if (rows > MAX_CELLS / cols) {
            throw std::invalid_argument("The number of cells exceeds 2^31 (maximum allowed cells).");
        }
        
        for (int row = 0; row < rows; row++) {
            if (grid[row].size() != cols) {
                throw std::invalid_argument("All rows in the grid must have the same number of cells.");
            }
        }
    }

    /**
    * @brief Traverses a cluster and marks all its connected cells as visited in the grid.
    * 
    * This method performs a breadth-first search (BFS) starting from the given cell. All cells 
    * connected to the starting cell (forming a cluster) are marked as visited by setting them to 
    * 'false' in the grid. If the BFS queue exceeds the maximum size, an exception is thrown.
    * 
    * @param grid The grid of boolean values representing the cells to be traversed.
    * @param start_row The row index of the starting cell.
    * @param start_col The column index of the starting cell.
    * @param rows The number of rows in the grid.
    * @param cols The number of columns in the grid.
    */
    void ClusterCounter::traverse_cluster(std::vector<std::vector<bool>>& grid, 
                                        int start_row, int start_col, int rows, int cols){
        grid[start_row][start_col] = 0;

        std::queue<std::pair<int, int>> waiting;
        waiting.push({start_row, start_col});
        while(!waiting.empty()){
            const auto [row, col] = waiting.front();
            for (int current_delta = 0; current_delta < deltas_number; current_delta++) {
                const int new_row = row + row_deltas[current_delta];
                const int new_col = col + col_deltas[current_delta];
                if(0 <= new_row && new_row < rows && 0 <= new_col && new_col < cols && grid[new_row][new_col]){
                    waiting.push({new_row, new_col});
                    grid[new_row][new_col] = 0;
                }
            }
            waiting.pop();
            if (waiting.size() > MAX_QUEUE_SIZE) {
                throw QueueSizeExceededException("Queue size exceeded max limit (" 
                                            + std::to_string(MAX_QUEUE_SIZE) + "), aborting BFS.");
            }
        }
    }
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
    * @param start_row The row index of the starting cell.
    * @param start_col The column index of the starting cell.
    * @param rows The number of rows in the grid.
    * @param cols The number of columns in the grid.
    */
    void ClusterCounter::traverse_cluster(const std::vector<std::vector<bool>>& grid, 
                                            std::vector<std::vector<bool>>& visited, 
                                            int start_row, int start_col, int rows, int cols){
        visited[start_row][start_col] = 1;

        std::queue<std::pair<int, int>> waiting;
        waiting.push({start_row, start_col});
        while(!waiting.empty()){
            const auto [row, col] = waiting.front();
            for (int current_delta = 0; current_delta < deltas_number; current_delta++) {
                const int new_row = row + row_deltas[current_delta];
                const int new_col = col + col_deltas[current_delta];
                if(0 <= new_row && new_row < rows && 0 <= new_col && new_col < cols &&
                    grid[new_row][new_col] && !visited[new_row][new_col]){
                        waiting.push({new_row, new_col});
                        visited[new_row][new_col] = 1;
                }
            }
            waiting.pop();
            if (waiting.size() > MAX_QUEUE_SIZE) {
                throw QueueSizeExceededException("Queue size exceeded max limit (" 
                                + std::to_string(MAX_QUEUE_SIZE) + "), aborting BFS.");
            }
        }
    }
}
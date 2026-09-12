#include<iostream>
#include<vector>
#include"ClusterCounter.h"

using namespace clusters;

int main() {

        std::vector<std::vector<bool>> grid = {
            {1, 0, 0, 0},
            {0, 1, 1, 0},
            {1, 1, 0, 1},
            {0, 0, 1, 1}
        };

        std::vector<std::vector<bool>> grid2 = {
            {1, 0, 0, 0, 0},
            {1, 0, 1, 1, 0},
            {0, 1, 0, 1, 0},
            {0, 1, 1, 1, 0},
            {0, 0, 0, 0, 1},
        };

        
        // Count clusters without modifying the grid
        int clusters = clusters::ClusterCounter::count_clusters(const_cast<const std::vector<std::vector<bool>>&>(grid));
        std::cout << "Clusters (without modification): " << clusters << std::endl;

        // Count clusters using the grid modification method
        clusters = clusters::ClusterCounter::count_clusters(grid);
        std::cout << "Clusters (direct modification): " << clusters << std::endl;


        // Count clusters without modifying the grid
        clusters = clusters::ClusterCounter::count_clusters(const_cast<const std::vector<std::vector<bool>>&>(grid2));
        std::cout << "Clusters (without modification): " << clusters << std::endl;

        // Count clusters using the grid modification method
        clusters = clusters::ClusterCounter::count_clusters(grid2);
        std::cout << "Clusters (direct modification): " << clusters << std::endl;


    return 0;
}

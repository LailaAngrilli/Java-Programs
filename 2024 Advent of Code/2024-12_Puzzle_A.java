import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GardenFencing {

    static class Point {
        final int x;
        final int y;
        Point(int x, int y) { this.x = x; this.y = y; }
    }

    public static int calculate_total_fencing_cost(List<List<Integer>> gardenMap) {
        int rows = gardenMap.size();
        int columns = gardenMap.get(0).size();
        boolean[][] processed = new boolean[rows][columns];
        int totalCost = 0;

        // Check if within bounds
        java.util.function.BiFunction<Integer,Integer,Boolean> is_valid = (x, y) ->
            (0 <= x && x < rows && 0 <= y && y < columns && !processed[x][y]);

        // Caluclate area and perimeter using DFS
        class DFSResult { int area; int perimeter; DFSResult(int a, int p){ area = a; perimeter = p; } }

        java.util.function.Function<Point, DFSResult> dfs = (start) -> {
            List<Point> plot = new ArrayList<>();
            int area = 0;
            int perimeter = 0;
            processed[start.x][start.y] = true;
            plot.add(start);
            while (!plot.isEmpty()) {
                Point cur = plot.remove(plot.size() - 1);
                int cx = cur.x;
                int cy = cur.y;
                area += 1;
                // Check directions (up, down, left, right)
                int[][] directions = { {-1,0}, {1,0}, {0,-1}, {0,1} };
                for (int[] d : directions) {
                    int directionX = d[0];
                    int directionY = d[1];
                    int numberOfX = cx + directionX;
                    int numberOfY = cy + directionY;
                    if (is_valid.apply(numberOfX, numberOfY)) {
                        if (gardenMap.get(numberOfX).get(numberOfY).equals(gardenMap.get(start.x).get(start.y))) {
                            processed[numberOfX][numberOfY] = true;
                            plot.add(new Point(numberOfX, numberOfY));
                        } else {
                            perimeter += 1;
                        }
                    }
                    // Out of bounds contribute to perimeter
                    else if (!(0 <= numberOfX && numberOfX < rows && 0 <= numberOfY && numberOfY < columns)) {
                        perimeter += 1;
                    }
                    // Neighbouring plant contribute to perimeter
                    else if (!gardenMap.get(numberOfX).get(numberOfY).equals(gardenMap.get(start.x).get(start.y))) {
                        perimeter += 1;
                    }
                }
            }
            return new DFSResult(area, perimeter);
        };

        // Calculations
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (!processed[i][j]) {
                    int plantType = gardenMap.get(i).get(j);
                    DFSResult res = dfs.apply(new Point(i, j));
                    int area = res.area;
                    int perimeter = res.perimeter;
                    int cost = area * perimeter;
                    totalCost += cost;
                    System.out.println(String.format("Region of plant %d: Area = %d, Perimeter = %d, Cost = %d",
                        plantType, area, perimeter, cost));
                }
            }
        }
        return totalCost;
    }

    // Input and Output
    public static void main(String[] args) {
        System.out.println("Enter the garden map, with each row on a new line.");
        System.out.println("Press Enter on an empty line to submit.");
        List<List<Integer>> gardenMap = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        int row = 1;
        while (true) {
            String line = scanner.nextLine().strip();
            if (line.isEmpty()) break;
            List<Integer> rowList = new ArrayList<>();
            for (char c : line.toCharArray()) {
                rowList.add(Character.getNumericValue(c));
            }
            gardenMap.add(rowList);
            row++;
        }

        // Output
        if (!gardenMap.isEmpty()) {
            int totalCost = calculate_total_fencing_cost(gardenMap);
            System.out.println(String.format("The total price for fencing is: %d", totalCost));
        } else {
            System.out.println("No garden map provided.");
        }
    }
}

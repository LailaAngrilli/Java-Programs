import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Main {
    public static List<char[]> simulate_memory(int gridSize, List<int[]> bytePositions, int stepsToSimulate) {
        List<char[]> grid = new ArrayList<>();
        for (int row = 0; row < gridSize; row++) {
            char[] line = new char[gridSize];
            for (int character = 0; character < gridSize; character++) {
                line[character] = '.';
            }
            grid.add(line);
        }
        for (int i = 0; i < bytePositions.size(); i++) {
            if (i >= stepsToSimulate) {
                break;
            }
            int[] pos = bytePositions.get(i);
            int x = pos[0];
            int y = pos[1];
            grid.get(y)[x] = '#';
        }
        return grid;
    }

    public static int find_shortest_path(List<char[]> grid) {
        int gridSize = grid.size();
        int[] start = new int[] {0, 0};
        int[] end = new int[] {gridSize - 1, gridSize - 1};
        int[][] directions = new int[][] { {-1, 0}, {1, 0}, {0, -1}, {0, 1} };
        Deque<Node> queue = new ArrayDeque<>();
        queue.addLast(new Node(start[0], start[1], 0));
        Set<String> visited = new HashSet<>();
        visited.add(coordKey(start[0], start[1]));
        while (!queue.isEmpty()) {
            Node node = queue.removeFirst();
            int x = node.x;
            int y = node.y;
            int steps = node.steps;
            if (x == end[0] && y == end[1]) {
                return steps;
            }
            for (int[] d : directions) {
                int newX = x + d[0];
                int newY = y + d[1];
                if (0 <= newX && newX < gridSize && 0 <= newY && newY < gridSize) {
                    if (grid.get(newY)[newX] == '.' && !visited.contains(coordKey(newX, newY))) {
                        visited.add(coordKey(newX, newY));
                        queue.addLast(new Node(newX, newY, steps + 1));
                    }
                }
            }
        }
        return -1;
    }

    private static String coordKey(int x, int y) {
        return x + "," + y;
    }

    private static class Node {
        int x;
        int y;
        int steps;
        Node(int x, int y, int steps) {
            this.x = x;
            this.y = y;
            this.steps = steps;
        }
    }

    // Input and Output
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter byte positions as a list of tuples (e.g., [(1, 1), (2, 2)]):");
        String bytePositionsInput = scanner.nextLine().trim();


        List<int[]> bytePositions = parseBytePositions(bytePositionsInput);

        System.out.print("Enter the number of steps to simulate: ");
        int steps = Integer.parseInt(scanner.nextLine().trim());

        // Calculation for gridSize
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        for (int[] p : bytePositions) {
            int x = p[0];
            int y = p[1];
            if (x > maxX) maxX = x;
            if (y > maxY) maxY = y;
        }
        int gridSize = Math.max(maxX, maxY) + 1;

        // Set grid variable
        List<char[]> grid = simulate_memory(gridSize, bytePositions, steps);

        // Find the shortest path
        int shortestPathSteps = find_shortest_path(grid);

        // Output the results
        System.out.println();
        if (shortestPathSteps == -1) {
            System.out.println("There is no possible path");
        } else if (shortestPathSteps == 1) {
            System.out.println("The shortest path has " + shortestPathSteps + " step");
        } else {
            System.out.println("The shortest path has " + shortestPathSteps + " steps");
        }

        scanner.close();
    }

    private static List<int[]> parseBytePositions(String s) {
        List<int[]> result = new ArrayList<>();
        String trimmed = s.trim();
        if (trimmed.startsWith("[") && trimmed.endsWith("]")) {
            trimmed = trimmed.substring(1, trimmed.length() - 1).trim();
            if (trimmed.isEmpty()) return result;
            // Split on '),'
            String[] parts = trimmed.split("\\),");
            for (String part : parts) {
                part = part.replace("(", "").replace(")", "").replace(" ", "");
                if (part.isEmpty()) continue;
                String[] nums = part.split(",");
                int x = Integer.parseInt(nums[0].trim());
                int y = Integer.parseInt(nums[1].trim());
                result.add(new int[] {x, y});
            }
        }
        return result;
    }
}

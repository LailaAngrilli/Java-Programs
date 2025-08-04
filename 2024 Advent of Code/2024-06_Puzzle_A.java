import java.util.*;

public class GuardSimulator {
    private static Object[] parse_map(String mapInput) {
        char[][] grid = Arrays.stream(mapInput.split("\\R"))
                .map(String::toCharArray)
                .toArray(char[][]::new);

        Map<Character,int[]> directions = new LinkedHashMap<>();
        directions.put('^', new int[]{-1, 0});
        directions.put('>', new int[]{0, 1});
        directions.put('v', new int[]{1, 0});
        directions.put('<', new int[]{0, -1});

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                char cell = grid[i][j];
                if (directions.containsKey(cell)) {
                    return new Object[]{ grid, new int[]{i, j}, directions.get(cell) };
                }
            }
        }

        throw new IllegalArgumentException(
                "Map must contain a guard (indicated with either ^, v, > or <)"
        );
    }

    // Simulate the guard's movement and mark them
    private static Set<List<Integer>> move_guard(char[][] grid, int[] start, int[] direction) {
        // Set up stuff
        int rows = grid.length, columns = grid[0].length;
        List<String> directions = Arrays.asList("^", ">", "v", "<");
        List<int[]> directionCoordinates = Arrays.asList(
                new int[]{ -1, 0 },
                new int[]{ 0,  1 },
                new int[]{ 1,  0 },
                new int[]{ 0, -1 }
        );
        int x = start[0], y = start[1];
        int directionIndex = 0;
        for (int i = 0; i < directionCoordinates.size(); i++) {
            int[] dc = directionCoordinates.get(i);
            if (dc[0] == direction[0] && dc[1] == direction[1]) {
                directionIndex = i;
                break;
            }
        }
        Set<List<Integer>> visited = new HashSet<>();

        // Mark visited potions with X and determine next position
        while (0 <= x && x < rows && 0 <= y && y < columns) {
            visited.add(Arrays.asList(x, y));
            if (grid[x][y] != '#') {
                grid[x][y] = 'X';
            }

            // Determine next position
            int[] dirXY = directionCoordinates.get(directionIndex);
            int nextPositionX = x + dirXY[0], nextPositionY = y + dirXY[1];

            // Movement (Turn right if obstructed, else go forward)
            if (0 <= nextPositionX && nextPositionX < rows
                    && 0 <= nextPositionY && nextPositionY < columns
                    && grid[nextPositionX][nextPositionY] == '#') {
                directionIndex = (directionIndex + 1) % 4;
            } else {
                x = nextPositionX;
                y = nextPositionY;
            }
        }

        return visited;
    }


    // Inputs and Outputs
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println(
                "Enter the map (for free spaces use '.', for obstructions use '#' " +
                        "and for the guard use '^', 'v', '<' or '>')."
        );
        System.out.println("Press Enter on empty line to submit: ");
        StringBuilder mapInput = new StringBuilder();
        while (true) {
            String line = scanner.nextLine();
            if (line.trim().isEmpty()) {
                break;
            }
            mapInput.append(line).append("\n");
        }

        Object[] parsed = parse_map(mapInput.toString().trim());
        char[][] grid = (char[][]) parsed[0];
        int[] start = (int[]) parsed[1];
        int[] direction = (int[]) parsed[2];

        Set<List<Integer>> visitedPositions = move_guard(grid, start, direction);

        System.out.println("Positions Guard Has Visited:");
        for (char[] row : grid) {
            System.out.println(new String(row));
        }
        System.out.printf(
                "The guard has visited %d distinct positions.%n",
                visitedPositions.size()
        );
    }
}
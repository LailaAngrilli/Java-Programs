import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.PriorityQueue;
import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;

public class MazeSolver {
    private static class Node implements Comparable<Node> {
        int score;
        int x;
        int y;
        char direction; // 'N','E','S','W'

        Node(int score, int x, int y, char direction) {
            this.score = score;
            this.x = x;
            this.y = y;
            this.direction = direction;
        }

        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.score, other.score);
        }
    }

    // parse maze
    private static class ParseResult {
        String[] grid;
        int[] start; 
        int[] end;   
        ParseResult(String[] grid, int[] start, int[] end) {
            this.grid = grid;
            this.start = start;
            this.end = end;
        }
    }

    private static ParseResult parse_maze(String maze) {
        String[] lines = maze.split("\\R", -1);
        int[] start = null, end = null;
        for (int y = 0; y < lines.length; y++) {
            String row = lines[y];
            for (int x = 0; x < row.length(); x++) {
                char tile = row.charAt(x);
                if (tile == 'S') start = new int[] { x, y };
                else if (tile == 'E') end = new int[] { x, y };
            }
        }
        return new ParseResult(lines, start, end);
    }

    // directions
    private static int[] direction_delta(char direction) {
        switch (direction) {
            case 'N': return new int[] { 0, -1 };
            case 'E': return new int[] { 1, 0 };
            case 'S': return new int[] { 0, 1 };
            case 'W': return new int[] { -1, 0 };
            default: throw new IllegalArgumentException("Invalid direction: " + direction);
        }
    }

    // rotate_direction
    private static char rotate_direction(char current, boolean clockwise) {
        char[] directions = { 'N', 'E', 'S', 'W' };
        int idx = Arrays.binarySearch(directions, current);
        if (idx < 0) {
            // fallback to linear search to preserve behavior if binarySearch fails due to order
            for (int i = 0; i < directions.length; i++) if (directions[i] == current) { idx = i; break; }
        }
        if (clockwise) return directions[(idx + 1) % 4];
        else return directions[(idx + 3) % 4]; // (idx - 1) mod 4
    }

    // find the lowest score
    private static int find_lowest_score(String maze) {
        ParseResult parsed = parse_maze(maze);
        String[] grid = parsed.grid;
        int[] start = parsed.start;
        int[] end = parsed.end;
        int startX = start[0], startY = start[1];
        int endX = end[0], endY = end[1];

        Set<String> visited = new HashSet<>();
        PriorityQueue<Node> pq = new PriorityQueue<>();
        pq.add(new Node(0, startX, startY, 'E')); // Start facing east

        while (!pq.isEmpty()) {
            Node node = pq.poll();
            int score = node.score;
            int x = node.x;
            int y = node.y;
            char direction = node.direction;

            if (x == endX && y == endY) {
                return score;
            }
            String stateKey = x + "," + y + "," + direction;
            if (visited.contains(stateKey)) continue;
            visited.add(stateKey);

            int[] d = direction_delta(direction);
            int directionX = d[0], directionY = d[1];
            int newX = x + directionX;
            int newY = y + directionY;

            if (newY >= 0 && newY < grid.length && newX >= 0 && newX < grid[newY].length()) {
                if (grid[newY].charAt(newX) != '#') {
                    pq.add(new Node(score + 1, newX, newY, direction));
                }
            }
            // rotate clockwise
            char newDirection = rotate_direction(direction, true);
            pq.add(new Node(score + 1000, x, y, newDirection));
            // rotate counterclockwise
            newDirection = rotate_direction(direction, false);
            pq.add(new Node(score + 1000, x, y, newDirection));
        }

        return -1; // if no path found
    }

    // get maze
    private static String get_maze() throws IOException {
        System.out.println("Enter the maze, line by line, and submit by pressing Enter on an empty line:\n ");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        String line;
        boolean first = true;
        while (true) {
            line = reader.readLine();
            if (line == null) break;
            if (!line.trim().isEmpty()) {
                if (!first) sb.append("\n");
                sb.append(line);
                first = false;
            } else {
                break;
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) throws Exception {
        String userMazeInput = get_maze();
        int answer = find_lowest_score(userMazeInput);
        System.out.println("\nThe lowest score is " + answer);
    }
}

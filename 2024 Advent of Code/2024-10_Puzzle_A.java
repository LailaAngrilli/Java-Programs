import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Trailheads {
    static class Point {
        final int x;
        final int y;
        Point(int x, int y) { this.x = x; this.y = y; }
        @Override

        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Point)) return false;
            Point p = (Point) o;
            return x == p.x && y == p.y;
        }

        @Override
        public int hashCode() {
            return 31 * x + y;
        }
    }

    // Functions
    public static List<List<Integer>> get_map() {
        System.out.println("Enter the topographical map row by row, using 0-9 to indicate the height.");
        System.out.println("Press Enter on empty line to submit: ");
        List<List<Integer>> mapInput = new ArrayList<>();
       
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String row = scanner.nextLine().strip();
            if (row.isEmpty()) {  // ← Ends input when empty line
                break;
            }
            
            List<Integer> rowList = new ArrayList<>();
            for (char character : row.toCharArray()) {
                rowList.add(Character.getNumericValue(character));
            }
            mapInput.add(rowList);
        }
        
        return mapInput;
    }

    public static List<Point> find_trailheads(List<List<Integer>> mapInput) {
        List<Point> trailheads = new ArrayList<>();
        
        for (int i = 0; i < mapInput.size(); i++) {
            List<Integer> row = mapInput.get(i);
            for (int j = 0; j < row.size(); j++) {
                int heightValue = row.get(j);
                if (heightValue == 0) {  // ← Identify trailheads (i.e., 0-heights)
                    trailheads.add(new Point(i, j));
                }
            }
        }
        
        return trailheads;
    }

    public static int calculate_trailhead_score(List<List<Integer>> mapInput, Point trailhead) {
        int rows = mapInput.size();
        int columns = mapInput.get(0).size();
        int[][] directions = {
            {-1, 0}, // Up
            {1, 0},  // Down
            {0, -1}, // Left
            {0, 1}   // Right
        };
        Deque<Point> queue = new ArrayDeque<>();
        queue.add(trailhead);
        Set<Point> visited = new HashSet<>();
        int score = 0;
        visited.add(trailhead);
        
        while (!queue.isEmpty()) {
            Point p = queue.removeFirst();
            int x = p.x;
            int y = p.y;
            int currentHeight = mapInput.get(x).get(y);
            
            for (int[] dir : directions) {
                int directionX = dir[0];
                int directionY = dir[1];
                int numberX = x + directionX;
                int numberY = y + directionY;
                // Check bounds and height increment
                if (0 <= numberX && numberX < rows && 0 <= numberY && numberY < columns
                    && !visited.contains(new Point(numberX, numberY))) {
                    if (mapInput.get(numberX).get(numberY) == currentHeight + 1) {
                        queue.add(new Point(numberX, numberY));
                        visited.add(new Point(numberX, numberY));
                        // ← Identify trailheads (i.e., 0-heights
                        if (mapInput.get(numberX).get(numberY) == 9) {
                            score += 1;
                        }
                    }
                }
            }
        }
        return score;
    }

    // Inputs and Outputs
    public static void main(String[] args) {
        // Inputs
        List<List<Integer>> mapInput = get_map();
        List<Point> trailheads = find_trailheads(mapInput);
        int totalScore = 0;
        List<Integer> scores = new ArrayList<>();
        for (Point trailhead : trailheads) {
            int score = calculate_trailhead_score(mapInput, trailhead);
            scores.add(score);
            totalScore += score;
        }

        // Outputs
        System.out.println();
        System.out.println("There are " + trailheads.size() + " trailheads.");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < scores.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(scores.get(i));
        }
        System.out.println("The trailhead scores are: " + sb.toString() + ".");
        System.out.println("The total sum of the scores of all trailheads is " + totalScore + ".");
    }
}

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class RobotSafety {

    // Variables
    static int gridWidth = 101;
    static int gridHeight = 103;
    static int seconds = 100;

    // Functions
    public static List<int[][]> get_robot_data(String userInput) {
        List<int[][]> robots = new ArrayList<>();
        for (String line : userInput.strip().split("\n")) {
            String[] parts = line.split(" v=");
            String position = parts[0];
            String velocity = parts[1];
            String[] posParts = position.substring(2).split(",");
            int positionX = Integer.parseInt(posParts[0].trim());
            int positionY = Integer.parseInt(posParts[1].trim());
            String[] velParts = velocity.split(",");
            int velocityX = Integer.parseInt(velParts[0].trim());
            int velocityY = Integer.parseInt(velParts[1].trim());
            // store as {{posX,posY},{velX,velY}}
            robots.add(new int[][]{{positionX, positionY}, {velocityX, velocityY}});
        }
        return robots;
    }

    public static List<int[]> update_positions(List<int[][]> robots, int gridWidth, int gridHeight, int seconds) {
        List<int[]> newPositions = new ArrayList<>();
        for (int[][] r : robots) {
            int positionX = r[0][0];
            int positionY = r[0][1];
            int velocityX = r[1][0];
            int velocityY = r[1][1];
            int newX = Math.floorMod(positionX + velocityX * seconds, gridWidth);
            int newY = Math.floorMod(positionY + velocityY * seconds, gridHeight);
            newPositions.add(new int[]{newX, newY});
        }
        return newPositions;
    }

    public static int[] count_robots_in_quadrants(List<int[]> position, int gridWidth, int gridHeight) {
        int midX = gridWidth / 2;
        int midY = gridHeight / 2;
        int[] quadrant_counts = new int[]{0, 0, 0, 0};
        for (int[] p : position) {
            int x = p[0];
            int y = p[1];
            if (x == midX || y == midY) {  // Ignore if on middle line
                continue;
            } else if (x < midX && y < midY) {  // Top Left
                quadrant_counts[0] += 1;
            } else if (x >= midX && y < midY) {  // Top Right
                quadrant_counts[1] += 1;
            } else if (x < midX && y >= midY) {  // Bottom Left
                quadrant_counts[2] += 1;
            } else if (x >= midX && y >= midY) {  // Bottom Right
                quadrant_counts[3] += 1;
            }
        }
        return quadrant_counts;
    }

    public static long calculate_safety_factor(int[] quadrant_counts) {
        long factor = 1;
        for (int count : quadrant_counts) {
            factor *= count;
        }
        return factor;
    }

    // Input and Output
    public static void main(String[] args) throws Exception {
        System.out.println("Enter robot data, one robot per line. Submit an empty line to finish:");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        String line;
        while (true) {
            line = reader.readLine();
            if (line == null) break;           // EOF safety
            if (line.strip().isEmpty()) break; // stop on empty line
            sb.append(line).append("\n");
        }
        String userInput = sb.toString();
        if (userInput.strip().isEmpty()) {
            System.out.println("No robot data provided.");
            return;
        }
        List<int[][]> robots = get_robot_data(userInput);
        List<int[]> positionsAfterTime = update_positions(robots, gridWidth, gridHeight, seconds);
        int[] quadrantCounts = count_robots_in_quadrants(positionsAfterTime, gridWidth, gridHeight);
        long safetyFactor = calculate_safety_factor(quadrantCounts);

        System.out.println(String.format("Safety Factor: %d", safetyFactor));
    }
}

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class AntinodeCounter {
    public static int count_antinodes(List<String> mapInput) {
        Map<Character, Set<Position>> antenna = new HashMap<>();
        int numberOfRows = mapInput.size();
        int numberOfColumn = numberOfRows > 0 ? mapInput.get(0).length() : 0;
        Set<Position> antinodes = new HashSet<>(); // <- To store unique antinode positions

        // Find antennas and their positions
        for (int row = 0; row < mapInput.size(); row++) {
            String line = mapInput.get(row);
            for (int column = 0; column < line.length(); column++) {
                char character = line.charAt(column);
                if (character != '.') {
                    antenna.computeIfAbsent(character, k -> new HashSet<>()).add(new Position(row, column));
                }
            }
        }

        // Check for antinodes in each frequency "type"
        for (Map.Entry<Character, Set<Position>> entry : antenna.entrySet()) {
            List<Position> positions = new ArrayList<>(entry.getValue());
            int n = positions.size();
            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j < n; j++) {
                    Position p1 = positions.get(i);
                    Position p2 = positions.get(j);
                    antinodes.add(new Position(2 * p1.row - p2.row, 2 * p1.col - p2.col));
                    antinodes.add(new Position(2 * p2.row - p1.row, 2 * p2.col - p1.col));
                }
            }
        }

        int answer = 0;
        for (Position p : antinodes) {
            int r = p.row;
            int c = p.col;
            if (0 <= r && r < numberOfRows && 0 <= c && c < numberOfColumn) {
                answer += 1;
            }
        }
        return answer;
    }

    // To represent a position and support uniqueness in a Set
    private static class Position {
        public final int row;
        public final int col;

        public Position(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Position)) return false;
            Position other = (Position) o;
            return this.row == other.row && this.col == other.col;
        }

        @Override
        public int hashCode() {
            return 31 * row + col;
        }
    }

    // Input and Output
    public static void main(String[] args) {
        System.out.println("Enter map (using '.' for empty spaces and a single letter/digit for antennas)");
        System.out.println("Press Enter on an empty line to submit.");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> mapInput = new ArrayList<>();
        try {
            while (true) {
                String line = reader.readLine();
                if (line == null) { // EOF
                    break;
                }
                line = line.trim();
                if (line.isEmpty()) {
                    break;
                }
                mapInput.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        int answer = count_antinodes(mapInput);
        System.out.println("The number of unique antinode positions is: " + answer);
    }
}

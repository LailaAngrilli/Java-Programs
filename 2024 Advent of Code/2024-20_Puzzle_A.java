import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Main {
    static int numberRows;
    static int numberColumns;
    static Map<Pair, Integer> distance = new HashMap<>();

    static class Pair {
        final int r;
        final int c;
        Pair(int r, int c) { this.r = r; this.c = c; }
        @Override public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Pair)) return false;
            Pair p = (Pair)o;
            return r == p.r && c == p.c;
        }
        @Override public int hashCode() {
            return 31 * r + c;
        }
    }

    // Translated count_shortcuts
    static Map<Integer, Integer> count_shortcuts(int maxCheat, int targetSavings) {
        Map<Integer, Integer> shortcuts = new HashMap<>();

        for (int row = 0; row < numberRows; row++) {
            for (int column = 0; column < numberColumns; column++) {
                Pair key = new Pair(row, column);
                if (!distance.containsKey(key)) {
                    continue;
                }
                for (int lengthCheat = 2; lengthCheat <= maxCheat; lengthCheat++) {
                    for (int directionsRow = 0; directionsRow <= lengthCheat; directionsRow++) {
                        int directionsColumns = lengthCheat - directionsRow;
                        Set<Pair> positions = new HashSet<>();
                        positions.add(new Pair(row + directionsRow, column + directionsColumns));
                        positions.add(new Pair(row + directionsRow, column - directionsColumns));
                        positions.add(new Pair(row - directionsRow, column + directionsColumns));
                        positions.add(new Pair(row - directionsRow, column - directionsColumns));
                        for (Pair p : positions) {
                            if (!distance.containsKey(p)) continue;
                            int timeSaved = distance.get(p) - distance.get(key) - lengthCheat;
                            if (timeSaved >= targetSavings) {
                                shortcuts.put(timeSaved, shortcuts.getOrDefault(timeSaved, 0) + 1);
                            }
                        }
                    }
                }
            }
        }

        return shortcuts;
    }

    static List<String> readGridLinesUntilEmpty() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        List<String> lines = new ArrayList<>();
        while (true) {
            String ln = br.readLine();
            if (ln == null) break; // EOF
            if (ln.isEmpty()) break;
            lines.add(ln);
        }
        return lines;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        // Input
        System.out.print("How many picoseconds do you want to save? ");
        String targetLine = br.readLine();
        int targetSavings = Integer.parseInt(targetLine.trim());

        // Prompt and read grid
        System.out.println();
        System.out.println("Enter each line,line by line, of the grid.");
        System.out.println("Press Enter on empty line to submit.");
        List<String> gridInput = readGridLinesUntilEmpty();
        // mimic map(str.strip, gridInput)
        List<String> grid = new ArrayList<>();
        for (String s : gridInput) {
            grid.add(s.trim());
        }

        numberRows = grid.size();
        numberColumns = grid.get(0).length();

        int r = -1, c = -1;
        outer:
        for (int rr = 0; rr < grid.size(); rr++) {
            String rowStr = grid.get(rr);
            for (int cc = 0; cc < rowStr.length(); cc++) {
                char ch = rowStr.charAt(cc);
                if (ch == 'S') {
                    r = rr;
                    c = cc;
                    break outer;
                }
            }
        }

        Deque<int[]> queue = new ArrayDeque<>();
        queue.addFirst(new int[] { r, c, 0 });
        distance = new HashMap<>();
        while (!queue.isEmpty()) {
            int[] triple = queue.removeFirst();
            int rr = triple[0];
            int cc = triple[1];
            int d = triple[2];
            Pair p = new Pair(rr, cc);
            
            if (distance.containsKey(p)) continue;
            distance.put(p, d);
            int[][] neigh = { { rr, cc - 1 }, { rr, cc + 1 }, { rr - 1, cc }, { rr + 1, cc } };
            for (int[] nb : neigh) {
                int numberR = nb[0];
                int numberC = nb[1];
                
                if (numberR < 0 || numberR >= numberRows || numberC < 0 || numberC >= numberColumns) continue;
                if (grid.get(numberR).charAt(numberC) != '#' && !distance.containsKey(new Pair(numberR, numberC))) {
                    queue.addFirst(new int[] { numberR, numberC, d + 1 });
                }
            }
        }

        Map<Integer, Integer> shortcuts = count_shortcuts(2, targetSavings);
        int cheatCount = 0;
        for (int v : shortcuts.values()) cheatCount += v;

        if (cheatCount == 1) {
            System.out.println(String.format("There is one cheat to shave off %d picoseconds", targetSavings));
        } else if (cheatCount == 0) {
            System.out.println(String.format("There are no cheats to shave off %d picoseconds", targetSavings));
        } else {
            System.out.println(String.format("There are %d cheats to shave off %d picoseconds", cheatCount, targetSavings));
        }
    }
}

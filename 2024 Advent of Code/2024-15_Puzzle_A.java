import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class GridMover {
    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        // Grid input
        System.out.println("Enter the grid (press Enter on empty line to submit): \n");
        List<String> gridInput = new ArrayList<>();
        while (true) {
            String row = reader.readLine();
            if (row == null) break; // EOF guard
            if (row.trim().equals("")) {
                break;
            }
            gridInput.add(row.trim());
        }

        // Convert to char[][] grid like Python list of lists
        int rows = gridInput.size();
        int cols = rows > 0 ? gridInput.get(0).length() : 0;
        char[][] grid = new char[rows][];
        for (int i = 0; i < rows; i++) {
            grid[i] = gridInput.get(i).toCharArray();
        }

        // Moves input
        System.out.print("Enter the moves (all one line, no spaces): \n");
        String movesLine = reader.readLine();
        if (movesLine == null) movesLine = "";
        String moves = movesLine.trim().replace(" ", "");

        // Moves map
        Map<Character, int[]> moveMap = new HashMap<>();
        moveMap.put('<', new int[] {0, -1});
        moveMap.put('^', new int[] {-1, 0});
        moveMap.put('>', new int[] {0, 1});
        moveMap.put('v', new int[] {1, 0});

        // Find initial @ position
        int r = -1, c = -1;
        outer:
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == '@') {
                    r = i;
                    c = j;
                    break outer;
                }
            }
        }

        // Apply moves
        for (int mi = 0; mi < moves.length(); mi++) {
            char move = moves.charAt(mi);
            int[] dir = moveMap.get(move);
            int directionRow = dir[0];
            int directionColumn = dir[1];
            int newRow = r + directionRow;
            int newColumn = c + directionColumn;
            boolean doMove = true;
            while (true) {
                if (grid[newRow][newColumn] == '#') {
                    doMove = false;
                    break;
                }
                if (grid[newRow][newColumn] == '.') {
                    break;
                }
                if (grid[newRow][newColumn] == 'O') {
                    newRow = newRow + directionRow;
                    newColumn = newColumn + directionColumn;
                } else {
                    throw new AssertionError();
                }
            }
            if (!doMove) {
                continue;
            }
            grid[r][c] = '.';
            r = r + directionRow;
            c = c + directionColumn;
            if (grid[r][c] == 'O') {
                grid[newRow][newColumn] = 'O';
            }
            grid[r][c] = '@';
        }

        // Calculate and output the answer
        long answer = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == 'O') {
                    answer += i * 100 + j;
                }
            }
        }
        System.out.println("\nThe sum of all boxes' GPS coordinates is " + answer);
    }
}

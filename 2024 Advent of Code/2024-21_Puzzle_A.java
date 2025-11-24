import java.util.*;
import java.io.*;

public class Main {
    static final Map<Point, String> numberPad = createNumberPad();
    static final Map<Point, String> directionalPad = createDirectionalPad();

    static class Point {
        final int r;
        final int c;
        Point(int r, int c) { this.r = r; this.c = c; }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Point)) return false;
            Point p = (Point) o;
            return r == p.r && c == p.c;
        }
        @Override
        public int hashCode() {
            return Objects.hash(r, c);
        }
    }

    static Map<Point, String> createNumberPad() {
        Map<Point, String> m = new HashMap<>();
        m.put(new Point(0,0), "7");
        m.put(new Point(0,1), "8");
        m.put(new Point(0,2), "9");
        m.put(new Point(1,0), "4");
        m.put(new Point(1,1), "5");
        m.put(new Point(1,2), "6");
        m.put(new Point(2,0), "1");
        m.put(new Point(2,1), "2");
        m.put(new Point(2,2), "3");
        m.put(new Point(3,1), "0");
        m.put(new Point(3,2), "A");
        return m;
    }

    static Map<Point, String> createDirectionalPad() {
        Map<Point, String> m = new HashMap<>();
        m.put(new Point(0,1), "^");
        m.put(new Point(0,2), "A");
        m.put(new Point(1,0), "<");
        m.put(new Point(1,1), "v");
        m.put(new Point(1,2), ">");
        return m;
    }

    static List<String> findBestXToY(Map<Point, String> keypad, Point x, Point y) {
        if (x.equals(y)) {
            return Collections.singletonList("A");
        }
        Deque<State> queue = new ArrayDeque<>();
        queue.addLast(new State(x.r, x.c, ""));
        int bestLength = 100;
        List<String> optimalPaths = new ArrayList<>();
        while (!queue.isEmpty()) {
            State s = queue.removeFirst();
            int row = s.row;
            int column = s.col;
            String path = s.path;
            int[][] moves = {{1,0},{-1,0},{0,1},{0,-1}};
            String[] mvChars = {"v","^",">","<"};
            for (int i = 0; i < moves.length; i++) {
                int newRow = row + moves[i][0];
                int newColumn = column + moves[i][1];
                Point np = new Point(newRow, newColumn);
                if (!keypad.containsKey(np)) {
                    continue;
                }
                if (path.length() + 1 > bestLength) {
                    return optimalPaths;
                }
                if (newRow == y.r && newColumn == y.c) {
                    optimalPaths.add(path + mvChars[i] + "A");
                    bestLength = Math.min(path.length() + 2, bestLength);
                } else {
                    queue.addLast(new State(newRow, newColumn, path + mvChars[i]));
                }
            }
        }
        return optimalPaths;
    }

    static class State {
        int row;
        int col;
        String path;
        State(int r, int c, String p) { row = r; col = c; path = p; }
    }

    static List<String> keysToPaths(Map<PairString, List<String>> keypadPaths, String code) {
        String pref = "A" + code;
        List<List<String>> paths = new ArrayList<>();
        for (int i = 0; i < code.length(); i++) {
            char buttonOne = pref.charAt(i);
            char buttonTwo = code.charAt(i);
            PairString key = new PairString(String.valueOf(buttonOne), String.valueOf(buttonTwo));
            List<String> val = keypadPaths.get(key);
            if (val == null) val = Collections.emptyList();
            paths.add(val);
        }
        List<String> results = new ArrayList<>();
        if (paths.isEmpty()) {
            results.add("");
            return results;
        }
        results.add("");
        for (List<String> group : paths) {
            List<String> next = new ArrayList<>();
            for (String prefix : results) {
                for (String seg : group) {
                    next.add(prefix + seg);
                }
            }
            results = next;
        }
        return results;
    }

    static class PairString {
        final String a;
        final String b;
        PairString(String a, String b) { this.a = a; this.b = b; }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof PairString)) return false;
            PairString p = (PairString) o;
            return Objects.equals(a, p.a) && Objects.equals(b, p.b);
        }
        @Override
        public int hashCode() {
            return Objects.hash(a, b);
        }
    }

    static List<String> checkEnterOnEmpty(List<String> lines, int line) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            if (!scanner.hasNextLine()) break;
            String input = scanner.nextLine();
            if (input == null || input.isEmpty()) break;
            lines.add(input);
            line++;
        }
        return lines;
    }

    public static void main(String[] args) throws Exception {
        Map<PairString, List<String>> numberPadPaths = new HashMap<>();
        List<Point> numberPadKeys = new ArrayList<>(numberPad.keySet());
        for (Point pathOne : numberPadKeys) {
            for (Point pathTwo : numberPadKeys) {
                PairString key = new PairString(numberPad.get(pathOne), numberPad.get(pathTwo));
                List<String> val = findBestXToY(numberPad, pathOne, pathTwo);
                numberPadPaths.put(key, val);
            }
        }

        Map<PairString, List<String>> directionalPadPaths = new HashMap<>();
        List<Point> directionalPadKeys = new ArrayList<>(directionalPad.keySet());
        for (Point pathOne : directionalPadKeys) {
            for (Point pathTwo : directionalPadKeys) {
                PairString key = new PairString(directionalPad.get(pathOne), directionalPad.get(pathTwo));
                List<String> val = findBestXToY(directionalPad, pathOne, pathTwo);
                directionalPadPaths.put(key, val);
            }
        }

        System.out.println("Enter each code, line by line. Press Enter on empty line to submit.\n");
        List<String> lines = new ArrayList<>();
        int line = 0;
        lines = checkEnterOnEmpty(lines, line);

        long complexitiesSum = 0;
        for (String code : lines) {
            int bestLength = Integer.MAX_VALUE;
            for (String pathOne : keysToPaths(numberPadPaths, code)) {
                for (String pathTwo : keysToPaths(directionalPadPaths, pathOne)) {
                    for (String pathThree : keysToPaths(directionalPadPaths, pathTwo)) {
                        bestLength = Math.min(pathThree.length(), bestLength);
                    }
                }
            }
            int prefixInt = Integer.parseInt(code.substring(0, code.length() - 1));
            complexitiesSum += (long) prefixInt * (long) bestLength;
        }

        System.out.println(String.format("The sum of the %d complexities is: %d", lines.size(), complexitiesSum));
    }
}

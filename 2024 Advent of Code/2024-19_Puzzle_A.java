import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.HashMap;

public class Main {
    public static String getUserInput() {
        System.out.println("Enter the data in the following format:" +
                "\nFirst Line: Towel patterns seperated by comma (e.g., PatternOne, PatternTwo)" +
                "\nSecond Line: Blank" +
                "\nSubsequent Lines: Desired designs (one per line)" +
                "\nTo Submit: Press Enter on empty line to submit."
        );

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            StringBuilder sb = new StringBuilder();
            String line;
            boolean first = true;
            while ((line = reader.readLine()) != null) {
                if (!first) {
                    sb.append("\n");
                }
                sb.append(line);
                first = false;
            }
            String userInput = sb.toString().trim();
            // Check format
            if (!userInput.contains("\n\n")) {
                throw new IllegalArgumentException("Invalid input format. Ensure towel patterns and designs are separated by a blank line.");
            }
            return userInput;
        } catch (IOException e) {
            throw new IllegalArgumentException("Please ensure the format is correct.");
        }
    }

    public static String[] parseInputParts(String inputData) {
        String[] parts = inputData.trim().split("\\n\\n", 2);
        if (parts.length < 2) {
            throw new IllegalArgumentException("Invalid input format. Ensure towel patterns and designs are separated by a blank line.");
        }
        return parts;
    }

    public static String[] parseTowelPatterns(String part0) {
        return part0.split(", ");
    }

    public static String[] parseDesigns(String part1) {
        return part1.split("\n");
    }

    public static boolean canConstruct(String design, String[] patterns, Map<String, Boolean> memo) {
        if (memo.containsKey(design)) {
            return memo.get(design);
        }
        if (design.isEmpty()) {
            return true;
        }

        for (String pattern : patterns) {
            if (design.startsWith(pattern)) {
                String remainder = design.substring(pattern.length());
                if (canConstruct(remainder, patterns, memo)) {
                    memo.put(design, true);
                    return true;
                }
            }
        }

        memo.put(design, false);
        return false;
    }

    public static int countPossibleDesigns(String inputData) {
        String[] parts = parseInputParts(inputData);
        String[] towelPatterns = parseTowelPatterns(parts[0]);
        String[] designs = parseDesigns(parts[1]);

        int count = 0;
        Map<String, Boolean> memo = new HashMap<>();

        for (String design : designs) {
            if (canConstruct(design, towelPatterns, memo)) {
                count += 1;
            }
        }
        return count;
    }

    public static void main(String[] args) {
        String userInput = getUserInput();
        int answer = countPossibleDesigns(userInput);
        System.out.println("Number of possible designs: " + answer);
    }
}

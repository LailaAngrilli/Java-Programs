import java.util.*;
import java.util.regex.*;

public class MulParser {
    // Variables and Et Cetera
    static String mulSequence = "";

    public static void parseAndSum(String stringInput) {
        // Extract pattern
        String pattern = "mul\$(\\d{1,3}),(\\d{1,3})\$";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(stringInput);

        // Convert pairs from string to integers and then calculate sum
        List<int[]> pairs = new ArrayList<>();
        while (matcher.find()) {
            int x = Integer.parseInt(matcher.group(1));
            int y = Integer.parseInt(matcher.group(2));
            pairs.add(new int[]{x, y});
        }

        int totalSum = 0;
        StringBuilder outputPairs = new StringBuilder();
        StringBuilder outputSum = new StringBuilder();

        for (int[] pair : pairs) {
            totalSum += pair[0] * pair[1];
            outputPairs.append(Arrays.toString(pair)).append(", ");
            outputSum.append(pair[0]).append("*").append(pair[1]).append(" + ");
        }

        // Display Output
        System.out.println("The mul pairs are: " + outputPairs.toString().replaceAll(", $", "") + " \n");
        System.out.println("The total sum: " + outputSum.toString().replaceAll(" \\+ $", "") + " = " + totalSum);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter string input: ");
        mulSequence = scanner.nextLine();
        parseAndSum(mulSequence);
        scanner.close();
    }
}

import java.util.ArrayList;
import java.util.List;

public class StoneProcessor {

    // Variables
    static int blinks = 25;


    // Functions
    public static int process_stone(List<String> stoneInput, int blinks) {
        List<String> stones = stoneInput;
        for (int x = 0; x < blinks; x++) {
            List<String> newStones = new ArrayList<>();
            for (String stone : stones) {
                // Rule 1: [See Advent of Code 2024 Day 11]
                if (stone.equals("0")) {
                    newStones.add("1");
                }
                // Rule 2: [See Advent of Code 2024 Day 11]
                else if (stone.length() % 2 == 0) {
                    int half = stone.length() / 2;
                    String left = stone.substring(0, half).replaceFirst("^0+", "");
                    if (left.isEmpty()) left = "0";
                    String right = stone.substring(half).replaceFirst("^0+", "");
                    if (right.isEmpty()) right = "0";
                    newStones.add(left);
                    newStones.add(right);
                }
                // Rule 3: [See Advent of Code 2024 Day 11]
                else {
                    newStones.add(Integer.toString(Integer.parseInt(stone) * 2024));
                }
            }
            stones = newStones; // <- Updates stone for next blink
        }
        return stones.size();
    }


    // Input and Output
    public static void main(String[] args) {
        // Input
        List<String> stoneInput = new ArrayList<>();
        stoneInput.add("125");
        stoneInput.add("17");
        int totalStones = process_stone(stoneInput, blinks);

        // Output
        if (blinks == 1) {
            System.out.println(String.format("There are %d stones after %d blink.", totalStones, blinks));
        } else {
            System.out.println(String.format("There are %d stones after %d blinks.", totalStones, blinks));
        }
    }
}

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    // Get rules
    public static List<int[]> getRules() {
        System.out.println("Enter rule order in X|Y format. Press Enter on empty line when done.");
        List<int[]> rules = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String ruleInput = scanner.nextLine().trim();
            if (ruleInput.isEmpty()) {  // Stop when pressed Enter on empty line
                break;
            }
            if (ruleInput.contains("|")) {
                try {
                    String[] parts = ruleInput.split("\\|");
                    int x = Integer.parseInt(parts[0]);
                    int y = Integer.parseInt(parts[1]);
                    rules.add(new int[]{x, y});
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Inputs must be integers in the format X|Y");
                }
            } else {
                System.out.println("Invalid input. Inputs must be integers in the format X|Y");
            }
        }
        return rules;
    }

    // Get pages by update
    public static List<List<Integer>> getPages() {
        System.out.println("Enter pages in each update, separated by comma. Press Enter on empty line when done\n");
        List<List<Integer>> pages = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String pageInput = scanner.nextLine().trim();
            if (pageInput.isEmpty()) {  // Stop when pressed Enter on empty line
                break;
            }
            try {
                String[] parts = pageInput.split(",");
                List<Integer> pagesByUpdate = new ArrayList<>();
                for (String part : parts) {
                    pagesByUpdate.add(Integer.parseInt(part.trim()));
                }
                pages.add(pagesByUpdate);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Inputs must be integers separated by commas.\n");
            }
        }
        return pages;
    }

    // Check if in correct order
    public static Object[] checkOrder(List<Integer> pages, List<int[]> rules) {
        for (int[] rule : rules) {
            int x = rule[0];
            int y = rule[1];
            if (pages.contains(x) && pages.contains(y)) {
                if (pages.indexOf(x) > pages.indexOf(y)) {
                    return new Object[]{false, "Breaks Rule " + x + "|" + y};
                }
            }
        }
        return new Object[]{true, "Correct Order"};
    }

    // Get middle number and calculate the sum
    public static Object[] getMiddleAndSum(List<List<Integer>> correctOrder) {
        List<Integer> middleNumbers = new ArrayList<>();
        int totalSum = 0;
        for (List<Integer> pages : correctOrder) {
            int middleIndex = pages.size() / 2;
            middleNumbers.add(pages.get(middleIndex));
            totalSum += pages.get(middleIndex);
        }
        return new Object[]{middleNumbers, totalSum};
    }

    public static void main(String[] args) {
        List<int[]> rules = getRules();
        List<List<Integer>> pages = getPages();
        List<List<Integer>> correctOrder = new ArrayList<>();

        // Prints which are correct and which are incorrect based on rules
        for (int i = 0; i < pages.size(); i++) {
            List<Integer> pageList = pages.get(i);
            Object[] result = checkOrder(pageList, rules);
            boolean isCorrect = (boolean) result[0];
            String message = (String) result[1];
            if (isCorrect) {
                correctOrder.add(pageList);
                System.out.println("Update " + (i + 1) + ": Correct Order");
            } else {
                System.out.println("Update " + (i + 1) + ": Not in the Correct Order -- " + message);
            }
        }
        System.out.println("\n");

        // Prints correct order only
        System.out.println("Updates in Correct Order");
        for (List<Integer> pagesList : correctOrder) {
            System.out.println(pagesList);
        }
        System.out.println("\n");

        // Print middle and sum
        Object[] middleAndSum = getMiddleAndSum(correctOrder);
        List<Integer> middleNumbers = (List<Integer>) middleAndSum[0];
        int totalSum = (int) middleAndSum[1];
        System.out.println("Middle Numbers: " + middleNumbers);
        System.out.println("Total sum of middle numbers: " + totalSum);
    }
}
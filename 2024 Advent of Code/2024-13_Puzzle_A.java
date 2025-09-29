import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ClawMachines {

    // Variables
    static int totalTokens = 0;
    static int totalPrizes = 0;
    static List<String> details = new ArrayList<>();
    static int aTokens = 3;
    static int bTokens = 1;

    static class Pair { int x; int y; Pair(int x, int y){ this.x = x; this.y = y; } }

    public static Pair can_reach_prize(int prizeX, int prizeY, int aX, int aY, int bX, int bY, int aTokensLocal, int bTokensLocal) {
        int minTokens = Integer.MAX_VALUE;
        Pair bestSolution = null;
        for (int aPresses = 0; aPresses <= 100; aPresses++) {
            if (bX == 0 || bY == 0) { // <- Avoid devision by 0
                continue;
            }
            // Calculate remaining movements
            int remainingX = prizeX - aPresses * aX;
            int remainingY = prizeY - aPresses * aY;
            // Ensure remaining movements can be achieved by Button B presses
            if (remainingX % bX == 0 && remainingY % bY == 0) {
                int bPressesX = remainingX / bX;
                int bPressesY = remainingY / bY;
                // Both must match for Button B presses
                if (bPressesX == bPressesY && 0 <= bPressesX && bPressesX <= 100) {
                    int tokens = (aTokensLocal * aPresses) + (bTokensLocal * bPressesX);
                    if (tokens < minTokens) {
                        minTokens = tokens;
                        bestSolution = new Pair(aPresses, bPressesX);
                    }
                }
            }
        }
        return bestSolution;
    }

    // Collect claw machine info from user
    public static List<Map<String, Pair>> get_user_input() {
        List<Map<String, Pair>> machines = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter number of claw machines: ");
        int numberOfMachines = Integer.parseInt(scanner.nextLine().trim());
        for (int i = 0; i < numberOfMachines; i++) {
            System.out.println();
            System.out.println("Enter details for Claw Machine " + (i + 1) + ":");
            System.out.print("Button A X increment: ");
            int aX = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Button A Y increment: ");
            int aY = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Button B X increment: ");
            int bX = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Button B Y increment: ");
            int bY = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Prize X location: ");
            int prizeX = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Prize Y location: ");
            int prizeY = Integer.parseInt(scanner.nextLine().trim());
            Map<String, Pair> m = new HashMap<>();
            m.put("A", new Pair(aX, aY));
            m.put("B", new Pair(bX, bY));
            m.put("Prize", new Pair(prizeX, prizeY));
            machines.add(m);
        }
        return machines;
    }

    public static void main(String[] args) {
        List<Map<String, Pair>> machines = get_user_input();
        for (int i = 0; i < machines.size(); i++) {
            Map<String, Pair> machine = machines.get(i);
            Pair aPair = machine.get("A");
            Pair bPair = machine.get("B");
            Pair prizePair = machine.get("Prize");
            int aX = aPair.x;
            int aY = aPair.y;
            int bX = bPair.x;
            int bY = bPair.y;
            int prizeX = prizePair.x;
            int prizeY = prizePair.y;
            Pair presses = can_reach_prize(prizeX, prizeY, aX, aY, bX, bY, aTokens, bTokens);
            if (presses != null) {
                int aPresses = presses.x;
                int bPresses = presses.y;
                int tokens = (aTokens * aPresses) + (bTokens * bPresses);
                totalTokens += tokens;
                totalPrizes += 1;
                details.add(String.format("Machine %d: Prize won with A: %d B: %d (Tokens Used: %d)",
                    i + 1, aPresses, bPresses, tokens));
            } else {
                details.add(String.format("Machine %d: Prize cannot be won.", i + 1));
            }
        }

        // Outputs
        System.out.println();
        System.out.println();
        System.out.println("Results:");
        System.out.println("Total Prizes Won: " + totalPrizes);
        System.out.println("Total Tokens Used: " + totalTokens);
        System.out.println();
        System.out.println("Details:");
        for (String detail : details) {
            System.out.println(detail);
        }
    }
}

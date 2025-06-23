import java.util.*;
import java.lang.Math;

public class Main {

    public static Scanner scanner = new Scanner(System.in);
    public static Random random = new Random();

    public static List<Integer> getUserInput() {
        // Get stats from user
        System.out.println("List the stats (number) you wish to check on one line:");
        String stats = scanner.nextLine();

        // Turn into list
        List<Integer> numbers = new ArrayList<>();
        if (stats.contains(",")) {
            String[] skills = stats.split(", ");
            for (String skill : skills) {
                numbers.add(Integer.parseInt(skill));
            }
        } else {
            String[] skills = stats.split(" ");
            for (String skill : skills) {
                numbers.add(Integer.parseInt(skill));
            }
        }
        return numbers;
    }


    public static void calculateSuccess(List<Integer> stats) {
        int statCounter = 1;
        for (int stat : stats) {
            System.out.println("For stat " + statCounter + ":");
            // Calculate Normal Success (=< Stat)
            System.out.println("- Normal Success is equal to or less than " + stat);

            // Calculate Hard Success (=< 1/2 Stat)
            int hardSuccess = (int) Math.floor(stat / 2.0);
            System.out.println("- Hard Success is equal to or less than " + hardSuccess);

            // Calculate Extreme Success (=< 1/5 Stat)
            int extremeSuccess = (int) Math.floor(stat / 5.0);
            System.out.println("- Extreme Success is equal to or less than " + extremeSuccess);

            // Increase statCounter and add spacing
            statCounter += 1;
            System.out.println();
        }
    }


    public static int rollDice() {
        int tensDie = random.nextInt(10) * 10;
        int unitsDie = random.nextInt(10);
        int roll;
        if (tensDie == 0 && unitsDie == 0) {
            roll = 100;
        } else {
            roll = tensDie + unitsDie;
        }
        return roll;
    }


    public static void rollForSuccess(List<Integer> stats) {
        String rollDiceInput = "";
        String advantageDisadvantageInput = "";
        int statCheck = 0;

        while (!rollDiceInput.equalsIgnoreCase("Y") && !rollDiceInput.equalsIgnoreCase("N")) {
            System.out.println("Roll dice? (Y/N)");
            rollDiceInput = scanner.nextLine();
            if (rollDiceInput.equalsIgnoreCase("Y")) {
                System.out.println();
                int roll = rollDice();
                while (!advantageDisadvantageInput.equalsIgnoreCase("Y") && !advantageDisadvantageInput.equalsIgnoreCase("N")) {
                    System.out.println("For all, roll with Advantage/Disadvantage? ");
                    advantageDisadvantageInput = scanner.nextLine();
                    if (advantageDisadvantageInput.equalsIgnoreCase("Y")) {
                        int secondRoll = rollDice();
                        String determineType = "";
                        System.out.println();
                        while (!determineType.equalsIgnoreCase("ADVANTAGE") && !determineType.equalsIgnoreCase("DISADVANTAGE")) {
                            System.out.println("Advantage or Disadvantage?");
                            determineType = scanner.nextLine();
                            System.out.println();
                            // If with disadvantage
                            if (determineType.equalsIgnoreCase("DISADVANTAGE")) {
                                statCheck = Math.min(roll, secondRoll);
                            }
                            // If with advantage
                            else if (determineType.equalsIgnoreCase("ADVANTAGE")) {
                                statCheck = Math.max(roll, secondRoll);
                            } else {
                                System.out.println("Not a valid response; please try again.");
                            }
                        }
                    } else if (advantageDisadvantageInput.equalsIgnoreCase("N")) {
                        statCheck = roll;
                    } else {
                        System.out.println("Not a valid response; please try again.");
                    }
                }
            } else if (rollDiceInput.equalsIgnoreCase("N")) {
                break;
            } else {
                System.out.println("Not a valid response; please try again.");
            }
        }

        // Check against stats
        System.out.println("You rolled a " + statCheck + "!");
        for (int stat : stats) {
            System.out.println("For " + stat + "...");
            int normalSuccess = stat;
            int hardSuccess = (int) Math.floor(stat / 2.0);
            int extremeSuccess = (int) Math.floor(stat / 5.0);
            if (statCheck <= extremeSuccess) {
                System.out.println("You rolled an Extreme Success!");
            } else if (statCheck <= hardSuccess) {
                System.out.println("You rolled a Hard Success!");
            } else if (statCheck <= normalSuccess) {
                System.out.println("You rolled a Normal Success!");
            } else {
                if (stat < 50) {
                    if (statCheck >= 96) {
                        System.out.println("You rolled a fumble!");
                    } else {
                        System.out.println("You failed the roll!");
                    }
                } else if (statCheck == 100) {
                    System.out.println("You rolled a fumble!");
                } else {
                    System.out.println("You failed the roll!");
                }
            }
        }

    }




    public static void main(String[] args) {
        List<Integer> stats = getUserInput();
        calculateSuccess(stats);
        rollForSuccess(stats);
    }

}
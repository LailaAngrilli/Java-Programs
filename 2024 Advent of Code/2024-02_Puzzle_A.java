import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MatrixInputChecker {
    // Function to get matrix input
    public static List<List<Integer>> getMatrixInput() {
        Scanner scanner = new Scanner(System.in);
        // Get input for number of rows and number of numbers per row
        int rowNumber = CheckUserInput.checkInteger("Enter the number of rows: ");
        int numberPerRow = CheckUserInput.checkInteger("Enter the number of numbers per rows: ");

        List<List<Integer>> rowStorage = new ArrayList<>();
        // Create rows
        for (int i = 0; i < rowNumber; i++) {
            System.out.println("Enter the numbers for row " + (i + 1) + ": ");
            List<Integer> row = new ArrayList<>();
            while (row.size() < numberPerRow) {
                try {
                    // Collect the individual numbers
                    System.out.print("Enter number " + (row.size() + 1) + " for row " + (i + 1) + ": ");
                    int number = Integer.parseInt(scanner.nextLine());
                    row.add(number);
                } catch (NumberFormatException e) {
                    System.out.println("That's not a whole number. Try again!");
                }
            }
            rowStorage.add(row);
        }
        return rowStorage;
    }

    // Function to check if a row is safe
    public static boolean isRowSafe(List<Integer> row) {
        // Check if increasing or decreasing
        boolean rowIncreasing = true;
        boolean rowDecreasing = true;
        for (int i = 0; i < row.size() - 1; i++) {
            if (row.get(i) >= row.get(i + 1)) {
                rowIncreasing = false;
            }
            if (row.get(i) <= row.get(i + 1)) {
                rowDecreasing = false;
            }
        }
        if (!(rowIncreasing || rowDecreasing)) {
            return false;
        }

        // Check if differences are within 1-3
        for (int i = 0; i < row.size() - 1; i++) {
            int diff = Math.abs(row.get(i) - row.get(i + 1));
            if (diff < 1 || diff > 3) {
                return false;
            }
        }
        return true;
    }

    // Function to count number of safe reports
    public static int countSafe(List<List<Integer>> matrix) {
        int safeCounter = 0;
        for (List<Integer> row : matrix) {
            if (isRowSafe(row)) {
                safeCounter++;
            }
        }
        return safeCounter;
    }

    // Main method to run the program
    public static void main(String[] args) {
        List<List<Integer>> reportMatrix = getMatrixInput();
        int safeReportsNumber = countSafe(reportMatrix);
        if (safeReportsNumber == 1) {
            System.out.println(safeReportsNumber + " report is safe.");
        } else {
            System.out.println(safeReportsNumber + " reports are safe.");
        }
    }
}
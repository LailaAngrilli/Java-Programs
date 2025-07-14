import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static int checkInteger(String prompt) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.print(prompt);
                int userInput = Integer.parseInt(scanner.nextLine());
                return userInput;
            } catch (NumberFormatException e) {
                System.out.println("That's not a whole number. Try again!");
                continue;
            }
        }
    }

    public static List<String> checkEnterOnEmpty(List<String> listName) {
        Scanner scanner = new Scanner(System.in);
        String variableName;
        while (true) {
            variableName = scanner.nextLine().trim();
            if (variableName.isEmpty()) {  // Stop when pressed Enter on empty line
                break;
            }
            listName.add(variableName);
        }
        return listName;
    }
}
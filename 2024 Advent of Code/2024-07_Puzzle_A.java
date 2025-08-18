import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class EquationFinder {
    public static class Pair {
        public int result;
        public List<Integer> numbers;

        public Pair(int result, List<Integer> numbers) {
            this.result = result;
            this.numbers = numbers;
        }
    }


    public static int evaluateLeftToRight(List<Integer> numbers, List<String> operators) {
        // result = numbers[0]
        int result = numbers.get(0);
        // for i in range(len(operators)):
        for (int i = 0; i < operators.size(); i++) {
            // if operators[i] == "+":
            if (operators.get(i).equals("+")) {
                // result += numbers[i + 1]
                result += numbers.get(i + 1);
            }
            // elif operators[i] == "*":
            else if (operators.get(i).equals("*")) {
                // result *= numbers[i + 1]
                result *= numbers.get(i + 1);
            }
        }
        // return result
        return result;
    }

    public static List<String> findEquation(int result, List<Integer> numbers) {

        String[] operators = { "+", "*" };

        int numberOfOperators = numbers.size() - 1;

        List<String> equations = new ArrayList<>();

        // Generate all possble combination of operators
        generateCombinations(numbers, operators, numberOfOperators, new ArrayList<>(), result, equations);
        // return equations
        return equations;
    }

    private static void generateCombinations(
            List<Integer> numbers,
            String[] operators,
            int slots,
            List<String> current,
            int target,
            List<String> equations
    ) {
        if (current.size() == slots) {
            if (evaluateLeftToRight(numbers, current) == target) {
                // Build the equation string
                StringBuilder equation = new StringBuilder();
                // equation = str(numbers[0])
                equation.append(numbers.get(0));
                // for i, operator in enumerate(operatorsInEquation):
                for (int i = 0; i < current.size(); i++) {
                    // equation += f" {operator} {numbers[i + 1]}"
                    equation.append(" ")
                            .append(current.get(i))
                            .append(" ")
                            .append(numbers.get(i + 1));
                }

                equations.add(equation.toString());
            }
            return;
        }

        for (String op : operators) {
            current.add(op);
            generateCombinations(numbers, operators, slots, current, target, equations);
            current.remove(current.size() - 1);
        }
    }

    public static void main(String[] args) {
        // Input and Output
        // Ask for input
        Scanner scanner = new Scanner(System.in);
        List<Pair> inputs = new ArrayList<>();

        System.out.println("Enter inputs in the format 'RESULT: NUMBER NUMBER [NUMBER...]'");
        System.out.println("Press Enter on an empty line to submit.");

        while (true) {
            String userInput = scanner.nextLine().trim();
            // if not userInput:
            if (userInput.isEmpty()) {
                break;
            }
            // if ":" not in userInput:
            if (!userInput.contains(":")) {
                System.out.println("Input is not in the correct format. Please try again.");
                continue;
            }
            try {
                String[] parts = userInput.split(":");
                int result = Integer.parseInt(parts[0].trim());
                String[] numStrs = parts[1].trim().split("\\s+");
                List<Integer> numbers = new ArrayList<>();
                for (String s : numStrs) {
                    numbers.add(Integer.parseInt(s));
                }
                inputs.add(new Pair(result, numbers));
            } catch (NumberFormatException e) {
                // except ValueError:
                System.out.println("Invalid format. Please try again.");
            }
        }

        // Prcess Input
        int sumOfFoundResults = 0;

        for (Pair entry : inputs) {
            int result = entry.result;
            List<Integer> numbers = entry.numbers;
            List<String> equations = findEquation(result, numbers);
            if (!equations.isEmpty()) {
                sumOfFoundResults += result;

                System.out.println(result);
                for (String eq : equations) {
                    System.out.println("- " + eq);
                }
                System.out.println();
            } else {
                System.out.println("No equations found for " + result +
                        " with numbers " + numbers + "\n");
            }
        }

        System.out.println("Result: " + sumOfFoundResults);

        scanner.close();
    }
}

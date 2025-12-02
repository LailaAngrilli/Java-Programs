import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SecretNumberGenerator {
    public static int next_number(int x) {
        x ^= (x * 64) % 16777216;
        x &= 0xFFFFFF;
        x ^= (x / 32) % 16777216;
        x &= 0xFFFFFF;
        x ^= (x * 2048) % 16777216;
        x &= 0xFFFFFF;
        return x;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("How many new secret numbers should be generated? ");
        int numberGeneration = Integer.parseInt(sc.nextLine());
        List<Integer> numbersInput = new ArrayList<>();

        System.out.println("\nEnter each number, line by line.");
        System.out.println("Press Enter on an empty line to submit.");

        while (true) {
            String line = sc.nextLine().strip();
            if (line.isEmpty()) {
                break;
            }
            try {
                numbersInput.add(Integer.parseInt(line));
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer.");
            }
        }

        int answer = 0;
        for (int number : numbersInput) {
            for (int 番号 = 0; 番号 < 2000; 番号++) {
                number = next_number(number);
            }
            answer += number;
        }

        long unsignedAnswer = Integer.toUnsignedLong(answer);
        System.out.println("The sum of the secret numbers is " + unsignedAnswer);
        sc.close();
    }
}

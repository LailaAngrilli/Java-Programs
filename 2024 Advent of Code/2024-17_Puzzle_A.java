import java.util.*;
import java.util.stream.*;

public class Main {
    static class Regs {
        int A, B, C;
        Regs(int a, int b, int c) { A = a; B = b; C = c; }
    }

    static int getCombinationOperandValue(int operand, Regs regs) {
        if (operand <= 3) {
            return operand;
        } else if (operand == 4) {
            return regs.A;
        } else if (operand == 5) {
            return regs.B;
        } else if (operand == 6) {
            return regs.C;
        } else {
            throw new IllegalArgumentException("Invalid combo operand (7 is reserved).");
        }
    }

    static int moduloEight(int value) {
        return value % 8;
    }

    public static String run_program(int[] registers, List<Integer> program) {
        Regs regs = new Regs(registers[0], registers[1], registers[2]);
        int instructionPointer = 0;
        List<Integer> output = new ArrayList<>();

        while (instructionPointer < program.size()) {
            int opcode = program.get(instructionPointer);
            int operand = program.get(instructionPointer + 1);
            instructionPointer += 2;
            
            if (opcode == 0) {
                int denom = (int) Math.pow(2, getCombinationOperandValue(operand, regs));
                regs.A = regs.A / denom;
            } else if (opcode == 1) {
                regs.B = regs.B ^ operand;
            } else if (opcode == 2) {
                regs.B = moduloEight(getCombinationOperandValue(operand, regs));
            } else if (opcode == 3) {
                if (regs.A != 0) {
                    instructionPointer = operand;
                }
            } else if (opcode == 4) {
                regs.B = regs.B ^ regs.C;
            } else if (opcode == 5) {
                output.add(moduloEight(getCombinationOperandValue(operand, regs)));
            } else if (opcode == 6) {
                int denom = (int) Math.pow(2, getCombinationOperandValue(operand, regs));
                regs.B = regs.A / denom;
            } else if (opcode == 7) { // cdv
                int denom = (int) Math.pow(2, getCombinationOperandValue(operand, regs));
                regs.C = regs.A / denom;
            } else {
                throw new IllegalArgumentException("Invalid opcode: " + opcode);
            }
        }
        // Output as string list
        return output.stream().map(String::valueOf).collect(Collectors.joining(","));
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Get user input and then output
        System.out.println("Enter initial register values (A, B, C) separated by commas: ");
        String registersInput = scanner.nextLine().trim();
        String[] regParts = registersInput.split(",");
        int[] registers = new int[3];
        for (int i = 0; i < 3; i++) {
            registers[i] = Integer.parseInt(regParts[i].trim());
        }

        System.out.println("Enter program as a comma-separated list of 3-bit numbers: ");
        String programInput = scanner.nextLine().trim();
        String[] progParts = programInput.split(",");
        List<Integer> program = new ArrayList<>();
        for (String p : progParts) {
            program.add(Integer.parseInt(p.trim()));
        }

        String output = run_program(registers, program);
        System.out.println("The output is " + output);
    }
}

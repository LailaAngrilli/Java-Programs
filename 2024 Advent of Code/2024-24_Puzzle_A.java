import java.util.*;
import java.util.stream.Collectors;

public class GateSimulator {
    public static int simulate_gates(Map<String, Integer> inputValues, List<String> gateDefinitions) {
        Map<String, Integer> wires = new HashMap<>();
        List<Gate> gates = new ArrayList<>();

        for (Map.Entry<String, Integer> e : inputValues.entrySet()) {
            wires.put(e.getKey(), e.getValue().intValue());
        }


        for (String definition : gateDefinitions) {
            try {
                String[] parts = definition.split(" -> ");
                if (parts.length != 2) {
                    throw new IllegalArgumentException("Invalid gate definition: " + definition);
                }
                String operation = parts[0];
                String outputWire = parts[1];
                if (operation.contains(" AND ")) {
                    String[] ab = operation.split(" AND ");
                    gates.add(new Gate(ab[0], ab[1], "AND", outputWire));
                } else if (operation.contains(" OR ")) {
                    String[] ab = operation.split(" OR ");
                    gates.add(new Gate(ab[0], ab[1], "OR", outputWire));
                } else if (operation.contains(" XOR ")) {
                    String[] ab = operation.split(" XOR ");
                    gates.add(new Gate(ab[0], ab[1], "XOR", outputWire));
                } else {
                    throw new IllegalArgumentException("Unknown operation in: " + operation);
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Error parsing gate definition: " + definition);
                throw e;
            }
        }

        // Evaluate outputs
        List<Gate> unresolvedGates = new ArrayList<>(gates);
        while (!unresolvedGates.isEmpty()) {
            List<Gate> resolvedGates = new ArrayList<>();
            for (Gate g : unresolvedGates) {
                String a = g.a;
                String b = g.b;
                String op = g.op;
                String output = g.output;
                if (wires.containsKey(a) && wires.containsKey(b)) {
                    if ("AND".equals(op)) {
                        wires.put(output, wires.get(a) & wires.get(b));
                    } else if ("OR".equals(op)) {
                        wires.put(output, wires.get(a) | wires.get(b));
                    } else if ("XOR".equals(op)) {
                        wires.put(output, wires.get(a) ^ wires.get(b));
                    }
                    resolvedGates.add(g);
                }
            }
            List<Gate> nextUnresolved = new ArrayList<>();
            for (Gate g : unresolvedGates) {
                if (!resolvedGates.contains(g)) {
                    nextUnresolved.add(g);
                }
            }
            unresolvedGates = nextUnresolved;
        }

        // Z-Wire Outputs
        Map<String, Integer> zWires = wires.entrySet().stream()
            .filter(e -> e.getKey().startsWith("z"))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        if (zWires.isEmpty()) {
            return 0;
        }

        int maxIndex = zWires.keySet().stream()
            .mapToInt(k -> Integer.parseInt(k.substring(1)))
            .max()
            .orElse(0);

        List<String> binaryDigits = new ArrayList<>();
        for (int i = 0; i <= maxIndex; i++) {
            String key = String.format("z%02d", i);
            Integer val = zWires.getOrDefault(key, 0);
            binaryDigits.add(String.valueOf(val));
        }

        // Decimal Conversion
        Collections.reverse(binaryDigits);
        StringBuilder binaryNumber = new StringBuilder();
        for (String d : binaryDigits) binaryNumber.append(d);
        return Integer.parseInt(binaryNumber.toString(), 2);
    }

    public static InputResult get_user_input() {
        // Get wires
        System.out.println("Enter initial wire values (e.g., x00: 1, y01: 0).");
        System.out.println("Press Enter on empty line to submit.");
        Map<String, Integer> inputValues = new HashMap<>();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Wire value: ");
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) break;
            if (line.contains(":")) {
                String[] parts = line.split(":", 2);
                inputValues.put(parts[0].trim(), Integer.parseInt(parts[1].trim()));
            } else {
                System.out.println("Invalid format. Please use 'wire: value' format.");
            }
        }

        // Get gates
        System.out.println("Enter gate definitions (e.g., x00 AND y00 -> z00).");
        System.out.println("Press Enter on empty line to submit.");
        List<String> gateDefinitions = new ArrayList<>();
        while (true) {
            System.out.print("Gate definition: ");
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) break;
            if (line.contains("->")) {
                gateDefinitions.add(line);
            } else {
                System.out.println("Invalid format. Please use 'input OPERATOR input -> output' format.");
            }
        }

        return new InputResult(inputValues, gateDefinitions);
    }

    public static void main(String[] args) {
        InputResult ir = get_user_input();
        int output = simulate_gates(ir.inputValues, ir.gateDefinitions);
        System.out.println("\nThe decimal output is: " + output);
    }


    static class Gate {
        String a;
        String b;
        String op;
        String output;
        Gate(String a, String b, String op, String output) {
            this.a = a;
            this.b = b;
            this.op = op;
            this.output = output;
        }
    }

    static class InputResult {
        Map<String, Integer> inputValues;
        List<String> gateDefinitions;
        InputResult(Map<String, Integer> i, List<String> g) {
            this.inputValues = i;
            this.gateDefinitions = g;
        }
    }
}

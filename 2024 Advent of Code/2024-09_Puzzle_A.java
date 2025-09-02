import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DiskChecksum {
    public static int checksum(List<Integer> diskInput) {
        List<Integer> disk = new ArrayList<>();
        
        for (int i = 0; i < diskInput.size(); i += 2) {
            int countA = diskInput.get(i);
            int valueA = i / 2;
            for (int k = 0; k < countA; k++) {
                disk.add(valueA);
            }
            
            if (i + 1 < diskInput.size()) {
                int countB = diskInput.get(i + 1);
                for (int k = 0; k < countB; k++) {
                    disk.add(-1);
                }
            }
        }

        List<Integer> empty = new ArrayList<>();
        
        for (int idx = 0; idx < disk.size(); idx++) {
            int character = disk.get(idx);
            if (character == -1) {
                empty.add(idx);
            }
        }
        
        int i = 0;
        while (true) {
            while (!disk.isEmpty() && disk.get(disk.size() - 1) == -1) {
                disk.remove(disk.size() - 1);
            }
            if (i >= empty.size()) {
                break;
            }
            int target = empty.get(i);
            if (target >= disk.size()) {
                break;
            }
            
            int last = disk.remove(disk.size() - 1);
            disk.set(target, last);
            i += 1;
        }

        int answer = 0;
        for (int idx = 0; idx < disk.size(); idx++) {
            int character = disk.get(idx);
            answer += idx * character;
        }
        return answer;
    }

    // Input and Output
    public static void main(String[] args) {
        // Input
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter disk map: ");
        String diskInput = scanner.nextLine().strip();
        List<Integer> diskMap = new ArrayList<>();
        for (char c : diskInput.toCharArray()) {
            if (Character.isDigit(c)) {
                diskMap.add(Character.getNumericValue(c));
            }
        }

        diskMap = new ArrayList<>();
        for (char c : diskInput.strip().toCharArray()) {
            if (Character.isDigit(c)) {
                diskMap.add(Character.getNumericValue(c));
            }
        }
        // Output
        int answer = checksum(diskMap);
        System.out.println(String.format("The resulting filesystem checksum is %d", answer));
    }
}

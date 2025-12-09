import java.util.*;
import java.io.*;

public class FindTriplesWithT {
    public static Pair<Integer, List<String>> find_triples_with_t(List<String> connections) {
        // Create the networkMap
        Map<String, Set<String>> networkMap = new HashMap<>();
        for (String connection : connections) {
            if (!connection.contains("-")) continue;
            String[] parts = connection.split("-");
            if (parts.length < 2) continue;
            String a = parts[0];
            String b = parts[1]; // a = computerOne, b = computerTwo
            networkMap.computeIfAbsent(a, k -> new HashSet<>()).add(b);
            networkMap.computeIfAbsent(b, k -> new HashSet<>()).add(a);
        }
        
        Set<String> triples = new LinkedHashSet<>();
        for (String a : networkMap.keySet()) {
            for (String b : networkMap.get(a)) {
                if (b.compareTo(a) > 0) {
                    for (String c : networkMap.get(b)) {
                        if (c.compareTo(b) > 0 && networkMap.get(a).contains(c)) {
                            List<String> sortedTriple = Arrays.asList(a, b, c);
                            Collections.sort(sortedTriple);
                            String tripleStr = String.join(",", sortedTriple);
                            triples.add(tripleStr);
                        }
                    }
                }
            }
        }

        List<String> triples_with_t = new ArrayList<>();
        for (String tripleStr : triples) {
            String[] nodes = tripleStr.split(",");
            for (String node : nodes) {
                if (node.startsWith("t")) {
                    triples_with_t.add(tripleStr);
                    break;
                }
            }
        }
        return new Pair<>(triples_with_t.size(), triples_with_t);
    }

    public static class Pair<K, V> {
        public final K first;
        public final V second;
        public Pair(K first, V second) {
            this.first = first;
            this.second = second;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter connections, one per line.");
        System.out.println("Press Enter on empty to submit.");
        List<String> connections = new ArrayList<>();
        String line = reader.readLine();
        while (line != null && !line.equals("")) {
            if (line.contains("-")) connections.add(line);
            line = reader.readLine();
        }

        Pair<Integer, List<String>> result = find_triples_with_t(connections);
        int numberOftriples = result.first;
        List<String> triples = result.second;

        System.out.println("\nNumber of triples with 'T': " + numberOftriples);
        System.out.println("Triples:");
        for (String triple : triples) {
            System.out.println(triple);
        }
    }
}

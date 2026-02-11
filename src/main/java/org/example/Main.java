package org.example;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Main {

    private static String best = "";

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java -jar program.jar input.txt");
            return;
        }

        Path path = Path.of(args[0]);

        if (!Files.exists(path)) {
            System.out.println("File not found");
            return;
        }

        if (!args[0].toLowerCase().endsWith(".txt")) {
            System.out.println("Only .txt files are allowed.");
            return;
        }

        try {
            List<String> lines = Files.readAllLines(Path.of(args[0]));
            String[] numbers = lines
                .stream()
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toArray(String[]::new);

            if (numbers.length == 0) {
                System.out.println("File is empty.");
                return;
            }

            System.out.println("Processing...");
            long start = System.currentTimeMillis();
            findLongestPuzzle(numbers);
            long end = System.currentTimeMillis();

            System.out.println("The biggest puzzle is: " + best);
            System.out.println("The length is:" + best.length());
            System.out.println("Execution time: " + (end - start) + " ms");


        } catch (IOException e) {
            System.out.println("Error reading file:" + e.getMessage());
        }

    }

    private static Map<String, List<String>> buildGraph(String[] numbers) {

        Map<String, List<String>> graph = new HashMap<>();

        for (String number : numbers) {
            String prefix = number.substring(0, 2);
            graph
                .computeIfAbsent(prefix, k -> new ArrayList<>())
                .add(number);
        }
        return graph;
    }

    public static void findLongestPuzzle(String[] numbers) {

        Map<String, List<String>> graph = buildGraph(numbers);

        for (String number : numbers) {

            Set<String> usedEdges = new HashSet<>();
            usedEdges.add(number);

            dfs(graph, number, usedEdges);
        }
    }

    private static void dfs(Map<String, List<String>> graph, String current, Set<String> usedEdges) {

        if (current.length() > best.length()) {
            best = current;
        }

        String lastTwo = current.substring(current.length() - 2);

        List<String> nextEdges = graph.get(lastTwo);

        if (nextEdges == null) {
            return;
        }

        for (String edge : nextEdges) {
            if (usedEdges.add(edge)) {

                String merged = current + edge.substring(2);

                dfs(graph, merged, usedEdges);

                usedEdges.remove(edge);
            }
        }
    }
}

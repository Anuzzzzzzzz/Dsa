import java.util.ArrayList;
import java.util.List;

public class ReorientConnections {

    static List<List<Integer>> graph;
    static boolean[] visited;
    static int[] reverseCount;

    public static int minReorder(int n, int[][] connections) {
        graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }

        for (int[] connection : connections) {
            int from = connection[0];
            int to = connection[1];
            graph.get(from).add(to);
            graph.get(to).add(-from); // Negative value to indicate reversed edge
        }

        visited = new boolean[n];
        reverseCount = new int[n];

        dfs(0);

        int totalReversals = 0;
        for (int count : reverseCount) {
            if (count > 0) {
                totalReversals++;
            }
        }

        return totalReversals;
    }

    private static void dfs(int node) {
        visited[node] = true;

        for (int neighbor : graph.get(node)) {
            if (!visited[Math.abs(neighbor)]) {
                if (neighbor < 0) {
                    reverseCount[node]++;
                    neighbor = -neighbor;
                }
                dfs(neighbor);
                reverseCount[node] += reverseCount[neighbor];
            }
        }
    }

    public static void main(String[] args) {
        int n = 6;
        int[][] connections = {{0, 1}, {1, 3}, {2, 3}, {4, 0}, {4, 5}};
        int result = minReorder(n, connections);
        System.out.println("Minimum number of reversals: " + result);
    }
}
